package org.lowfer.web.rest.vm;

import org.lowfer.domain.common.Maintainer;

public final class MaintainerListItemView {

    private final String name;

    public MaintainerListItemView(Maintainer maintainer) {
        this.name = maintainer.getName();
    }

    public String getName() {
        return name;
    }
}
