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