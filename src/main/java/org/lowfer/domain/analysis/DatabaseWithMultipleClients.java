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

import static java.util.stream.Collectors.joining;
import static org.lowfer.domain.common.SoftwareComponentType.DATABASE;

public final class DatabaseWithMultipleClients implements Issue {

    private final SoftwareComponent database;
    private final List<SoftwareComponent> clients;

    public DatabaseWithMultipleClients(SoftwareComponent database, List<SoftwareComponent> clients) {
        if (database.getType() != DATABASE)
            throw new IllegalArgumentException("component is not a database");

        this.database = database;
        this.clients = clients;
    }

    @Override
    public Rule getRule() {
        return Rule.AVOID_DATABASE_MULTIPLE_CLIENTS;
    }

    @Override
    public String getSummary() {
        return String.format("Database '%s' has multiple clients", database.getLabel());
    }

    @Override
    public String getDescription() {
        return String.format(
            "Database '%s' has multiple clients:\n (%s)",
            database.getLabel(),
            clients.stream()
                .map(SoftwareComponent::getName)
                .collect(joining("\n-")));
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
        return component.equals(database) || clients.contains(component);
    }
}
