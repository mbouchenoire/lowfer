package org.lowfer.repository;

import io.vavr.collection.Stream;
import io.vavr.concurrent.Future;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.lowfer.domain.common.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class ComponentGitRepository implements AsyncComponentGitRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentGitRepository.class);

    private final String componentName;
    private final Path directory;
    private final Future<Git> gitFuture;

    public ComponentGitRepository(String componentName, String uri, String username, String password, Path componentDirectory) {
        this.componentName = componentName;
        this.directory = componentDirectory;

        final Path gitConfigDirectory = componentDirectory.resolve(".git");

        if (Files.exists(gitConfigDirectory)) {
            LOG.info("Repository '{}' has already been cloned", uri);
            this.gitFuture = Future.of(() -> Git.open(gitConfigDirectory.toFile()));
        } else {
            LOG.info("Cloning git repository: {}...", uri);

            this.gitFuture = Future.of(() -> Git.cloneRepository()
                .setURI(uri)
                .setDirectory(componentDirectory.toFile())
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call())
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
