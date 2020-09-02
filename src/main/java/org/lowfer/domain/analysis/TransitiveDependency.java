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

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class TransitiveDependency implements Issue {

    private final List<DependencyChain> dependencyChains;

    public TransitiveDependency(List<DependencyChain> dependencyChains) {
        this.dependencyChains = dependencyChains.stream()
            .distinct()
            .sorted(Comparator.comparingInt(o -> o.getComponents().size()))
            .collect(Collectors.toList());
    }

    public List<DependencyChain> getDependencyChains() {
        return dependencyChains;
    }

    @Override
    public String getSummary() {
        final DependencyChain anyChain = dependencyChains.get(0);
        return String.format("Multiple dependency chains from '%s' to '%s'",
            anyChain.head().getLabel(),
            anyChain.tail().getLabel());
    }

    @Override
    public String getDescription() {
        return getSummary();
    }

    @Override
    public Rule getRule() {
        return Rule.AVOID_TRANSITIVE_DEPENDENCIES;
    }

    @Override
    public IssueType getType() {
        return IssueType.MAINTENANCE;
    }

    @Override
    public Severity getSeverity() {
        return Severity.MINOR;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return dependencyChains.stream()
            .anyMatch(dependencyChain -> dependencyChain.getComponents().contains(component));
    }

    public boolean isRedundantWith(TransitiveDependency other) {
        return this.getSignature().equals(other.getSignature());
    }

    private String getSignature() {
        return dependencyChains.stream()
            .map(DependencyChain::getSignature)
            .sorted()
            .collect(Collectors.joining(","));
    }

    public int getMinChainSize() {
        return dependencyChains.stream()
            .mapToInt(value -> value.getComponents().size())
            .min()
            .orElse(-1);
    }

    public String getMinChainSignature() {
        return dependencyChains.stream()
            .min(Comparator.comparingInt(o -> o.getComponents().size()))
            .map(DependencyChain::getSignature)
            .orElseThrow(() -> new IllegalStateException("Failed to get minimal chain signature for:" + this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransitiveDependency that = (TransitiveDependency) o;
        return Objects.equals(hashCode(), that.hashCode());
    }

    @Override
    public int hashCode() {
        return getMinChainSignature().hashCode();
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
