package org.lowfer.repository;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.serde.ManifestYamlParser;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;

class ArchitectureGitRepositoryTestIT {

    @Test
    public void testInitializeArchitectureGitRepository() throws IOException, GitAPIException {
        final ArchitectureGitRepository repository = new ArchitectureGitRepository(
            new ManifestYamlParser(),
            "https://github.com/mbouchenoire/lowfer.git",
            "0.1.0",
            "src/test/resources/architectures/demo",
            "",
            "");

        final List<SoftwareArchitecture> architectures = repository.findAll();
        assertFalse(architectures.isEmpty());
    }
}
