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

import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public final class CircularDependency implements Issue {

    private final DependencyChain dependencyChain;

    public CircularDependency(DependencyChain dependencyChain) {
        if (!dependencyChain.isCircularDependency())
            throw new IllegalArgumentException("Dependency chain is not circular");

        this.dependencyChain = dependencyChain;
    }

    @Override
    public Rule getRule() {
        return Rule.APPLY_ACYCLIC_DEPENDENCIES_PRINCIPLE;
    }

    @Override
    public String getSummary() {
        return "Circular dependency: " + dependencyChain.getComponents().stream()
            .map(SoftwareComponent::getLabel)
            .collect(Collectors.joining(" -> "));
    }

    @Override
    public String getDescription() {
        return getSummary();
    }

    @Override
    public IssueType getType() {
        return IssueType.MAINTENANCE;
    }

    @Override
    public Severity getSeverity() {
        return Severity.CRITICAL;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return dependencyChain.getComponents().contains(component);
    }

    /**
     * @return true if this circular dependency cycles with the same components
     *          as the provided circular dependency.
     */
    public boolean isRedundantWith(CircularDependency other) {
        return this.signature().equals(other.signature());
    }

    private String signature() {
        return dependencyChain.getComponents().stream() // c9 -> c2 -> c3 -> c1 -> c2
                .skip(dependencyChain.getComponents().indexOf(dependencyChain.tail())) // c2 -> c3 -> c1 -> c2
                .map(SoftwareComponent::getName)
                .sorted() // c1 -> c1 -> c2 -> c3
                .distinct() // c1 -> c2 -> c3
                .collect(joining(",")); // c1c2c3
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
