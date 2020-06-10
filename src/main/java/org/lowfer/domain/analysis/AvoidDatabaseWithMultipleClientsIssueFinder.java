package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.lowfer.domain.analysis.IssueFinderUtils.findNonLibraryClients;
import static org.lowfer.domain.common.SoftwareComponentType.DATABASE;

public final class AvoidDatabaseWithMultipleClientsIssueFinder implements IssueFinder {

    private static final Logger LOG = LoggerFactory.getLogger(AvoidDatabaseWithMultipleClientsIssueFinder.class);

    @Override
    public List<Issue> find(SoftwareArchitecture architecture) {
        LOG.trace("Finding databases with multiple clients (architecture: {})...", architecture.getName());

        return architecture.getComponents().stream()
                .filter(component -> component.getType() == DATABASE)
                .flatMap(database -> {
                    final List<SoftwareComponent> databaseClients =
                        findNonLibraryClients(database, dependency -> true, architecture);

                    if (databaseClients.size() <= 1) {
                        return Stream.empty();
                    } else {
                        LOG.debug("Found database with {} clients: {} (architecture: {})", databaseClients.size(), database.getName(), architecture.getName());
                        return Stream.of(new DatabaseWithMultipleClients(database, databaseClients));
                    }
                })
                .collect(toUnmodifiableList());
    }
}
