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
import org.lowfer.domain.common.SoftwareComponentType;

public final class ServiceCouldBeLibrary implements Issue {

    private final SoftwareComponent service;

    public ServiceCouldBeLibrary(SoftwareComponent service) {
        if (service.getType() != SoftwareComponentType.SERVICE)
            throw new IllegalArgumentException("Component is not a service");

        this.service = service;
    }

    @Override
    public String getSummary() {
        return String.format("Service '%s' should probably be a library", service.getLabel());
    }

    @Override
    public String getDescription() {
        return getSummary();
    }

    @Override
    public Rule getRule() {
        return Rule.PREFER_LIBRARY_OVER_SERVICE;
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
        return component.equals(service);
    }
}
