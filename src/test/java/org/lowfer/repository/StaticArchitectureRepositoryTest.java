package org.lowfer.repository;

import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.serde.ManifestYamlParser;

import static org.junit.jupiter.api.Assertions.*;

class StaticArchitectureRepositoryTest {

    @Test
    public void testFindArchitectureByName() {
        final StaticArchitectureRepository repository = new StaticArchitectureRepository(
            "src/test/resources/architectures/sample", new ManifestYamlParser());

        final SoftwareArchitecture sample = repository.findByName("sample").orElseThrow();

        assertEquals("sample", sample.getName());
        assertEquals(2, sample.getComponents().size());
    }
}
