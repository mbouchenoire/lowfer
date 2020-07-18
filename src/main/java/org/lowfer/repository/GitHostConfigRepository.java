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

import org.eclipse.jgit.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class GitHostConfigRepository {

    private static final Logger LOG = LoggerFactory.getLogger(GitHostConfigRepository.class);

    private final GitHostConfig defaultConfig;

    public GitHostConfigRepository(
        @Value("${git.host.default.ssl-verify:true}") boolean sslVerify,
        @Value("${git.host.default.private-key.base64:}") String privateKeyBase64,
        @Value("${git.host.default.private-key.path:}") String configuredPrivateKeyPath,
        @Value("${git.host.default.private-key.password:}") String privateKeyPassword) throws IOException {

        final GitSshConfig sshConfig = getPrivateKeyPath(configuredPrivateKeyPath, privateKeyBase64)
            .map(privateKeyPath -> new GitSshConfig(privateKeyPath, privateKeyPassword))
            .orElse(null);

        this.defaultConfig = new GitHostConfig(sslVerify, sshConfig);
    }

    public static GitHostConfigRepository defaultConfig() {
        try {
            return new GitHostConfigRepository(true, null, null, null);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to build default git host configuration", e);
        }
    }

    private static Optional<String> getPrivateKeyPath(
        @Nullable String configuredPath, @Nullable String privateKeyBase64) throws IOException {

        if (isNotBlank(configuredPath) && isNotBlank(privateKeyBase64)) {
            throw new IllegalArgumentException("Cannot configure both SSH private key value and path");
        }

        if (isNotBlank(configuredPath)) {
            LOG.info("Configured SSH private key path: {}", configuredPath);
            return Optional.of(configuredPath);
        }

        if (isNotBlank(privateKeyBase64)) {
            final Path privateKeyFile = Files.createTempFile("lowfer", "private-key");
            final String decodedPrivateKey = new String(Base64.decode(privateKeyBase64), StandardCharsets.UTF_8);
            Files.write(privateKeyFile, Collections.singletonList(decodedPrivateKey));
            final String createdPath = privateKeyFile.toAbsolutePath().toString();
            LOG.info("Created SSH private key file using configured value: {}", createdPath);
            return Optional.of(createdPath);
        }

        LOG.info("No SSH private key configured");
        return Optional.empty();
    }

    public GitHostConfig fromUri(String uri) {
        // multi-host management is not implemented yet
        return defaultConfig;
    }
}
