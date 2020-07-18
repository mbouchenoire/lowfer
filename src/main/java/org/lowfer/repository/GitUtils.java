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

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.URIish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public final class GitUtils {

    private static final Logger LOG = LoggerFactory.getLogger(GitUtils.class);

    private GitUtils() {
    }

    public static Git pull(GitPullConfig config) throws IOException, GitAPIException, URISyntaxException {
        final String branch = config.getBranch().orElse("master");

        LOG.info("Cloning branch {} of git repository: {}...", branch, config.getUri());

        final Git git = Git.init()
            .setDirectory(config.getDirectory()).call();

        final StoredConfig gitConfig = git.getRepository().getConfig();
        gitConfig.setBoolean("http", null, "sslVerify", config.getGitHostConfig().isSslVerify());
        gitConfig.save();

        git.remoteAdd()
            .setName("origin").setUri(new URIish(config.getUri())).call();

        final PullCommand pullCommand = git.pull()
            .setRemote("origin")
            .setRemoteBranchName(branch);

        config.getGitHostConfig().getSshConfig().ifPresent(sshConfig -> pullCommand.setTransportConfigCallback(
            new SshTransportConfigCallback(sshConfig.getPrivateKeyPath(), sshConfig.getPrivateKeyPassword())));

        pullCommand.call();

        LOG.info("Cloned branch '{}' of git repository ({})", branch, config.getUri());

        return git;
    }
}
