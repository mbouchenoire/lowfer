package org.lowfer.web.rest.vm;

import org.lowfer.domain.analysis.Rule;

public final class RuleView {

    private final String name;
    private final String label;

    public RuleView(Rule rule) {
        this.name = rule.name();
        this.label = rule.getLabel();
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }
}
