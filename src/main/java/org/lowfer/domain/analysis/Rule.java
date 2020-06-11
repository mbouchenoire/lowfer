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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Rule {
    APPLY_ACYCLIC_DEPENDENCIES_PRINCIPLE("Apply the Acyclic Dependencies Principle", new ApplyAcyclicDependenciesPrincipleIssueFinder()),
    APPLY_RULE_OF_THREE("Apply the Rule of Three", new ApplyRuleOfThreeIssueFinder()),
    APPLY_STABLE_DEPENDENCY_PRINCIPLE("Apply the Stable Dependency Principle", new ApplyStableDependencyPrincipleIssueFinder()),
    AVOID_DATABASE_MULTIPLE_CLIENTS("Avoid database with multiple clients", new AvoidDatabaseWithMultipleClientsIssueFinder()),
    AVOID_QUEUE_MULTIPLE_PUBLISHERS("Avoid queue with multiple publishers", new AvoidQueueWithMultiplePublishersIssueFinder()),
    AVOID_TRANSITIVE_DEPENDENCIES("Avoid transitive dependencies", new AvoidTransitiveDependencyIssueFinder()),
    AVOID_UNSTABLE_LIBRARY("Avoid unstable libraries", new AvoidUnstableLibraryIssueFinder()),
    PREFER_LIBRARY_OVER_SERVICE("Prefer libraries over services", new PreferLibraryOverServiceIssueFinder(), false);

    // TODO: avoid service used by only one non-frontend component
    // TODO: watch for availability ? https://queue.acm.org/detail.cfm?id=3096459

    private final String label;
    private final IssueFinder issueFinder;
    private final boolean published;

    Rule(String label, IssueFinder issueFinder) {
        this(label, issueFinder, true);
    }

    Rule(String label, IssueFinder issueFinder, boolean published) {
        this.label = label;
        this.issueFinder = issueFinder;
        this.published = published;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Some rules might not be ready to be published, e.g. when the specifications are
     * not done, or the available data is not sufficient.
     *
     * @return true if the rule is ready to be published, false otherwise
     */
    public boolean isPublished() {
        return published;
    }

    public static List<Issue> findAllIssues(SoftwareArchitecture architecture) {
        return Arrays.stream(values())
            .filter(Rule::isPublished)
            .flatMap(rule -> rule.issueFinder.find(architecture).stream())
            .collect(Collectors.toUnmodifiableList());
    }
}
