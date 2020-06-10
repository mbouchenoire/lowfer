package org.lowfer.web.rest.vm;

import java.util.List;

public final class MaintainerListView {

    private final List<MaintainerListItemView> maintainers;

    public MaintainerListView(List<MaintainerListItemView> maintainers) {
        this.maintainers = maintainers;
    }

    public List<MaintainerListItemView> getMaintainers() {
        return maintainers;
    }
}
