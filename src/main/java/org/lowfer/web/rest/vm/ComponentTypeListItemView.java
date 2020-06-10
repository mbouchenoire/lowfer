package org.lowfer.web.rest.vm;

import org.lowfer.domain.common.SoftwareComponentType;

public final class ComponentTypeListItemView {

    private final String name;
    private final String label;

    public ComponentTypeListItemView(SoftwareComponentType componentType) {
        this.name = componentType.getName();
        this.label = componentType.getPluralLabel();
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }
}
