package org.lowfer.repository;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.serde.ManifestYamlParser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertFalse;

class ArchitectureGitRepositoryTestIT {

    @Test
    public void testInitializeArchitectureGitRepository() throws IOException, GitAPIException, URISyntaxException {
        final ComponentGitRepositoryFactory componentGitRepositoryFactory =
            new ComponentGitRepositoryFactory(GitHostConfigRepository.defaultConfig());

        final ArchitectureGitRepository repository = new ArchitectureGitRepository(
            new ManifestYamlParser(componentGitRepositoryFactory),
            GitHostConfigRepository.defaultConfig(),
            "https://github.com/mbouchenoire/lowfer.git",
            "0.1.0",
            "src/test/resources/architectures/demo");

        final List<SoftwareArchitecture> architectures = repository.findAll();
        assertFalse(architectures.isEmpty());
    }
}
