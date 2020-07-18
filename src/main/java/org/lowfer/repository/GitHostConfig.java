package org.lowfer.repository;

import javax.annotation.Nullable;
import java.util.Optional;

public final class GitHostConfig {

    private final boolean sslVerify;
    private final GitSshConfig sshConfig;

    public GitHostConfig(boolean sslVerify, @Nullable GitSshConfig sshConfig) {
        this.sslVerify = sslVerify;
        this.sshConfig = sshConfig;
    }

    public static GitHostConfig defaultConfig() {
        return new GitHostConfig(true, null);
    }

    public boolean isSslVerify() {
        return sslVerify;
    }

    public Optional<GitSshConfig> getSshConfig() {
        return Optional.ofNullable(sshConfig);
    }
}
