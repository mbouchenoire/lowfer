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

package org.lowfer.domain.analysis;

import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.SoftwareArchitecture;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lowfer.domain.SoftwareComponentMother.single;

class ApplyAcyclicDependenciesPrincipleIssueFinderTest {

    @Test
    void testFindNoCircularDependency() {
        final SoftwareArchitecture architecture = new SoftwareArchitecture("architecture", Set.of(
                single("c1", singleton("c2")),
                single("c2", singleton("c3")),
                single("c3", emptySet())));

        final List<Issue> issues = new ApplyAcyclicDependenciesPrincipleIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == Rule.APPLY_ACYCLIC_DEPENDENCIES_PRINCIPLE));
    }

    @Test
    void testFindSingleCircularDependency() {
        final SoftwareArchitecture architecture = new SoftwareArchitecture("architecture", Set.of(
                single("c1", singleton("c2")),
                single("c2", singleton("c3")),
                single("c3", singleton("c1"))));

        final List<Issue> issues = new ApplyAcyclicDependenciesPrincipleIssueFinder().find(architecture);

        assertEquals(1, issues.stream().filter(issue -> issue.getRule() == Rule.APPLY_ACYCLIC_DEPENDENCIES_PRINCIPLE).count());
    }

    @Test
    void testFindMultipleCircularDependency() {
        final SoftwareArchitecture architecture = new SoftwareArchitecture("architecture", Set.of(
                single("c1", singleton("c2")),
                single("c2", singleton("c3")),
                single("c3", singleton("c1")),
                single("c4", singleton("c5")),
                single("c5", singleton("c6")),
                single("c6", singleton("c4")),
                single("c7", singleton("c1"))));

        final List<Issue> issues = new ApplyAcyclicDependenciesPrincipleIssueFinder().find(architecture);

        assertEquals(2, issues.stream().filter(issue -> issue.getRule() == Rule.APPLY_ACYCLIC_DEPENDENCIES_PRINCIPLE).count());
    }
}
