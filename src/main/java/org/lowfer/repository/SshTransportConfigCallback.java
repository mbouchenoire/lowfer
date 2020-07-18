package org.lowfer.repository;

import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;

public final class SshTransportConfigCallback implements TransportConfigCallback {

    private final String privateKeyPath;
    private final String privateKeyPassword;

    public SshTransportConfigCallback(String privateKeyPath, String privateKeyPassword) {
        this.privateKeyPath = privateKeyPath;
        this.privateKeyPassword = privateKeyPassword;
    }

    @Override
    public void configure(Transport transport) {
        final SshTransport sshTransport = (SshTransport) transport;

        sshTransport.setSshSessionFactory(
            new DefaultJschConfigSessionFactory(privateKeyPath, privateKeyPassword));
    }
}
