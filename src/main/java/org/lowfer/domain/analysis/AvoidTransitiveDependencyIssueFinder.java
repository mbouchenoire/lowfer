package org.lowfer.domain.analysis;

import org.lowfer.domain.common.ComponentDependency;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class AvoidTransitiveDependencyIssueFinder implements IssueFinder {

    private static final Logger LOG = LoggerFactory.getLogger(AvoidTransitiveDependencyIssueFinder.class);

    @Override
    public List<Issue> find(SoftwareArchitecture architecture) {
        LOG.trace("Finding transitive dependencies issues (architecture: {})...", architecture.getName());

        return architecture.getComponents().stream()
            .map(component -> {
                final List<DependencyChain> dependencyChainList =
                    findDependencyChains(architecture, new DependencyChain(component));

                return new DependencyChainSet(dependencyChainList);
            })
            .flatMap(dependencyChains -> dependencyChains.findTransitiveDependencies().stream())
            .sorted(Comparator.comparing(TransitiveDependency::getSummary))
            .collect(toUnmodifiableList());
    }

    private static List<DependencyChain> findDependencyChains(
        SoftwareArchitecture architecture, DependencyChain dependencyChain) {

        LOG.trace("Finding dependency with current chain: {} (architecture: {})", dependencyChain, architecture.getName());

        final Set<ComponentDependency> dependencies = dependencyChain.tail().getDependencies();

        if (dependencies.isEmpty()) {
            return Collections.singletonList(dependencyChain);
        }

        if (dependencyChain.isCircularDependency()) {
            LOG.debug("Found circular dependency ({}) while searching for dependency chains (architecture: {})", dependencyChain, architecture.getName());
            return Collections.singletonList(dependencyChain);
        }

        return dependencies.stream()
                .flatMap(dependency -> architecture.findComponent(dependency).stream())
                .flatMap(dependency -> findDependencyChains(architecture, dependencyChain.add(dependency)).stream())
                .collect(toUnmodifiableList());
    }
}
