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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.lowfer.domain.SoftwareComponentMother.external;
import static org.lowfer.domain.SoftwareComponentMother.single;
import static org.lowfer.domain.common.SoftwareComponentType.SERVICE;

class InternalComponentFilterTest {

    @Test
    void testHideExternalComponent() {
        final SoftwareComponent internal = single();
        final SoftwareComponent external = external("external", SERVICE);

        final SoftwareArchitecture architecture =
            new SoftwareArchitecture("architecture", Set.of(internal, external));

        assertFalse(new InternalComponentFilter(false).test(architecture, external));
        assertFalse(new InternalComponentFilter(true).test(architecture, external));
    }
}
