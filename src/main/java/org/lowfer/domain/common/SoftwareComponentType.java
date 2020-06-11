/*
 * Copyright 2020 the original author or authors from the Lowfer project.
 *
 * This file is part of the Lowfer project, see https://github.com/mbouchenoire/lowfer
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
