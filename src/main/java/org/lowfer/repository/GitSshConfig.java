package org.lowfer.repository;

public final class GitSshConfig {

    private final String privateKeyPath;
    private final String privateKeyPassword;

    public GitSshConfig(String privateKeyPath, String privateKeyPassword) {
        this.privateKeyPath = privateKeyPath;
        this.privateKeyPassword = privateKeyPassword;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public String getPrivateKeyPassword() {
        return privateKeyPassword;
    }
}
