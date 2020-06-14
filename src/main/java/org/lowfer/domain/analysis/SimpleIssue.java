package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;
import org.lowfer.domain.common.SoftwareComponentFilter;

public final class SimpleIssue implements Issue {

    private final String summary;
    private final Rule rule;
    private final IssueType issueType;
    private final Severity severity;
    private final SoftwareComponentFilter filter;

    public SimpleIssue(
        String summary,
        Rule rule,
        IssueType issueType,
        Severity severity,
        SoftwareComponentFilter filter) {

        this.summary = summary;
        this.rule = rule;
        this.issueType = issueType;
        this.severity = severity;
        this.filter = filter;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public String getDescription() {
        return summary;
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    @Override
    public IssueType getType() {
        return issueType;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return filter.test(architecture, component);
    }
}
