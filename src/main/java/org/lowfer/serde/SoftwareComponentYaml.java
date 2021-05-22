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

import org.lowfer.domain.common.SoftwareComponent;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SoftwareComponentYaml {

    private String name;
    private String label;
    private String description;
    private String type;
    private String context;
    private String repository;
    private List<MaintainerYaml> maintainers;
    private List<ComponentDependencyYaml> dependencies;

    SoftwareComponentYaml() {
        this.name = null;
        this.label = null;
        this.description = null;
        this.type = null;
        this.context = null;
        this.repository = null;
        this.maintainers = Collections.emptyList();
        this.dependencies = Collections.emptyList();
    }

    public SoftwareComponentYaml(SoftwareComponent component) {
        this.name = component.getName();
        this.label = component.getLabel();
        this.description = component.getDescription();
        this.type = component.getType().getName();
        this.context = component.getContext().orElse(null);
        this.maintainers = component.getMaintainers().stream()
            .map(MaintainerYaml::new)
            .collect(Collectors.toList());
        this.dependencies = component.getDependencies().stream()
            .map(ComponentDependencyYaml::new)
            .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return isNotBlank(label) ? label : name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getRepository() {
        return repository;
    }

    public List<MaintainerYaml> getMaintainers() {
        return maintainers == null ? Collections.emptyList() : maintainers;
    }

    public void setMaintainers(List<MaintainerYaml> maintainers) {
        this.maintainers = maintainers;
    }

    public List<ComponentDependencyYaml> getDependencies() {
        return dependencies != null ? Collections.unmodifiableList(dependencies) : Collections.emptyList();
    }

    public void setDependencies(List<ComponentDependencyYaml> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public String toString() {
        return name
            + Optional.ofNullable(type).map(type -> " (" + type + ")").orElse("")
            + (maintainers.isEmpty() ? "" : ", maintainers: " + maintainers);
    }
}
