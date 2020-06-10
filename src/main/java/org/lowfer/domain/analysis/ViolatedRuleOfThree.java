package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

import java.util.Collections;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public final class ViolatedRuleOfThree implements Issue {

    private final SoftwareComponent component;
    private final Set<SoftwareComponent> users;

    public ViolatedRuleOfThree(SoftwareComponent component, Set<SoftwareComponent> users) {
        this.component = component;
        this.users = Collections.unmodifiableSet(users);
    }

    @Override
    public Rule getRule() {
        return Rule.APPLY_RULE_OF_THREE;
    }

    @Override
    public String getSummary() {
        return String.format(
                "Library '%s' is used by only %d component%s",
                component.getLabel(),
                users.size(),
                users.size() > 1 ? "s" : "");
    }

    @Override
    public String getDescription() {
        return String.format(
            "Component '%s' violates the rule of three (%d user%s: %s)",
            component.getLabel(),
            users.size(),
            users.size() > 1 ? "s" : "",
            users.stream().map(SoftwareComponent::getLabel).collect(joining(", ")));
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
        return this.component.equals(component) || users.contains(component);
    }
}
