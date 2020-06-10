package org.lowfer.domain.analysis;

import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

import java.util.List;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lowfer.domain.SoftwareComponentMother.single;
import static org.lowfer.domain.common.SoftwareComponentType.LIBRARY;
import static org.lowfer.domain.common.SoftwareComponentType.SERVICE;

class AvoidTransitiveDependencyIssueFinderTest {

    @Test
    void testNoTransitiveDependency() {
        final SoftwareComponent c1 = single("c1", of("c2"), SERVICE);
        final SoftwareComponent c2 = single("c2", of("c4"), SERVICE);
        final SoftwareComponent c3 = single("c3", of("c4"), SERVICE);
        final SoftwareComponent c4 = single("c4", SERVICE);
        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(c1, c2, c3, c4));
        final List<Issue> issues = new AvoidTransitiveDependencyIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == Rule.AVOID_TRANSITIVE_DEPENDENCIES));
    }

    @Test
    void testNotTransitiveDependencyBecauseService() {
        final SoftwareComponent c1 = single("c1", of("c2", "c3"), SERVICE);
        final SoftwareComponent c2 = single("c2", of("c3"), SERVICE);
        final SoftwareComponent c3 = single("c3", SERVICE);
        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(c1, c2, c3));
        final List<Issue> issues = new AvoidTransitiveDependencyIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == Rule.AVOID_TRANSITIVE_DEPENDENCIES));
    }

    @Test
    void testSimpleTransitiveDependency() {
        final SoftwareComponent c1 = single("c1", of("c2", "c3"), SERVICE);
        final SoftwareComponent c2 = single("c2", of("c3"), SERVICE);
        final SoftwareComponent c3 = single("c3", LIBRARY);
        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(c1, c2, c3));
        final List<Issue> issues = new AvoidTransitiveDependencyIssueFinder().find(architecture);
        assertEquals(1, issues.stream().filter(issue -> issue.getRule() == Rule.AVOID_TRANSITIVE_DEPENDENCIES).count());
    }
}
