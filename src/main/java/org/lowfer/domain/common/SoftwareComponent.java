package org.lowfer.domain.common;

import javax.annotation.Nullable;
import java.util.*;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static org.lowfer.domain.common.SoftwareComponentType.AGGREGATE;

public class SoftwareComponent {

    private final String name;
    private final String label;
    private final SoftwareComponentType type;
    private final String context;
    private final Set<Maintainer> maintainers;
    private final Collection<ComponentDependency> dependencies;

    public SoftwareComponent(
        String name,
        String label,
        SoftwareComponentType type,
        @Nullable String context,
        Set<Maintainer> maintainers,
        Collection<ComponentDependency> dependencies) {

        this.name = name;
        this.label = label;
        this.type = type;
        this.context = context;
        this.maintainers = maintainers;
        this.dependencies = Collections.unmodifiableCollection(dependencies);
    }

    static SoftwareComponent agg(long aggSize, @Nullable String context) {
        return new SoftwareComponent(UUID.randomUUID().toString(), "+" + aggSize, AGGREGATE, context, emptySet(), emptySet());
    }

    public long weight() {
        return type == AGGREGATE
            ? Long.parseLong(label.replace("+", ""))
            : 1;
    }

    SoftwareComponent removeDependencies(Collection<SoftwareComponent> dependencies) {
        final Set<ComponentDependency> updatedDependencies = new HashSet<>(this.dependencies);
        updatedDependencies.removeIf(componentDependency -> dependencies.stream()
            .map(SoftwareComponent::getName)
            .collect(toList())
            .contains(componentDependency.getComponentName()));

        return new SoftwareComponent(name, label, type, context, maintainers, updatedDependencies);
    }

    SoftwareComponent addDependency(ComponentDependency dependency) {
        return addDependencies(Set.of(dependency));
    }

    SoftwareComponent addDependencies(Set<ComponentDependency> dependencies) {
        if (dependencies.isEmpty())
            return this;

        final Set<ComponentDependency> updatedDependencies = new HashSet<>(this.dependencies);
        updatedDependencies.addAll(dependencies);
        return new SoftwareComponent(name, label, type, context, maintainers, updatedDependencies);
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public SoftwareComponentType getType() {
        return type;
    }

    public Optional<String> getContext() {
        return Optional.ofNullable(context);
    }

    public Set<Maintainer> getMaintainers() {
        return maintainers;
    }

    public boolean isExternal() {
        return maintainers.stream().anyMatch(Maintainer::isExternal);
    }

    public Set<ComponentDependency> getDependencies() {
        return Set.copyOf(dependencies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoftwareComponent that = (SoftwareComponent) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
