package org.lowfer.domain.analysis;

import org.lowfer.domain.common.ComponentInstability;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Stream.empty;
import static java.util.stream.Stream.of;

/**
 * The stable dependency principle: less stable components should depend on more stable components.
 * Depend in the direction of stability.
 */
public final class ApplyStableDependencyPrincipleIssueFinder implements IssueFinder {

    private static final Logger LOG = LoggerFactory.getLogger(ApplyStableDependencyPrincipleIssueFinder.class);

    @Override
    public List<Issue> find(SoftwareArchitecture architecture) {
        LOG.trace("Finding dependencies that violate the Stable Dependency Principle (architecture: {})...", architecture.getName());

        return architecture.getComponents().stream()
                .flatMap(component -> findViolations(architecture, new DependencyChain(component)).stream())
                .collect(toUnmodifiableList());
    }

    private static List<StableDependencyPrincipleViolation> findViolations(
            SoftwareArchitecture architecture, DependencyChain dependencyChain) {

        if (dependencyChain.getCircularDependency().isPresent()) {
            LOG.debug("Found circular dependency while searching for Stable Dependency Principle violation (architecture: {})", architecture.getName());
            return Collections.emptyList();
        }

        final SoftwareComponent currentComponent = dependencyChain.tail();
        final ComponentInstability currentInstability = architecture.instability(currentComponent);

        return currentComponent.getDependencies().stream()
                .flatMap(dependency -> {
                    final SoftwareComponent dependencyComponent = architecture.findComponent(dependency).orElseThrow();

                    if (dependencyComponent.isExternal()) {
                        LOG.trace("Component '{}' is external, Stable Dependency Principle does not apply (architecture: {})", currentComponent.getName(), architecture.getName());
                        return empty();
                    }

                    final ComponentInstability dependencyInstability = architecture.instability(dependencyComponent);

                    if (dependencyInstability.greaterThan(currentInstability)) {
                        LOG.debug("Found Stable Dependency Principle violation: {} -> {} (architecture: {})", currentComponent.getName(), dependencyComponent.getName(), architecture.getName());
                        return of(new StableDependencyPrincipleViolation(architecture, currentComponent, dependency));
                    } else {
                        return empty();
                    }
                })
                .collect(toUnmodifiableList());
    }
}
