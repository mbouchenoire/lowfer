package org.lowfer.domain.analysis;

import org.lowfer.domain.common.ComponentDependency;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.lowfer.domain.common.SoftwareComponentType.LIBRARY;

public final class IssueFinderUtils {

    private static final Logger LOG = LoggerFactory.getLogger(IssueFinderUtils.class);

    private IssueFinderUtils() {
    }

    public static List<SoftwareComponent> findNonLibraryClients(
        SoftwareComponent component,
        Predicate<ComponentDependency> dependencyPredicate,
        SoftwareArchitecture architecture) {

        LOG.trace("Finding non-library clients for component: {} (architecture: {})...", component.getName(), architecture.getName());

        return architecture.findClients(component, dependencyPredicate).stream()
            .flatMap(componentClient -> {
                if (componentClient.getType() == LIBRARY) {
                    LOG.trace("Client '{}' of component '{}' is a library, ignoring it (architecture: {})", componentClient.getName(), component.getName(), architecture.getName());
                    return findNonLibraryClients(componentClient, dependencyPredicate, architecture).stream();
                } else {
                    return Stream.of(componentClient);
                }
            })
            .collect(toUnmodifiableList());
    }
}
