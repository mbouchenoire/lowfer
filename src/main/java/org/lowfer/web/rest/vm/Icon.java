package org.lowfer.web.rest.vm;

import org.lowfer.domain.common.SemanticUIColor;

public final class Icon {

    private final String name;
    private final SemanticUIColor color;

    public Icon(String name, SemanticUIColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public SemanticUIColor getColor() {
        return color;
    }
}
