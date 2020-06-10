package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareComponentFilter;

public interface Issue extends SoftwareComponentFilter {

    String getSummary();

    String getDescription();

    Rule getRule();

    IssueType getType();

    Severity getSeverity();

    // TODO: define which components are relevant to the issue
    //  (to use their types as issue tags, for example)

    // TODO: define how "zoom in" on the issue architecture with graphviz
    //  ({@link SoftwareComponentFilter} ?)

    // TODO: define how to "highlight" (if necessary) the relevant component(s)
    //   in the issue graphviz ({@link SoftwareComponentFilter} ?)
}
