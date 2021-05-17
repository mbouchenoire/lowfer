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
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public final class ComponentTypeFilter implements SoftwareComponentFilter {

    private final Set<SoftwareComponentType> componentTypes;

    private ComponentTypeFilter(Set<SoftwareComponentType> componentTypes) {
        this.componentTypes = Collections.unmodifiableSet(componentTypes);
    }

    public String getText() {
        return componentTypes.stream()
                .map(SoftwareComponentType::getName)
                .collect(Collectors.joining(","));
    }

    public static ComponentTypeFilter ofText(String text) {
        final Set<SoftwareComponentType> types = Arrays.stream(text.split(","))
                .flatMap(s -> SoftwareComponentType.fromSerializedName(s).stream())
                .collect(toSet());

        return new ComponentTypeFilter(types);
    }

    @Override
    public boolean isSet() {
        return !componentTypes.isEmpty();
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return componentTypes.contains(component.getType());
    }
}
