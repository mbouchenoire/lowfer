package org.lowfer.repository;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Optional;

public final class GitPullConfig {

    private final String uri;
    private final String branch;
    private final GitHostConfig gitHostConfig;
    private final File directory;

    public GitPullConfig(
        String uri,
        @Nullable String branch,
        GitHostConfig gitHostConfig,
        File directory) {

        this.uri = uri;
        this.branch = branch;
        this.gitHostConfig = gitHostConfig;
        this.directory = directory;
    }

    public String getUri() {
        return uri;
    }

    public Optional<String> getBranch() {
        return Optional.ofNullable(branch);
    }

    public GitHostConfig getGitHostConfig() {
        return gitHostConfig;
    }

    public File getDirectory() {
        return directory;
    }
}
