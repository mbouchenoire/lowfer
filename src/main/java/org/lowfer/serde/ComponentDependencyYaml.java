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

package org.lowfer.serde;

import org.lowfer.domain.common.ComponentDependency;
import org.lowfer.domain.common.DependencyType;

import java.util.Optional;

public class ComponentDependencyYaml {

    private String component;
    private String type;

    ComponentDependencyYaml() {
        this.component = null;
        this.type = null;
    }

    public ComponentDependencyYaml(ComponentDependency dependency) {
        this.component = dependency.getComponentName();
        this.type = dependency.getDependencyType().map(DependencyType::getSerializedName).orElse(null);
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return component + Optional.ofNullable(type).map(type -> " (" + type + ")").orElse("");
    }
}
