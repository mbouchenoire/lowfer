package org.lowfer.serde;

import org.lowfer.domain.common.Maintainer;

public class MaintainerYaml {

    private String name;

    MaintainerYaml() {
        this.name = null;
    }

    public MaintainerYaml(Maintainer maintainer) {
        this.name = maintainer.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
