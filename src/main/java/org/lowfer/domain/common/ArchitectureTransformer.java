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

package org.lowfer.domain.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.lowfer.domain.common.SoftwareComponent.agg;

@Component
public class ArchitectureTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(ArchitectureTransformer.class);

    public SoftwareArchitecture filter(SoftwareArchitecture architecture, SoftwareComponentFilter filter) {
        if (filter == null)
            return architecture;

        LOG.trace("Transforming architecture with {} component(s)...", architecture.getComponents().size());

        final Set<SoftwareComponent> mainComponents = architecture.getComponents().stream()
            .filter(component -> filter.test(architecture, component))
            .collect(toSet());

        final Map<Set<SoftwareComponent>, SoftwareComponent> aggregates = mainComponents.stream()
            .flatMap(component -> {
                final Set<SoftwareComponent> notMainDependencies = component.getDependencies().stream()
                    .flatMap(d -> architecture.findComponent(d).stream())
                    .filter(c -> !mainComponents.contains(c))
                    .collect(toSet());

                final Set<SoftwareComponent> notMainClients = architecture.findClients(component).stream()
                    .filter(c -> !mainComponents.contains(c))
                    .collect(toSet());

                return Stream.of(notMainDependencies, notMainClients);
            })
            .filter(components -> !components.isEmpty())
            .flatMap(components -> contextualizeComponents(components).stream())
            .distinct()
            .collect(toMap(
                ContextualizedComponents::getComponents,
                components -> agg(components.getComponents(), components.getContext().orElse(null))));

        // aggregates are ok

        final Set<SoftwareComponent> simplifiedComponents = mainComponents.stream()
            .flatMap(mainComponent -> {
                final Set<SoftwareComponent> notMainDependencies = mainComponent.getDependencies().stream()
                    .flatMap(d -> architecture.findComponent(d).stream())
                    .filter(c -> !mainComponents.contains(c))
                    .collect(toSet());

                final Set<ContextualizedComponents> contextualizedDependencies = contextualizeComponents(notMainDependencies);

                final Set<SoftwareComponent> dependenciesAggregates = contextualizedDependencies.stream()
                    .flatMap(d -> Optional.ofNullable(aggregates.get(d.getComponents())).stream())
                    .collect(toSet());

                final Set<SoftwareComponent> notMainClients = architecture.findClients(mainComponent).stream()
                    .filter(c -> !mainComponents.contains(c))
                    .collect(toSet());

                final Set<ContextualizedComponents> contextualizedClients = contextualizeComponents(notMainClients);

                final Set<SoftwareComponent> clientsAggregates = contextualizedClients.stream()
                    .flatMap(c -> Optional.ofNullable(aggregates.get(c.getComponents())).stream())
                    .collect(toSet());

                final SoftwareComponent updatedMainComponent = mainComponent
                    .removeDependencies(notMainDependencies)
                    .addDependencies(dependenciesAggregates.stream().map(ComponentDependency::new).collect(toSet()));

                return Stream.concat(
                    Stream.concat(clientsAggregates.stream()
                            .map(clientAggregate -> clientAggregate.addDependency(new ComponentDependency(mainComponent))),
                        dependenciesAggregates.stream()),
                    Stream.of(updatedMainComponent));
            })
            .collect(toSet());

        final SoftwareArchitecture transformedArchitecture =
            new SoftwareArchitecture(architecture.getName(), simplifiedComponents);

        LOG.debug("Transformed architecture of {} component(s) into architecture of {} component(s)", architecture.getComponents().size(), transformedArchitecture.getComponents().size());

        return transformedArchitecture;
    }

    private static Set<ContextualizedComponents> contextualizeComponents(Set<SoftwareComponent> components) {
        return components.stream()
            .map(SoftwareComponent::getContext)
            .distinct()
            .map(context -> {
                final Set<SoftwareComponent> clients = components.stream()
                    .filter(c -> c.getContext().equals(context))
                    .collect(toSet());

                return new ContextualizedComponents(clients, context.orElse(null));
            })
            .collect(toSet());
    }
}
