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

package org.lowfer.domain.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.lowfer.domain.SoftwareArchitectureMother.*;

class ArchitectureTransformerTest {

    @Test
    void testSimplifyWithoutFilterWithGroupOf1() {
        final SoftwareArchitecture architecture = simple();

        final SoftwareArchitecture simplified =
            new ArchitectureTransformer().filter(architecture, null);

        assertEquals(architecture.getComponents().size(), simplified.getComponents().size());
    }

    @Test
    void testSimplifyFilterEdgeComponentWithGroupOf1() {
        final SoftwareArchitecture architecture = simple();

        final SoftwareArchitecture simplified =
            new ArchitectureTransformer().filter(architecture, ComponentNeighborsFilter.ofText(BDD));

        assertEquals(4, simplified.getComponents().size());
        assertEquals(2, simplified.findComponentByLabel("+1").size());
        assertTrue(simplified.findComponentByName(BDD).isPresent());
        assertTrue(simplified.findComponentByName(API_FRONT).isPresent());
    }
}
