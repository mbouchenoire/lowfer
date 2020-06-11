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

import java.util.Objects;
import java.util.Optional;

import static org.lowfer.domain.common.DependencyType.QUEUE_SUBSCRIBE;
import static org.lowfer.domain.common.DependencyType.PUBLIC_KEY;

public final class ComponentDependency {

    private final String componentName;
    private final DependencyType dependencyType;

    public ComponentDependency(SoftwareComponent component, DependencyType dependencyType) {
        this(component.getName(), dependencyType);
    }

    public ComponentDependency(SoftwareComponent component) {
        this(component.getName(), null);
    }

    public ComponentDependency(String componentName, DependencyType dependencyType) {
        this.componentName = componentName;
        this.dependencyType = dependencyType;
    }

    public ComponentDependency(String componentName) {
        this(componentName, null);
    }

    public String getComponentName() {
        return componentName;
    }

    public Optional<DependencyType> getDependencyType() {
        return Optional.ofNullable(dependencyType);
    }

    public boolean isPublicKey() {
        return getDependencyType().map(type -> type == PUBLIC_KEY).orElse(false);
    }

    public boolean isConsumer() {
        return getDependencyType().map(type -> type == QUEUE_SUBSCRIBE).orElse(false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentDependency that = (ComponentDependency) o;
        return Objects.equals(componentName, that.componentName) &&
                dependencyType == that.dependencyType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentName, dependencyType);
    }

    @Override
    public String toString() {
        return componentName + getDependencyType().map(type -> " (" + type.getSerializedName() + ")").orElse("");
    }
}
