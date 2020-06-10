package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.lowfer.domain.common.SoftwareComponentType.LIBRARY;

public final class DependencyChainSet {

    private static final Logger LOG = LoggerFactory.getLogger(DependencyChainSet.class);

    private final Set<DependencyChain> dependencyChains;

    public DependencyChainSet(Collection<DependencyChain> dependencyChains) {
        final Set<SoftwareComponent> distinctFirstComponents = dependencyChains.stream()
            .map(DependencyChain::head)
            .collect(toUnmodifiableSet());

        if (distinctFirstComponents.size() > 1)
            throw new IllegalArgumentException("Not all dependency chains start with the same component");

        this.dependencyChains = new HashSet<>(dependencyChains);
    }

    public List<TransitiveDependency> findTransitiveDependencies() {
        // bad implementation is bad :(
        LOG.trace("Finding transitive dependencies for dependency chain: {}...", this);

        final Set<TransitiveDependency> transitiveDependencies = dependencyChains.stream()
            .filter(this::isInvalidTransitiveDependency) // composition
            .flatMap(dependencyChain -> {
                final List<DependencyChain> transitiveChains = dependencyChains.stream()
                    .filter(otherChain -> !otherChain.equals(dependencyChain))
                    .flatMap(otherChain -> otherChain.subChain(dependencyChain.head(), dependencyChain.tail()).stream())
                    .collect(Collectors.toList());

                if (transitiveChains.isEmpty()) {
                    return Stream.empty();
                } else {
                    LOG.trace("Found two dependency chains with a common transitive dependency: {} & {}", dependencyChain, transitiveChains);
                    final List<DependencyChain> chainAndTransitiveChains = new ArrayList<>(transitiveChains);
                    chainAndTransitiveChains.add(dependencyChain);
                    return Stream.of(new TransitiveDependency(chainAndTransitiveChains));
                }
            })
            .filter(transitiveDependency -> transitiveDependency.getMinChainSize() == 2)
            .collect(toUnmodifiableSet());

        final List<TransitiveDependency> filteredTransitiveDependencies = new ArrayList<>();

        for (TransitiveDependency transitiveDependency : transitiveDependencies) {
            if (filteredTransitiveDependencies.stream().noneMatch(td -> td.isRedundantWith(transitiveDependency))) {
                filteredTransitiveDependencies.add(transitiveDependency);
            }
        }

        return filteredTransitiveDependencies.stream()
            .distinct()
            .sorted(Comparator.comparingInt(td -> td.getDependencyChains().size()))
            .collect(toUnmodifiableList());
    }

    private boolean isInvalidTransitiveDependency(DependencyChain dependencyChain) {
        return dependencyChain.tail().getType() == LIBRARY;
    }
}
