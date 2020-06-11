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
