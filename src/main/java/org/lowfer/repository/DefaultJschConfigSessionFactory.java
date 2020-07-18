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
