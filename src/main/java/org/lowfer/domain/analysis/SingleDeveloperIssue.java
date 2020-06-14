package org.lowfer.domain.analysis;

import org.lowfer.domain.common.CommitAuthor;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

public class SingleDeveloperIssue implements Issue {

    private final String componentName;
    private final CommitAuthor developer;
    private final int commitPercentage;

    public SingleDeveloperIssue(String componentName, CommitAuthor developer, int commitPercentage) {
        this.componentName = componentName;
        this.developer = developer;
        this.commitPercentage = commitPercentage;
    }

    @Override
    public String getSummary() {
        return developer.getName() + " is the unique maintainer of " + componentName;
    }

    @Override
    public String getDescription() {
        return developer.getName() + " is responsible for " + commitPercentage + "% of commits on " + componentName;
    }

    @Override
    public Rule getRule() {
        return Rule.AVOID_HAVING_SINGLE_DEVELOPER;
    }

    @Override
    public IssueType getType() {
        return IssueType.MAINTENANCE;
    }

    @Override
    public Severity getSeverity() {
        return Severity.MINOR;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return this.componentName.equals(component.getName());
    }
}
