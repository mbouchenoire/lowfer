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

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.serde.MasterManifestDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(prefix = "architectures.", value = "directory")
@ConditionalOnMissingBean(ArchitectureGitRepository.class)
public class ArchitectureDirectoryRepository implements ArchitectureRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ArchitectureDirectoryRepository.class);

    private final Map<String, SoftwareArchitecture> architectures;

    public ArchitectureDirectoryRepository(
        @Value("${architectures.directory}") String architecturesDirectoryPath,
        MasterManifestDeserializer masterManifestDeserializer) {

        final File architecturesDirectory = new File(architecturesDirectoryPath);

        if (!architecturesDirectory.isDirectory())
            throw new IllegalArgumentException("Configured architectures' directory is not a directory (" + architecturesDirectoryPath + ")");

        final File[] masterFiles = architecturesDirectory.listFiles((dir, name) -> name.contains("master"));

        if (masterFiles == null)
            throw new IllegalArgumentException("An I/O error occurred while reading architectures' directory");

        this.architectures = Arrays.stream(masterFiles)
            .map(masterFile -> masterManifestDeserializer.deserializeMasterManifest(masterFile)
                .getOrElseThrow(throwable -> new IllegalStateException("Could not load software architecture", throwable)))
            .collect(Collectors.toMap(SoftwareArchitecture::getName, architecture -> architecture));

        LOG.info("Loaded {} architecture(s) from directory: {}", architectures.size(), architecturesDirectory);
    }

    @Override
    public Optional<SoftwareArchitecture> findByName(String name) {
        return Optional.ofNullable(architectures.get(name));
    }

    @Override
    public List<SoftwareArchitecture> findAll() {
        return new ArrayList<>(architectures.values());
    }
}
