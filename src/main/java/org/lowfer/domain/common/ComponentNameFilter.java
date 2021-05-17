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

public final class ComponentNameFilter implements SoftwareComponentFilter {

    private final Set<String> componentNames;

    private ComponentNameFilter(Set<String> componentNames) {
        this.componentNames = Collections.unmodifiableSet(componentNames);
    }

    public String getText() {
        return String.join(",", componentNames);
    }

    public static ComponentNameFilter ofText(String text) {
        final Set<String> names = Arrays.stream(text.split(","))
                .filter(s -> !"".equals(s))
                .collect(Collectors.toSet());

        return new ComponentNameFilter(names);
    }

    @Override
    public boolean isSet() {
        return !componentNames.isEmpty();
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return componentNames.contains(component.getName());
    }
}
