package org.lowfer.serde;

import java.util.List;

public class Master {

    private String name;
    private List<Include> include;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Include> getInclude() {
        return include;
    }

    public void setInclude(List<Include> include) {
        this.include = include;
    }
}
