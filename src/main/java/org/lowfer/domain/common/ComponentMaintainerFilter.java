package org.lowfer.domain.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public final class ComponentMaintainerFilter implements SoftwareComponentFilter {

    private final Set<String> maintainerNames;

    private ComponentMaintainerFilter(Set<String> maintainerNames) {
        this.maintainerNames = Collections.unmodifiableSet(maintainerNames);
    }

    public String getText() {
        return String.join(",", maintainerNames);
    }

    public static ComponentMaintainerFilter ofText(String text) {
        final Set<String> names = Arrays.stream(text.split(","))
                .filter(s -> !"".equals(s))
                .collect(Collectors.toSet());

        return new ComponentMaintainerFilter(names);
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        if (maintainerNames.isEmpty())
            return true;

        return maintainerNames.stream()
                .anyMatch(maintainerName -> component.getMaintainers().stream()
                        .anyMatch(maintainer -> maintainer.getName().equals(maintainerName)));
    }
}
