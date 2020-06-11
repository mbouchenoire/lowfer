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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class ApplyAcyclicDependenciesPrincipleIssueFinder implements IssueFinder {

    private static final Logger LOG = LoggerFactory.getLogger(ApplyAcyclicDependenciesPrincipleIssueFinder.class);

    @Override
    public List<Issue> find(SoftwareArchitecture architecture) {
        // this implementation is so bad but it's 3am i just wanna sleep
        LOG.trace("Finding circular dependencies (architecture: {})...", architecture.getName());

        final List<CircularDependency> circularDependencies = architecture.getComponents().stream()
                .flatMap(component -> findCircularDependencies(architecture, new DependencyChain(component)).stream())
                .collect(toUnmodifiableList());

        final List<CircularDependency> filteredCircularDependencies = new LinkedList<>();

        for (CircularDependency circularDependency : circularDependencies) {
            filteredCircularDependencies.stream()
                    .filter(filteredCircularDependency -> filteredCircularDependency.isRedundantWith(circularDependency))
                    .findAny()
                    .ifPresentOrElse(filteredCircularDependency -> {
                        LOG.trace("Found redundant circular dependency: {} <-> {} (architecture: {})", circularDependency, filteredCircularDependency, architecture.getName());
                        filteredCircularDependencies.remove(filteredCircularDependency);
                        filteredCircularDependencies.add(circularDependency);
                    }, () -> filteredCircularDependencies.add(circularDependency));
        }

        return filteredCircularDependencies.stream().collect(toUnmodifiableList());
    }

    private static List<CircularDependency> findCircularDependencies(
            SoftwareArchitecture architecture, DependencyChain dependencyChain) {

        LOG.trace("Finding circular dependencies with current dependency chain: {} (architecture: {})...", dependencyChain, architecture.getName());

        return dependencyChain.getCircularDependency()
                .map(Collections::singletonList)
                .orElseGet(() -> dependencyChain.tail().getDependencies().stream()
                        .flatMap(dependency -> architecture.findComponent(dependency).stream())
                        .flatMap(dependency -> findCircularDependencies(architecture, dependencyChain.add(dependency)).stream())
                        .collect(toUnmodifiableList()));
    }
}
