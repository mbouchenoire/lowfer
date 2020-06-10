package org.lowfer.domain.analysis;

import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lowfer.domain.SoftwareComponentMother.single;
import static org.lowfer.domain.common.SoftwareComponentType.*;

class AvoidDatabaseWithMultipleClientsIssueFinderTest {

    @Test
    void testNoDatabaseWithMultipleClients() {
        final SoftwareComponent service1 = single("service1", Set.of("db1"), SERVICE);
        final SoftwareComponent db1 = single("db1", DATABASE);
        final SoftwareComponent service2 = single("service2", Set.of("db2"), SERVICE);
        final SoftwareComponent db2 = single("db2", DATABASE);
        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", Set.of(service1, db1, service2, db2));
        final List<Issue> issues = new AvoidDatabaseWithMultipleClientsIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == Rule.AVOID_DATABASE_MULTIPLE_CLIENTS));
    }

    @Test
    void testSingleDatabaseWithMultipleClients() {
        final SoftwareComponent service1 = single("service1", Set.of("db1"), SERVICE);
        final SoftwareComponent service2 = single("service2", Set.of("db1"), SERVICE);
        final SoftwareComponent db1 = single("db1", DATABASE);
        final SoftwareArchitecture architecture = new SoftwareArchitecture("ko", Set.of(service1, service2, db1));
        final List<Issue> issues = new AvoidDatabaseWithMultipleClientsIssueFinder().find(architecture);
        assertEquals(1, issues.stream().filter(issue -> issue.getRule() == Rule.AVOID_DATABASE_MULTIPLE_CLIENTS).count());
    }

    @Test
    void testSingleDatabaseWithMultipleClientsThroughLibrary() {
        final SoftwareComponent service1 = single("service1", Set.of("library"), SERVICE);
        final SoftwareComponent service2 = single("service2", Set.of("library"), SERVICE);
        final SoftwareComponent library = single("library", Set.of("db"), LIBRARY);
        final SoftwareComponent db1 = single("db", DATABASE);
        final SoftwareArchitecture architecture = new SoftwareArchitecture("ko", Set.of(service1, service2, library, db1));
        final List<Issue> issues = new AvoidDatabaseWithMultipleClientsIssueFinder().find(architecture);
        assertEquals(1, issues.stream().filter(issue -> issue.getRule() == Rule.AVOID_DATABASE_MULTIPLE_CLIENTS).count());
    }
}
