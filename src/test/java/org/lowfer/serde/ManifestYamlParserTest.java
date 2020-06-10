package org.lowfer.serde;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.SoftwareArchitecture;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ManifestYamlParserTest {

    static Try<SoftwareArchitecture> deserialize(String errorFileName) {
        return new ManifestYamlParser().deserializeManifest(new File("src/test/resources/architectures/errors/" + errorFileName));
    }

    @Test
    void testInvalidYaml() {
        final Try<SoftwareArchitecture> deserialize = deserialize("invalid-yaml.yml");
        final String errorMessage = deserialize.getCause().getMessage().toLowerCase();
        assertTrue(errorMessage.contains("line 5"));
    }

    @Test
    void testMissingComponentType() {
        final Try<SoftwareArchitecture> deserialize = deserialize("missing-type.yml");
        final String errorMessage = deserialize.getCause().getMessage().toLowerCase();
        assertTrue(errorMessage.contains("component-name"));
        assertTrue(errorMessage.contains("missing"));
        assertTrue(errorMessage.contains("type"));
    }

    @Test
    void testInvalidDependencyType() {
        final Try<SoftwareArchitecture> deserialize = deserialize("invalid-dependency-type.yml");
        final String errorMessage = deserialize.getCause().getMessage().toLowerCase();
        assertTrue(errorMessage.contains("other-component-name"));
        assertTrue(errorMessage.contains("component-name"));
        assertTrue(errorMessage.contains("invalid"));
        assertTrue(errorMessage.contains("dependency"));
        assertTrue(errorMessage.contains("type"));
    }
}
