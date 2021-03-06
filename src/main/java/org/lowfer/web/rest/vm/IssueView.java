/*
 * Copyright 2020 the original author or authors from the Lowfer project.
 *
 * This file is part of the Lowfer project, see https://github.com/mbouchenoire/lowfer
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
