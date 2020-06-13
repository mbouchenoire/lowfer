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

import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.serde.ManifestYamlParser;

import static org.junit.jupiter.api.Assertions.*;

class ArchitectureDirectoryRepositoryTest {

    @Test
    public void testFindArchitectureByName() {
        final ArchitectureDirectoryRepository repository = new ArchitectureDirectoryRepository(
            "src/test/resources/architectures/sample", new ManifestYamlParser());

        final SoftwareArchitecture sample = repository.findByName("sample").orElseThrow();

        assertEquals("sample", sample.getName());
        assertEquals(2, sample.getComponents().size());
    }
}
