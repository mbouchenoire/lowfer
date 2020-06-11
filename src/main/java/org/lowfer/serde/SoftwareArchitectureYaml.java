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

import org.lowfer.domain.common.SoftwareArchitecture;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SoftwareArchitectureYaml {

    private String name;
    private List<SoftwareComponentYaml> components;

    SoftwareArchitectureYaml() {
        this.name = null;
        this.components = Collections.emptyList();
    }

    public SoftwareArchitectureYaml(SoftwareArchitecture architecture) {
        this.name = architecture.getName();
        this.components = architecture.getComponents().stream()
            .map(SoftwareComponentYaml::new)
            .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SoftwareComponentYaml> getComponents() {
        return Collections.unmodifiableList(components);
    }

    public void setComponents(List<SoftwareComponentYaml> components) {
        this.components = components;
    }
}
