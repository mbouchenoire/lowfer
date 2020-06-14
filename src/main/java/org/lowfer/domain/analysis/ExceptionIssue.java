package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;
import org.lowfer.domain.common.SoftwareComponentFilter;

public final class ExceptionIssue implements Issue {

    private final Throwable throwable;
    private final SoftwareComponentFilter filter;

    public ExceptionIssue(Throwable throwable, SoftwareComponentFilter filter) {
        this.throwable = throwable;
        this.filter = filter;
    }

    @Override
    public String getSummary() {
        return throwable.getMessage();
    }

    @Override
    public String getDescription() {
        return throwable.getMessage();
    }

    @Override
    public Rule getRule() {
        return null;
    }

    @Override
    public IssueType getType() {
        return IssueType.CONFIGURATION;
    }

    @Override
    public Severity getSeverity() {
        return Severity.CRITICAL;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return filter.test(architecture, component);
    }
}
