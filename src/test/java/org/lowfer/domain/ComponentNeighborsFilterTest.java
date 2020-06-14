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

package org.lowfer.domain;

import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.ComponentDependency;
import org.lowfer.domain.common.ComponentNeighborsFilter;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lowfer.domain.common.DependencyType.HTTP;
import static org.lowfer.domain.common.SoftwareComponentType.*;

class ComponentNeighborsFilterTest {

    @Test
    void test() {
        final SoftwareComponent bdd1 = new SoftwareComponent(
                "bdd1", "bdd1", DATABASE, null, null, emptySet(), emptySet());

        final SoftwareComponent back1 = new SoftwareComponent(
                "back1", "back1", SERVICE, null, null, emptySet(), Set.of(new ComponentDependency(bdd1)));

        final SoftwareComponent back2 = new SoftwareComponent(
                "back2", "back2", SERVICE, null, null, emptySet(), Set.of(new ComponentDependency(back1, HTTP)));

        final SoftwareComponent back3 = new SoftwareComponent(
                "back3", "back3", SERVICE, null, null, emptySet(), emptySet());

        final SoftwareComponent front1 = new SoftwareComponent(
                "front1", "front1", FRONTEND, null, null, emptySet(), Set.of(new ComponentDependency(back2), new ComponentDependency(back3)));

        final SoftwareArchitecture architecture = new SoftwareArchitecture(UUID.randomUUID().toString(), Set.of(bdd1, back1, back2, back3, front1));

        final ComponentNeighborsFilter filterBack2 = ComponentNeighborsFilter.ofText("back2");

        assertFalse(filterBack2.test(architecture, bdd1)); // its a dependency of back1, distance=2
        assertTrue(filterBack2.test(architecture, back1)); // dependency of back2
        assertTrue(filterBack2.test(architecture, back2)); // obviously
        assertFalse(filterBack2.test(architecture, back3)); // it's a dependency of front1, distance=2
        assertTrue(filterBack2.test(architecture, front1)); // depends on back2
    }
}
