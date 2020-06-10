package org.lowfer.web.rest.vm;

import org.lowfer.domain.common.SoftwareComponent;
import org.lowfer.domain.common.SoftwareComponentType;

import java.util.List;

public final class ComponentTypeGroup {

    private final SoftwareComponentType type;
    private final List<SoftwareComponent> components;

    public ComponentTypeGroup(SoftwareComponentType type, List<SoftwareComponent> components) {
        this.type = type;
        this.components = components;
    }

    public SoftwareComponentType getType() {
        return type;
    }

    public List<SoftwareComponent> getComponents() {
        return components;
    }
}
