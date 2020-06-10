package org.lowfer.domain.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public final class ComponentTypeFilter implements SoftwareComponentFilter {

    private final Set<SoftwareComponentType> componentTypes;

    private ComponentTypeFilter(Set<SoftwareComponentType> componentTypes) {
        this.componentTypes = Collections.unmodifiableSet(componentTypes);
    }

    public String getText() {
        return componentTypes.stream()
                .map(SoftwareComponentType::getName)
                .collect(Collectors.joining(","));
    }

    public static ComponentTypeFilter ofText(String text) {
        final Set<SoftwareComponentType> types = Arrays.stream(text.split(","))
                .flatMap(s -> SoftwareComponentType.fromSerializedName(s).stream())
                .collect(toSet());

        return new ComponentTypeFilter(types);
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        if (componentTypes.isEmpty())
            return true;

        return componentTypes.contains(component.getType());
    }
}
