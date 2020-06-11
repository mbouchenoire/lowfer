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
import org.lowfer.domain.common.SoftwareComponent;

import java.util.List;
import java.util.Set;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lowfer.domain.SoftwareComponentMother.external;
import static org.lowfer.domain.SoftwareComponentMother.single;
import static org.lowfer.domain.common.SoftwareComponentType.SERVICE;

class ApplyStableDependencyPrincipleIssueFinderTest {

    @Test
    void testPrincipleApplied() {
        final SoftwareComponent db = single("db");
        final SoftwareComponent api = single("api", of("db"));
        final SoftwareComponent frontend = single("frontend", of("api"));
        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(frontend, api, db));
        final List<Issue> issues = new ApplyStableDependencyPrincipleIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == Rule.APPLY_STABLE_DEPENDENCY_PRINCIPLE));
    }

    @Test
    void testPrincipleIgnoredWithExternalDependency() {
        final SoftwareComponent internal1 = single("internal1", of("internal2"), SERVICE);
        final SoftwareComponent internal2 = single("internal2", of("external1"), SERVICE);
        final SoftwareComponent external1 = external("external1", SERVICE, Set.of("external2", "external3"));
        final SoftwareComponent external2 = external("external2", SERVICE);
        final SoftwareComponent external3 = external("external3", SERVICE);
        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(internal1, internal2, external1, external2, external3));
        final List<Issue> issues = new ApplyStableDependencyPrincipleIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == Rule.APPLY_STABLE_DEPENDENCY_PRINCIPLE));
    }

    @Test
    void testPrincipleNotApplied() {
        final SoftwareComponent db = single("db");
        final SoftwareComponent api1 = single("api1", of("db", "api2", "api3", "api4", "api5"));
        final SoftwareComponent api2 = single("api2");
        final SoftwareComponent api3 = single("api3");
        final SoftwareComponent api4 = single("api4");
        final SoftwareComponent api5 = single("api5");
        final SoftwareComponent library = single("library", of("api1"));
        final SoftwareComponent frontend = single("frontend", of("library"));

        final SoftwareArchitecture architecture =
                new SoftwareArchitecture("ko", of(frontend, library, api1, api2, api3, api4, api5, db));

        final List<Issue> issues = new ApplyStableDependencyPrincipleIssueFinder().find(architecture);

        assertEquals(1, issues.stream().filter(issue -> issue.getRule() == Rule.APPLY_STABLE_DEPENDENCY_PRINCIPLE).count());
    }
}
