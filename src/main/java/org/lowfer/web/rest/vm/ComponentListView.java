package org.lowfer.web.rest.vm;

import java.util.List;

public final class ComponentListView {

    private final List<ComponentListItemView> components;

    public ComponentListView(List<ComponentListItemView> components) {
        this.components = components;
    }

    public List<ComponentListItemView> getComponents() {
        return components;
    }
}
