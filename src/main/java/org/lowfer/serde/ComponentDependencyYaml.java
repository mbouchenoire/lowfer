package org.lowfer.serde;

import org.lowfer.domain.common.ComponentDependency;
import org.lowfer.domain.common.DependencyType;

import java.util.Optional;

public class ComponentDependencyYaml {

    private String component;
    private String type;

    ComponentDependencyYaml() {
        this.component = null;
        this.type = null;
    }

    public ComponentDependencyYaml(ComponentDependency dependency) {
        this.component = dependency.getComponentName();
        this.type = dependency.getDependencyType().map(DependencyType::getSerializedName).orElse(null);
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return component + Optional.ofNullable(type).map(type -> " (" + type + ")").orElse("");
    }
}
