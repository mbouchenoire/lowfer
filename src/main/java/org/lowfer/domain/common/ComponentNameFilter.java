package org.lowfer.domain.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public final class ComponentNameFilter implements SoftwareComponentFilter {

    private final Set<String> componentNames;

    private ComponentNameFilter(Set<String> componentNames) {
        this.componentNames = Collections.unmodifiableSet(componentNames);
    }

    public String getText() {
        return String.join(",", componentNames);
    }

    public static ComponentNameFilter ofText(String text) {
        final Set<String> names = Arrays.stream(text.split(","))
                .filter(s -> !"".equals(s))
                .collect(Collectors.toSet());

        return new ComponentNameFilter(names);
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        if (componentNames.isEmpty())
            return true;

        return componentNames.contains(component.getName());
    }
}
