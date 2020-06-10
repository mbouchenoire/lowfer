package org.lowfer.web.rest.vm;

import org.lowfer.domain.common.SoftwareArchitecture;

public final class ArchitectureListItemView {

    private final String name;

    public ArchitectureListItemView(SoftwareArchitecture architecture) {
        this.name = architecture.getName();
    }

    public String getName() {
        return name;
    }
}
