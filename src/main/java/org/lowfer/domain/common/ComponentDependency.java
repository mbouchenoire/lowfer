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
