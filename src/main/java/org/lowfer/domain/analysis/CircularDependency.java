package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;
import org.lowfer.domain.common.SoftwareComponentFilter;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public final class CircularDependency implements Issue {

    private final DependencyChain dependencyChain;

    public CircularDependency(DependencyChain dependencyChain) {
        if (!dependencyChain.isCircularDependency())
            throw new IllegalArgumentException("Dependency chain is not circular");

        this.dependencyChain = dependencyChain;
    }

    @Override
    public Rule getRule() {
        return Rule.APPLY_ACYCLIC_DEPENDENCIES_PRINCIPLE;
    }

    @Override
    public String getSummary() {
        return "Circular dependency: " + dependencyChain.getComponents().stream()
            .map(SoftwareComponent::getLabel)
            .collect(Collectors.joining(" -> "));
    }

    @Override
    public String getDescription() {
        return getSummary();
    }

    @Override
    public IssueType getType() {
        return IssueType.MAINTENANCE;
    }

    @Override
    public Severity getSeverity() {
        return Severity.BLOCKER;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return dependencyChain.getComponents().contains(component);
    }

    /**
     * @return true if this circular dependency cycles with the same components
     *          as the provided circular dependency.
     */
    public boolean isRedundantWith(CircularDependency other) {
        return this.signature().equals(other.signature());
    }

    private String signature() {
        return dependencyChain.getComponents().stream() // c9 -> c2 -> c3 -> c1 -> c2
                .skip(dependencyChain.getComponents().indexOf(dependencyChain.tail())) // c2 -> c3 -> c1 -> c2
                .map(SoftwareComponent::getName)
                .sorted() // c1 -> c1 -> c2 -> c3
                .distinct() // c1 -> c2 -> c3
                .collect(joining(",")); // c1c2c3
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
