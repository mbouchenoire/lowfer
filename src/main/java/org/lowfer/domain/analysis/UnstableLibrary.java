package org.lowfer.domain.analysis;

import org.lowfer.domain.common.ComponentInstability;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;
import org.lowfer.domain.common.SoftwareComponentType;

public final class UnstableLibrary implements Issue {

    private final ComponentInstability unstableLibrary;

    public UnstableLibrary(ComponentInstability unstableLibrary) {
        if (unstableLibrary.getComponent().getType() != SoftwareComponentType.LIBRARY)
            throw new IllegalArgumentException("Unstable library is not a library");

        this.unstableLibrary = unstableLibrary;
    }

    @Override
    public String getSummary() {
        return String.format("Library '%s' is unstable", unstableLibrary.getComponent().getName());
    }

    @Override
    public String getDescription() {
        return String.format(
            "Library '%s' is unstable (I=%s)",
            unstableLibrary.getComponent().getLabel(),
            unstableLibrary.getFormattedValue(2));
    }

    @Override
    public Rule getRule() {
        return Rule.AVOID_UNSTABLE_LIBRARY;
    }

    @Override
    public IssueType getType() {
        return IssueType.MAINTENANCE;
    }

    @Override
    public Severity getSeverity() {
        return Severity.MAJOR;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return component.equals(unstableLibrary.getComponent())
            || unstableLibrary.getComponent().getDependencies().stream().anyMatch(dependency -> dependency.getComponentName().equals(component.getName()))
            || architecture.findClients(unstableLibrary.getComponent()).contains(component);
    }
}
