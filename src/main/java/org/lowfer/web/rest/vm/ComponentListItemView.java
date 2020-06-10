package org.lowfer.web.rest.vm;

import org.lowfer.domain.common.SoftwareComponent;

public final class ComponentListItemView {

    private final String name;

    public ComponentListItemView(SoftwareComponent component) {
        this.name = component.getName();
    }

    public String getName() {
        return name;
    }
}
