/*
 * Copyright 2020 the original author or authors from the Lowfer project.
 *
 * This file is part of the Lowfer project, see https://github.com/mbouchenoire/lowfer
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lowfer.repository;

import io.vavr.collection.Stream;
import io.vavr.concurrent.Future;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.lowfer.domain.common.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class ComponentGitRepository implements AsyncComponentGitRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentGitRepository.class);

    private final String componentName;
    private final Future<Git> gitFuture;

    public ComponentGitRepository(
        String componentName,
        String uri,
        @Nullable String branch,
        GitHostConfig hostConfig,
        Path componentDirectory) {

        this.componentName = componentName;

        final Path gitConfigDirectory = componentDirectory.resolve(".git");

        if (Files.exists(gitConfigDirectory)) {
            LOG.info("Repository '{}' has already been cloned", uri);
            this.gitFuture = Future.of(() -> Git.open(gitConfigDirectory.toFile()));
        } else {
            LOG.info("Cloning git repository: {}...", uri);

            final GitPullConfig pullConfig = new GitPullConfig(uri, branch, hostConfig, componentDirectory.toFile());
            final Future<Git> future = Future.of(() -> GitUtils.pull(pullConfig));

            this.gitFuture = future
                .andThen(repository -> LOG.info("Cloned git repository: {}", uri))
                .onFailure(throwable -> LOG.error("Failed to clone git repository: {}", uri, throwable));

        }
    }

    @Override
    public String getComponentName() {
        return componentName;
    }

    @Override
    public Future<List<Commit>> getCommits(String branchName) {
        if (!gitFuture.isCompleted())
            return gitFuture.map(git -> Collections.emptyList());

        final Git git = gitFuture.get();
        // Those 2 consecutive .get() are not good because
        // they block the current thread. We should do this
        // asynchronously.
        final ObjectId branch = branch(git, branchName).get();
        final Iterable<RevCommit> revCommits = commits(git, branch).get();
        final List<Commit> commits = Stream.ofAll(revCommits)
            .map(Commit::new)
            .collect(toUnmodifiableList());

        return Future.successful(commits);
    }

    private static Future<ObjectId> branch(Git git, String branchName) {
        return Future.of(() -> git.getRepository().resolve("refs/heads/" + branchName));
    }

    private static Future<Iterable<RevCommit>> commits(Git git, ObjectId branch) {
        return Future.of(() -> git.log().add(branch).call());
    }
}
