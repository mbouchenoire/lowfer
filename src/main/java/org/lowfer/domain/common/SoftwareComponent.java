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

import org.lowfer.repository.AsyncComponentGitRepository;

import javax.annotation.Nullable;
import java.util.*;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static org.lowfer.domain.common.SoftwareComponentType.AGGREGATE;

public class SoftwareComponent {

    private final String name;
    private final String label;
    private final SoftwareComponentType type;
    private final String context;
    private final AsyncComponentGitRepository repository;
    private final Set<Maintainer> maintainers;
    private final Collection<ComponentDependency> dependencies;

    public SoftwareComponent(
        String name,
        String label,
        SoftwareComponentType type,
        @Nullable String context,
        @Nullable AsyncComponentGitRepository repository,
        Set<Maintainer> maintainers,
        Collection<ComponentDependency> dependencies) {

        this.name = name;
        this.label = label;
        this.type = type;
        this.context = context;
        this.repository = repository;
        this.maintainers = maintainers;
        this.dependencies = Collections.unmodifiableCollection(dependencies);
    }

    static SoftwareComponent agg(long aggSize, @Nullable String context) {
        return new SoftwareComponent(UUID.randomUUID().toString(), "+" + aggSize, AGGREGATE, context, null, emptySet(), emptySet());
    }

    public long weight() {
        return type == AGGREGATE
            ? Long.parseLong(label.replace("+", ""))
            : 1;
    }

    SoftwareComponent removeDependencies(Collection<SoftwareComponent> dependencies) {
        final Set<ComponentDependency> updatedDependencies = new HashSet<>(this.dependencies);
        updatedDependencies.removeIf(componentDependency -> dependencies.stream()
            .map(SoftwareComponent::getName)
            .collect(toList())
            .contains(componentDependency.getComponentName()));

        return new SoftwareComponent(name, label, type, context, repository, maintainers, updatedDependencies);
    }

    SoftwareComponent addDependency(ComponentDependency dependency) {
        return addDependencies(Set.of(dependency));
    }

    SoftwareComponent addDependencies(Set<ComponentDependency> dependencies) {
        if (dependencies.isEmpty())
            return this;

        final Set<ComponentDependency> updatedDependencies = new HashSet<>(this.dependencies);
        updatedDependencies.addAll(dependencies);
        return new SoftwareComponent(name, label, type, context, repository, maintainers, updatedDependencies);
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public SoftwareComponentType getType() {
        return type;
    }

    public Optional<String> getContext() {
        return Optional.ofNullable(context);
    }

    public Optional<AsyncComponentGitRepository> getRepository() {
        return Optional.ofNullable(repository);
    }

    public Set<Maintainer> getMaintainers() {
        return maintainers;
    }

    public boolean isExternal() {
        return maintainers.stream().anyMatch(Maintainer::isExternal);
    }

    public Set<ComponentDependency> getDependencies() {
        return Set.copyOf(dependencies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoftwareComponent that = (SoftwareComponent) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
