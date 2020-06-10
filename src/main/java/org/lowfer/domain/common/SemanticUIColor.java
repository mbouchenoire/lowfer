package org.lowfer.domain.common;

import java.util.Random;

public enum SemanticUIColor {
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    OLIVE("olive"),
    GREEN("green"),
    TEAL("teal"),
    BLUE("blue"),
    VIOLET("violet"),
    PURPLE("purple"),
    PINK("pink"),
    BROWN("brown"),
    GREY("grey"),
    BLACK("black");

    private final String name;

    SemanticUIColor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SemanticUIColor randomFromSeed(String seed) {
        final int colorIndex = new Random(seed.hashCode()).nextInt(values().length);
        return values()[colorIndex];
    }
}
