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

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.lowfer.domain.common.DependencyType.CODEBASE;
import static org.lowfer.domain.common.SoftwareComponentType.*;

public final class PreferLibraryOverServiceIssueFinder implements IssueFinder {

    @Override
    public List<Issue> find(SoftwareArchitecture architecture) {
        return architecture.getComponents().stream()
            .filter(component -> couldBeLibrary(component, architecture))
            .map(ServiceCouldBeLibrary::new)
            .collect(toUnmodifiableList());
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean couldBeLibrary(SoftwareComponent component, SoftwareArchitecture architecture) {
        if (component.getType() != SERVICE)
            return false;

        if (component.isExternal() || isExternalEntryPoint(component, architecture))
            return false;

        if (hasStatefulDependency(component, architecture))
            return false;

        if (hasStatefulCodebaseHasDependency(component, architecture))
            return false;

        if (isBackendForFrontend(component, architecture)) {
            return false;
        }

        return true;
    }

    private boolean isExternalEntryPoint(SoftwareComponent component, SoftwareArchitecture architecture) {
        return architecture.getComponents().stream()
            .filter(SoftwareComponent::isExternal)
            .anyMatch(externalComponent -> externalComponent.getDependencies().stream()
                .anyMatch(externalComponentDependency -> {
                    return externalComponentDependency.getComponentName().equals(component.getName());
                }));
    }

    private boolean hasStatefulDependency(SoftwareComponent component, SoftwareArchitecture architecture) {
        return component.getDependencies().stream()
            .flatMap(dependency -> architecture.findComponent(dependency).stream())
            .anyMatch(dependency -> dependency.getType().isStateful());
    }

    private boolean hasStatefulCodebaseHasDependency(SoftwareComponent component, SoftwareArchitecture architecture) {
        return component.getDependencies().stream()
            .anyMatch(dependency -> dependency.getDependencyType()
                .filter(dependencyType -> dependencyType == CODEBASE)
                .flatMap(t -> architecture.findComponent(dependency))
                .filter(dependencyComponent -> hasStatefulDependency(dependencyComponent, architecture))
                .isPresent());
    }

    private boolean hasOnlyFrontendUsers(SoftwareComponent service, SoftwareArchitecture architecture) {
        return architecture.findClients(service).stream()
            .allMatch(component -> hasOnlyFrontendUsers(new DependencyChain(component), architecture));
    }

    private static boolean hasOnlyFrontendUsers(
            DependencyChain dependencyChain, SoftwareArchitecture architecture) {

        final List<SoftwareComponent> users = architecture.findClients(dependencyChain.head());

        if (users.isEmpty()) {
            return (dependencyChain.getComponents().stream()
                .allMatch(component -> component.getType() == FRONTEND || component.getType() == LIBRARY));
        } else {
            return users.stream()
                .allMatch(component -> hasOnlyFrontendUsers(dependencyChain.addFirst(component), architecture));
        }
    }

    private boolean hasMultipleServicesAsDependencies(SoftwareComponent component, SoftwareArchitecture architecture) {
        return component.getDependencies().stream()
            .flatMap(dependency -> architecture.findComponent(dependency).stream())
            .filter(dependency -> dependency.getType() == SERVICE)
            .count() >= 2;

    }

    private boolean isBackendForFrontend(SoftwareComponent component, SoftwareArchitecture architecture) {
        // FIXME: an api gateway does not necessarily have only frontend users
        return hasOnlyFrontendUsers(component, architecture)
            && hasMultipleServicesAsDependencies(component, architecture);
    }
}
