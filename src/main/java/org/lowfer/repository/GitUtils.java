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
