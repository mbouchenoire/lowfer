package org.lowfer.domain.common;

import java.util.Arrays;
import java.util.Optional;

public enum DependencyType {
    DEFAULT("default"),
    HTTP("http"),
    PUBLIC_KEY("pubkey"),
    CODEBASE("codebase"),
    QUEUE_PUBLISH("queue.publish"),
    QUEUE_SUBSCRIBE("queue.subscribe");

    private final String serializedName;

    DependencyType(String serializedName) {
        this.serializedName = serializedName;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public static Optional<DependencyType> fromSerializedName(String serializedName) {
        if (serializedName == null)
            return Optional.of(DEFAULT);

        return Arrays.stream(values())
                .filter(type -> type.serializedName.toUpperCase().equals(serializedName.toUpperCase()))
                .findAny();
    }
}
