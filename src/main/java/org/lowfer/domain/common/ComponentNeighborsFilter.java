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

import java.util.Collections;
import java.util.Set;

public final class ComponentNeighborsFilter implements SoftwareComponentFilter {

    private final Set<String> componentNames;

    private ComponentNeighborsFilter(Set<String> componentNames) {
        this.componentNames = Collections.unmodifiableSet(componentNames);
    }

    public String getText() {
        return String.join(",", componentNames);
    }

    public static ComponentNeighborsFilter ofText(String text) {
        final Set<String> names = Set.of(text.split(","));
        return new ComponentNeighborsFilter(names);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        if (componentNames.isEmpty())
            return true;

        if (componentNames.contains(component.getName()))
            return true;

        final boolean isFanIn = component.getDependencies().stream()
                .anyMatch(dependency -> componentNames.contains(dependency.getComponentName()));

        if (isFanIn)
            return true;

        final boolean isFanOut = architecture.getComponents().stream()
                .filter(c -> componentNames.contains(c.getName()))
                .anyMatch(c -> c.getDependencies().stream().anyMatch(dependency -> dependency.getComponentName().equals(component.getName())));

        if (isFanOut)
            return true;

        return false;
    }
}
