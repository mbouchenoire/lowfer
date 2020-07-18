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

package org.lowfer.serde;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.repository.ComponentGitRepositoryFactory;
import org.lowfer.repository.GitHostConfigRepository;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ManifestYamlParserTest {

    static Try<SoftwareArchitecture> deserialize(String errorFileName) {
        return new ManifestYamlParser(new ComponentGitRepositoryFactory(GitHostConfigRepository.defaultConfig()))
            .deserializeManifest(new File("src/test/resources/architectures/errors/" + errorFileName));
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
