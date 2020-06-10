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
