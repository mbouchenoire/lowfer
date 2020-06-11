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
