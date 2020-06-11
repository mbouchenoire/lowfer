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

package org.lowfer.web.rest.vm;

import org.lowfer.domain.common.SoftwareComponentType;

public final class ComponentTypeListItemView {

    private final String name;
    private final String label;

    public ComponentTypeListItemView(SoftwareComponentType componentType) {
        this.name = componentType.getName();
        this.label = componentType.getPluralLabel();
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }
}
