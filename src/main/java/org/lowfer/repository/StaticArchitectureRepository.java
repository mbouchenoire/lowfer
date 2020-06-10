package org.lowfer.repository;

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.serde.MasterManifestDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StaticArchitectureRepository {

    private static final Logger LOG = LoggerFactory.getLogger(StaticArchitectureRepository.class);

    private final Map<String, SoftwareArchitecture> architectures;

    public StaticArchitectureRepository(
        @Value("${application.architectures.directory}") String architecturesDirectoryPath,
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

        LOG.info("Loaded {} static architecture(s)", architectures.size());
    }

    public Optional<SoftwareArchitecture> findByName(String name) {
        return Optional.ofNullable(architectures.get(name));
    }

    public List<SoftwareArchitecture> findAll() {
        return new ArrayList<>(architectures.values());
    }
}
