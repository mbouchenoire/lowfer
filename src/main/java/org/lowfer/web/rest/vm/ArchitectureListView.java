package org.lowfer.web.rest.vm;

import java.util.List;

public final class ArchitectureListView {

    private final List<ArchitectureListItemView> architectures;

    public ArchitectureListView(List<ArchitectureListItemView> architectures) {
        this.architectures = architectures;
    }

    public List<ArchitectureListItemView> getArchitectures() {
        return architectures;
    }
}
