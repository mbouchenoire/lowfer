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

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.lowfer.domain.common.SoftwareComponentType.QUEUE;

public final class QueueWithMultiplePublishers implements Issue {

    private final SoftwareComponent queue;
    private final List<SoftwareComponent> clients;

    public QueueWithMultiplePublishers(SoftwareComponent queue, List<SoftwareComponent> publishers) {
        if (queue.getType() != QUEUE)
            throw new IllegalArgumentException("component is not a queue");

        this.queue = queue;
        this.clients = publishers;
    }

    @Override
    public Rule getRule() {
        return Rule.AVOID_QUEUE_MULTIPLE_PUBLISHERS;
    }

    @Override
    public String getSummary() {
        return String.format("Queue '%s' has multiple publishers", queue.getLabel());
    }

    @Override
    public String getDescription() {
        return String.format(
            "Queue '%s' has multiple publishers:\n (%s)",
            queue.getLabel(),
            clients.stream()
                .map(SoftwareComponent::getName)
                .collect(joining("\n-")));
    }

    @Override
    public IssueType getType() {
        return IssueType.MAINTENANCE;
    }

    @Override
    public Severity getSeverity() {
        return Severity.MAJOR;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return component.equals(queue) || clients.contains(component);
    }
}
