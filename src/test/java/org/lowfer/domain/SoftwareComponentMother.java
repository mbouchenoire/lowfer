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

package org.lowfer.domain;

import org.lowfer.domain.common.*;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class SoftwareComponentMother {

    public static SoftwareComponent single() {
        return single(UUID.randomUUID().toString());
    }

    public static SoftwareComponent single(String name) {
        return single(name, Collections.emptySet());
    }

    public static SoftwareComponent single(String name, SoftwareComponentType type) {
        return single(name, Collections.emptySet(), type);
    }

    public static SoftwareComponent single(String name, Set<String> dependencyNames) {
        return single(name, dependencyNames, SoftwareComponentType.SERVICE);
    }

    public static SoftwareComponent single(String name, Set<String> dependencyNames, SoftwareComponentType type) {
        return new SoftwareComponent(
                name,
                name,
                type,
                null,
                Collections.emptySet(),
                dependencyNames.stream().map(ComponentDependency::new).collect(Collectors.toSet()));
    }

    public static SoftwareComponent single(String name, SoftwareComponentType type, Set<ComponentDependency> dependencies) {
        return new SoftwareComponent(
                name,
                name,
                type,
                null,
                Collections.emptySet(),
                dependencies);
    }

    public static SoftwareComponent external(String name, SoftwareComponentType type) {
        return external(name, type, Collections.emptySet());
    }

    public static SoftwareComponent external(String name, SoftwareComponentType type, Set<String> dependencyNames) {
        return new SoftwareComponent(
                name,
                name,
                type,
                null,
                Set.of(Maintainer.external(SemanticUIColor.GREY)),
                dependencyNames.stream().map(ComponentDependency::new).collect(Collectors.toSet()));
    }
}
