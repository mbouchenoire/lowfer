package org.lowfer.domain.common;

import java.util.Collections;
import java.util.Set;

public final class ComponentNeighborsFilter implements SoftwareComponentFilter {

    private final Set<String> componentNames;

    private ComponentNeighborsFilter(Set<String> componentNames) {
        this.componentNames = Collections.unmodifiableSet(componentNames);
    }

    public String getText() {
        return String.join(",", componentNames);
    }

    public static ComponentNeighborsFilter ofText(String text) {
        final Set<String> names = Set.of(text.split(","));
        return new ComponentNeighborsFilter(names);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        if (componentNames.isEmpty())
            return true;

        if (componentNames.contains(component.getName()))
            return true;

        final boolean isFanIn = component.getDependencies().stream()
                .anyMatch(dependency -> componentNames.contains(dependency.getComponentName()));

        if (isFanIn)
            return true;

        final boolean isFanOut = architecture.getComponents().stream()
                .filter(c -> componentNames.contains(c.getName()))
                .anyMatch(c -> c.getDependencies().stream().anyMatch(dependency -> dependency.getComponentName().equals(component.getName())));

        if (isFanOut)
            return true;

        return false;
    }
}
