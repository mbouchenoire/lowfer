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

import io.vavr.control.Try;
import org.lowfer.domain.error.DependencyComponentNotFound;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;
import static java.util.stream.Collectors.toUnmodifiableList;

public final class SoftwareArchitecture {

    private final String name;
    private final Set<SoftwareComponent> components;

    public SoftwareArchitecture(String name, Set<SoftwareComponent> components) {
        this.name = name;
        this.components = Collections.unmodifiableSet(components);
    }

    public static Try<SoftwareArchitecture> of(String name, Set<SoftwareComponent> components, boolean lazyLoading) {
        final Set<Try<SoftwareComponent>> componentTries = components.stream()
            .map(component -> {
                final Set<Try<ComponentDependency>> dependencyTries = component.getDependencies().stream()
                    .<Try<ComponentDependency>>map(dependency -> {
                        if (!lazyLoading && components.stream().noneMatch(c -> c.getName().equals(dependency.getComponentName()))) {
                            return failure(new DependencyComponentNotFound(component.getName(), dependency.getComponentName()));
                        }

                        return success(dependency);
                    })
                    .collect(Collectors.toSet());

                return Try.sequence(dependencyTries)
                    .map(dependencies -> component);
            })
            .collect(Collectors.toSet());

        return Try.sequence(componentTries)
            .map(validatedComponents -> new SoftwareArchitecture(name, validatedComponents.toJavaSet()));
    }

    public String getName() {
        return name;
    }

    public SoftwareArchitecture named(String name) {
        return new SoftwareArchitecture(name, components);
    }

    public SoftwareArchitecture concat(SoftwareArchitecture other) {
        final Set<SoftwareComponent> allComponents = new HashSet<>(components);
        allComponents.addAll(other.components);
        return new SoftwareArchitecture(name + "+" + other.name, allComponents);
    }

    public Set<SoftwareComponent> getComponents() {
        return components;
    }

    public List<SoftwareComponentType> getComponentTypes() {
        return components.stream()
            .map(SoftwareComponent::getType)
            .distinct()
            .sorted(Comparator.comparingInt(Enum::ordinal))
            .collect(Collectors.toList());
    }

    public Optional<SoftwareComponent> findComponent(ComponentDependency dependency) {
        return components.stream()
            .filter(softwareComponent -> softwareComponent.getName().equals(dependency.getComponentName()))
            .findAny();
    }

    public Optional<SoftwareComponent> findComponentByName(String componentName) {
        return components.stream()
            .filter(component -> component.getName().equals(componentName))
            .findAny();
    }

    public List<SoftwareComponent> findComponentByLabel(String componentLabel) {
        return components.stream()
            .filter(component -> component.getLabel().equals(componentLabel))
            .collect(Collectors.toList());
    }

    public List<SoftwareComponent> findClients(
        SoftwareComponent component, Predicate<ComponentDependency> dependencyPredicate) {

        return components.stream()
            .filter(c -> c.getDependencies().stream()
                .filter(dependencyPredicate)
                .anyMatch(d -> d.getComponentName().equals(component.getName())))
            .collect(toUnmodifiableList());
    }

    public List<SoftwareComponent> findClients(SoftwareComponent component) {
        return findClients(component, dependency -> true);
    }

    public Optional<ComponentInstability> instability(ComponentDependency dependency) {
        return findComponent(dependency).map(this::instability);
    }

    public ComponentInstability instability(SoftwareComponent component) {
        final long fanIn = components.stream()
            .filter(c -> c.getDependencies().stream().anyMatch(dependency -> dependency.getComponentName().equals(component.getName())))
            .mapToLong(SoftwareComponent::weight)
            .sum();

        final long fanOut = component.getDependencies().stream()
            .mapToLong(dependency -> findComponent(dependency)
                .map(SoftwareComponent::weight)
                .orElse(0L))
            .sum();

        return new ComponentInstability(component, fanIn, fanOut);
    }


    public List<Maintainer> getMaintainers() {
        return components.stream()
            .flatMap(softwareComponent -> softwareComponent.getMaintainers().stream())
            .distinct()
            .sorted(Comparator.comparing(Maintainer::getName))
            .collect(toUnmodifiableList());
    }

    @Override
    public String toString() {
        return name;
    }
}
