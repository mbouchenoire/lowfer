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

import org.lowfer.domain.common.SoftwareComponent;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.*;

public final class DependencyChain {

    private final List<SoftwareComponent> components;

    public DependencyChain() {
        this.components = emptyList();
    }

    public DependencyChain(SoftwareComponent component) {
        this.components = singletonList(component);
    }

    private DependencyChain(List<SoftwareComponent> components) {
        this.components = Collections.unmodifiableList(components);
    }

    public List<SoftwareComponent> getComponents() {
        return components;
    }

    public SoftwareComponent head() {
        return components.get(0);
    }

    public SoftwareComponent tail() {
        return components.get(components.size() - 1);
    }

    boolean isCircularDependency() {
        return components.stream().anyMatch(component -> frequency(components, component) > 1);
    }

    public Optional<CircularDependency> getCircularDependency() {
        return isCircularDependency()
                ? Optional.of(new CircularDependency(this))
                : Optional.empty();
    }

    public DependencyChain add(SoftwareComponent component) {
        final List<SoftwareComponent> updatedChain = new ArrayList<>(components);
        updatedChain.add(component);
        return new DependencyChain(updatedChain);
    }

    public DependencyChain addFirst(SoftwareComponent component) {
        final List<SoftwareComponent> updatedChain = new ArrayList<>(components);
        updatedChain.add(0, component);
        return new DependencyChain(updatedChain);
    }

    public Optional<DependencyChain> subChain(SoftwareComponent startInclusive, SoftwareComponent endInclusive) {
        final int indexOfStart = components.indexOf(startInclusive);

        if (indexOfStart < 0)
            return Optional.empty();

        final List<SoftwareComponent> builtChain = new LinkedList<>();

        for (int componentIndex = indexOfStart; componentIndex < components.size(); componentIndex++) {
            final SoftwareComponent component = components.get(componentIndex);

            builtChain.add(component);

            if (component.equals(endInclusive)) {
                break;
            }
        }

        if (builtChain.isEmpty())
            return Optional.empty();

        final SoftwareComponent last = builtChain.get(builtChain.size() - 1);

        return last.equals(endInclusive)
            ? Optional.of(new DependencyChain(builtChain))
            : Optional.empty();
    }

    public String getSignature() {
        return components.stream()
            .sorted(Comparator.comparing(SoftwareComponent::getName))
            .map(SoftwareComponent::getName)
            .collect(Collectors.joining(","));
    }

    @Override
    public String toString() {
        return components.stream().map(SoftwareComponent::getName).collect(Collectors.joining(" -> "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DependencyChain that = (DependencyChain) o;
        return Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }
}
