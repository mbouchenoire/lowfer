package org.lowfer.web.rest.vm;

public final class IconView {

    private final String name;
    private final String color;

    public IconView(Icon icon) {
        this.name = icon.getName();
        this.color = icon.getColor().getName();
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
