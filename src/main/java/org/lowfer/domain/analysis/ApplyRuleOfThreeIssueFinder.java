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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.lowfer.domain.common.SoftwareComponentType.LIBRARY;

/**
 * Finds libraries that are used by 2 or less components,
 * potentially not following the rule of 3.
 */
public final class ApplyRuleOfThreeIssueFinder implements IssueFinder {

    private static final Logger LOG = LoggerFactory.getLogger(ApplyRuleOfThreeIssueFinder.class);

    @Override
    public List<Issue> find(SoftwareArchitecture architecture) {
        LOG.trace("Finding libraries that are used by 2 or less components (architecture: {})...", architecture.getName());

        return architecture.getComponents().stream()
                .filter(component -> component.getType() == LIBRARY)
                .filter(component -> !component.isExternal())
                .flatMap(library -> {
                    final Set<SoftwareComponent> clients = architecture.getComponents().stream()
                            .filter(component -> component.getDependencies().stream()
                                    .anyMatch(dependency -> dependency.getComponentName().equals(library.getName())))
                            .collect(toSet());

                    if (clients.size() < 3) {
                        LOG.debug("Found library with only {} clients: {}", clients.size(), library.getName());
                        return Stream.of(new ViolatedRuleOfThree(library, clients));
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(toUnmodifiableList());
    }
}
