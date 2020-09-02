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

import org.lowfer.domain.common.ComponentDependency;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

public final class StableDependencyPrincipleViolation implements Issue {

    private final SoftwareArchitecture architecture;
    private final SoftwareComponent component;
    private final ComponentDependency dependency;

    public StableDependencyPrincipleViolation(
            SoftwareArchitecture architecture, SoftwareComponent component, ComponentDependency dependency) {

        this.architecture = architecture;
        this.component = component;
        this.dependency = dependency;
    }

    @Override
    public Rule getRule() {
        return Rule.APPLY_STABLE_DEPENDENCY_PRINCIPLE;
    }

    @Override
    public String getSummary() {
        return String.format("Component '%s' depends on a less stable component", component.getLabel());
    }

    @Override
    public String getDescription() {
        return String.format("Dependency '%s -> %s' does not apply the stable dependency principle (%s -> %s)",
            component.getLabel(),
            dependency.getComponentName(),
            architecture.instability(component),
            architecture.instability(dependency).orElseThrow(() -> new IllegalStateException("Failed to find instability for dependency: " + dependency)));
    }

    @Override
    public IssueType getType() {
        return IssueType.MAINTENANCE;
    }

    @Override
    public Severity getSeverity() {
        return Severity.MAJOR;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return component.equals(this.component) || component.getName().equals(dependency.getComponentName());
    }
}
