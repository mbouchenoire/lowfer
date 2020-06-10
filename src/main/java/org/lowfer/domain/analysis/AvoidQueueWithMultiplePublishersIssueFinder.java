package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.lowfer.domain.analysis.IssueFinderUtils.findNonLibraryClients;
import static org.lowfer.domain.common.DependencyType.QUEUE_PUBLISH;
import static org.lowfer.domain.common.SoftwareComponentType.QUEUE;

public final class AvoidQueueWithMultiplePublishersIssueFinder implements IssueFinder {

    private static final Logger LOG = LoggerFactory.getLogger(AvoidQueueWithMultiplePublishersIssueFinder.class);

    @Override
    public List<Issue> find(SoftwareArchitecture architecture) {
        LOG.trace("Finding queues with multiple publishers (architecture: {})...", architecture.getName());

        return architecture.getComponents().stream()
                .filter(component -> component.getType() == QUEUE)
                .flatMap(queue -> {
                    final List<SoftwareComponent> queuePublishers = findNonLibraryClients(
                        queue, dependency -> dependency.getDependencyType().equals(of(QUEUE_PUBLISH)), architecture);

                    if (queuePublishers.size() <= 1) {
                        return Stream.empty();
                    } else {
                        LOG.debug("Found queue with {} publishers: {} (architecture: {})", queuePublishers.size(), queue.getName(), architecture.getName());
                        return Stream.of(new QueueWithMultiplePublishers(queue, queuePublishers));
                    }
                })
                .collect(toUnmodifiableList());
    }
}
