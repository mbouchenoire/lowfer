package org.lowfer.domain.common;

import java.util.Objects;

public final class Maintainer {

    private final String name;
    private final SemanticUIColor color;

    public Maintainer(String name, SemanticUIColor color) {
        this.name = name;
        this.color = color;
    }

    public static Maintainer external(SemanticUIColor color) {
        return new Maintainer("external", color);
    }

    public String getName() {
        return name;
    }

    public SemanticUIColor getColor() {
        return color;
    }

    public boolean isExternal() {
        return name.equals("external");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maintainer that = (Maintainer) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
