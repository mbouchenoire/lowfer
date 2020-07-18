package org.lowfer.repository;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.util.FS;

public final class DefaultJschConfigSessionFactory extends JschConfigSessionFactory {

    private final String privateKeyPath;
    private final String privateKeyPassword;

    public DefaultJschConfigSessionFactory(String privateKeyPath, String privateKeyPassword) {
        this.privateKeyPath = privateKeyPath;
        this.privateKeyPassword = privateKeyPassword;
    }

    @Override
    protected JSch getJSch(OpenSshConfig.Host hc, FS fs) throws JSchException {
        final JSch jsch = super.getJSch(hc, fs);

        if (privateKeyPath != null) {
            jsch.removeAllIdentity();
            jsch.addIdentity(privateKeyPath, privateKeyPassword);
        }

        return jsch;
    }
}
