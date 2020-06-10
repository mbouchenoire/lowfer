package org.lowfer.web.rest.vm;

import org.lowfer.domain.analysis.Issue;
import org.lowfer.domain.analysis.IssueType;
import org.lowfer.domain.analysis.Severity;

public final class IssueView {

    private final String summary;
    private final String description;
    private final RuleView rule;
    private final IssueType type;
    private final Severity severity;
    private final EncodedArchitectureView encodedArchitecture;

    public IssueView(Issue issue, EncodedArchitectureView encodedArchitecture) {
        this.summary = issue.getSummary();
        this.description = issue.getDescription();
        this.rule = new RuleView(issue.getRule());
        this.type = issue.getType();
        this.severity = issue.getSeverity();
        this.encodedArchitecture = encodedArchitecture;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public RuleView getRule() {
        return rule;
    }

    public IssueType getType() {
        return type;
    }

    public Severity getSeverity() {
        return severity;
    }

    public EncodedArchitectureView getEncodedArchitecture() {
        return encodedArchitecture;
    }
}
