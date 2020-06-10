package org.lowfer.domain.analysis;

import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

import java.util.List;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lowfer.domain.SoftwareComponentMother.external;
import static org.lowfer.domain.SoftwareComponentMother.single;
import static org.lowfer.domain.common.SoftwareComponentType.FRONTEND;
import static org.lowfer.domain.common.SoftwareComponentType.LIBRARY;

class ApplyRuleOfThreeIssueFinderTest {

    @Test
    void testNoIssue() {
        final SoftwareComponent c1 = single("c1", of("l1"), FRONTEND);
        final SoftwareComponent c2 = single("c2", of("l1"), FRONTEND);
        final SoftwareComponent c3 = single("c3", of("l1"), FRONTEND);
        final SoftwareComponent l1 = single("l1", LIBRARY);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(l1, c1, c2, c3));
        final List<Issue> issues = new ApplyRuleOfThreeIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == Rule.APPLY_RULE_OF_THREE));
    }

    @Test
    void testNoIssueBecauseExternal() {
        final SoftwareComponent c1 = single("c1", of("l1"), FRONTEND);
        final SoftwareComponent c2 = single("c2", of("l1"), FRONTEND);
        final SoftwareComponent l1 = external("l1", LIBRARY);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(l1, c1, c2));
        final List<Issue> issues = new ApplyRuleOfThreeIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == Rule.APPLY_RULE_OF_THREE));
    }

    @Test
    void testBasicIssue() {
        final SoftwareComponent c1 = single("c1", of("l1"), FRONTEND);
        final SoftwareComponent c2 = single("c2", of("l1"), FRONTEND);
        final SoftwareComponent l1 = single("l1", LIBRARY);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(l1, c1, c2));
        final List<Issue> issues = new ApplyRuleOfThreeIssueFinder().find(architecture);
        assertEquals(1, issues.stream().filter(issue -> issue.getRule() == Rule.APPLY_RULE_OF_THREE).count());
    }
}
