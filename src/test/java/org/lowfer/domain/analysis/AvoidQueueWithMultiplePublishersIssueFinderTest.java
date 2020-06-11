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
