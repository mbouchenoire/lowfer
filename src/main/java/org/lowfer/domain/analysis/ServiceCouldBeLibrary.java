package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;
import org.lowfer.domain.common.SoftwareComponentType;

public final class ServiceCouldBeLibrary implements Issue {

    private final SoftwareComponent service;

    public ServiceCouldBeLibrary(SoftwareComponent service) {
        if (service.getType() != SoftwareComponentType.SERVICE)
            throw new IllegalArgumentException("Component is not a service");

        this.service = service;
    }

    @Override
    public String getSummary() {
        return String.format("Service '%s' should probably be a library", service.getLabel());
    }

    @Override
    public String getDescription() {
        return getSummary();
    }

    @Override
    public Rule getRule() {
        return Rule.PREFER_LIBRARY_OVER_SERVICE;
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
        return component.equals(service);
    }
}
