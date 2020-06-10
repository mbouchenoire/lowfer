package org.lowfer.domain.analysis;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.ComponentDependency;
import org.lowfer.domain.common.DependencyType;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lowfer.domain.SoftwareComponentMother.single;
import static org.lowfer.domain.common.DependencyType.*;
import static org.lowfer.domain.common.SoftwareComponentType.*;

class AvoidQueueWithMultiplePublishersIssueFinderTest {

    @Test
    void testNoQueueWithMultiplePublishers() {
        final SoftwareComponent service1 = single("service1", Set.of("queue1"), SERVICE);
        final SoftwareComponent queue1 = single("queue1", QUEUE);

        final SoftwareComponent queue2 = single("queue2", QUEUE);
        final SoftwareComponent queue3 = single("queue3", QUEUE);

        final SoftwareComponent service2 = single("service2", SERVICE, Set.of(
            new ComponentDependency("queue2", QUEUE_PUBLISH),
            new ComponentDependency("queue3", QUEUE_SUBSCRIBE)));


        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", Set.of(service1, queue1, service2, queue2, queue3));
        final List<Issue> issues = new AvoidQueueWithMultiplePublishersIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == Rule.AVOID_QUEUE_MULTIPLE_PUBLISHERS));
    }

    @Test
    void testSingleQueueWithMultiplePublishers() {
        final SoftwareComponent service1 = single(
            "service1", SERVICE, Set.of(new ComponentDependency("queue1", QUEUE_PUBLISH)));

        final SoftwareComponent service2 = single(
            "service2", SERVICE, Set.of(new ComponentDependency("queue1", QUEUE_PUBLISH)));

        final SoftwareComponent queue1 = single("queue1", QUEUE);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ko", Set.of(service1, service2, queue1));
        final List<Issue> issues = new AvoidQueueWithMultiplePublishersIssueFinder().find(architecture);
        assertEquals(1, issues.stream().filter(issue -> issue.getRule() == Rule.AVOID_QUEUE_MULTIPLE_PUBLISHERS).count());
    }

    @Disabled("TODO")
    @Test
    void testSingleQueueWithMultiplePublishersThroughLibrary() {
        final SoftwareComponent service1 = single("service1", Set.of("library"), SERVICE);
        final SoftwareComponent service2 = single("service2", Set.of("library"), SERVICE);
        final SoftwareComponent library = single("library", LIBRARY, Set.of(new ComponentDependency("queue1", QUEUE_PUBLISH)));
        final SoftwareComponent queue1 = single("queue1", QUEUE);
        final SoftwareArchitecture architecture = new SoftwareArchitecture("ko", Set.of(service1, service2, library, queue1));
        final List<Issue> issues = new AvoidQueueWithMultiplePublishersIssueFinder().find(architecture);
        assertEquals(1, issues.stream().filter(issue -> issue.getRule() == Rule.AVOID_QUEUE_MULTIPLE_PUBLISHERS).count());
    }
}
