package org.lowfer.web.rest.vm;

import java.util.List;

public final class ComponentTypeListView {

    private final List<ComponentTypeListItemView> componentTypes;

    public ComponentTypeListView(List<ComponentTypeListItemView> componentTypes) {
        this.componentTypes = componentTypes;
    }

    public List<ComponentTypeListItemView> getComponentTypes() {
        return componentTypes;
    }
}
