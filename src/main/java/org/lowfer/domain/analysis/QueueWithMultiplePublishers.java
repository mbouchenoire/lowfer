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
