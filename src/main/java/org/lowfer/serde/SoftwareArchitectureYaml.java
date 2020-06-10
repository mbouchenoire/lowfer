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
