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
        return Severity.CRITICAL;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return component.equals(database) || clients.contains(component);
    }
}
