package org.lowfer.domain.common;

import org.lowfer.config.Color;

import java.util.Arrays;
import java.util.Optional;

public enum SoftwareComponentType {
    LIBRARY("library", false, "Library", "Libraries", Color.YELLOW),
    FRONTEND("frontend", true, "Front-end", "Front-ends", Color.BLUE),
    SCRIPT("script", false, "Script", "Scripts", Color.BLUE),
    SERVICE("service", false, "Service", "Services", Color.GREEN),
    GATEWAY("gateway", false, "Gateway", "Gateways", Color.YELLOW),
    FUNCTION("function", false, "Function", "Functions", Color.ORANGE),
    DATABASE("database", true, "Database", "Databases", Color.RED),
    OBJECT_STORAGE("object-storage", true, "Object Storage", "Object Storage", Color.RED),
    CACHE("cache", true, "Cache", "Caches", Color.ORANGE),
    STORED_PROCEDURE("stored-procedure", false, "SQL script", "SQL scripts", Color.ORANGE),
    QUEUE("queue", true, "Queue", "Queues", Color.BLACK),
    SECRET("secret", true, "Secret", "Secrets", Color.GRAY),
    CONFIG("config", true, "Config", "Configs", Color.GRAY),
    AGGREGATE("aggregate", false, "Aggregate", "Aggregates", Color.GRAY);

    private final String name;
    private final boolean stateful;
    private final String singularLabel;
    private final String pluralLabel;
    private final Color graphvizColor;

    SoftwareComponentType(
            String name,
            boolean stateful,
            String singularLabel,
            String pluralLabel,
            Color graphvizColor) {

        this.name = name;
        this.stateful = stateful;
        this.singularLabel = singularLabel;
        this.pluralLabel = pluralLabel;
        this.graphvizColor = graphvizColor;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getName() {
        return name;
    }

    public String getSingularLabel() {
        return singularLabel;
    }

    public String getPluralLabel() {
        return pluralLabel;
    }

    public Color getColor() {
        return graphvizColor;
    }

    public static Optional<SoftwareComponentType> fromSerializedName(String serializedName) {
        if (serializedName == null)
            return Optional.empty();

        return Arrays.stream(values())
                .filter(type -> type.name.toUpperCase().equals(serializedName.toUpperCase()))
                .findAny();
    }
}
