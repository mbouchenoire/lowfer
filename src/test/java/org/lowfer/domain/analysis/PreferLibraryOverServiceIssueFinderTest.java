package org.lowfer.domain.analysis;

import org.junit.jupiter.api.Test;
import org.lowfer.domain.common.ComponentDependency;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponent;

import java.util.List;

import static java.util.Set.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lowfer.domain.SoftwareComponentMother.external;
import static org.lowfer.domain.SoftwareComponentMother.single;
import static org.lowfer.domain.analysis.Rule.PREFER_LIBRARY_OVER_SERVICE;
import static org.lowfer.domain.common.DependencyType.CODEBASE;
import static org.lowfer.domain.common.SoftwareComponentType.*;

class PreferLibraryOverServiceIssueFinderTest {

    @Test
    void testNoIssueBecauseExternal() {
        final SoftwareComponent library = single("library", of("service"), LIBRARY);
        final SoftwareComponent service = external("service", SERVICE);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(library, service));

        final List<Issue> issues = new PreferLibraryOverServiceIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == PREFER_LIBRARY_OVER_SERVICE));
    }

    @Test
    void testNoIssueBecauseIsExternalEntryPoint() {
        final SoftwareComponent service = single("service", SERVICE);
        final SoftwareComponent external = external("external", SERVICE, of("service"));

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(service, external));

        final List<Issue> issues = new PreferLibraryOverServiceIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == PREFER_LIBRARY_OVER_SERVICE));
    }

    @Test
    void testNoIssueBecauseHasCodebase() {
        final SoftwareComponent library = single("library", of("api1"), LIBRARY);
        final SoftwareComponent api1 = single("api1", SERVICE, of(new ComponentDependency("codebase", CODEBASE)));
        final SoftwareComponent codebase = single("codebase", of("db"), LIBRARY);
        final SoftwareComponent db = single("db", DATABASE);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(library, api1, codebase, db));

        final List<Issue> issues = new PreferLibraryOverServiceIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == PREFER_LIBRARY_OVER_SERVICE));
    }

    @Test
    void testNoIssueBecauseBackendForFrontend() {
        final SoftwareComponent frontend1 = single("frontend1", of("service1"), FRONTEND);
        final SoftwareComponent bff = single("bff", of("service2", "service3"), SERVICE);
        final SoftwareComponent service2 = single("service2", of("db2"), SERVICE);
        final SoftwareComponent db2 = single("db2", DATABASE);
        final SoftwareComponent service3 = single("service3", of("db3"), SERVICE);
        final SoftwareComponent db3 = single("db3", DATABASE);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(frontend1, bff, service2, db2, service3, db3));

        final List<Issue> issues = new PreferLibraryOverServiceIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == PREFER_LIBRARY_OVER_SERVICE));
    }

    @Test
    void testNoIssueBecauseHasDatabase() {
        final SoftwareComponent library = single("library", of("service"), LIBRARY);
        final SoftwareComponent service = single("service", of("db"), SERVICE);
        final SoftwareComponent db = single("db", DATABASE);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(library, service, db));

        final List<Issue> issues = new PreferLibraryOverServiceIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == PREFER_LIBRARY_OVER_SERVICE));
    }

    @Test
    void testNoIssueBecauseHasSecret() {
        final SoftwareComponent library = single("library", of("service"), LIBRARY);
        final SoftwareComponent service = single("service", of("secret"), SERVICE);
        final SoftwareComponent secret = single("secret", SECRET);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(library, service, secret));

        final List<Issue> issues = new PreferLibraryOverServiceIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == PREFER_LIBRARY_OVER_SERVICE));
    }

    @Test
    void testNoIssueBecauseHasConfig() {
        final SoftwareComponent library = single("library", of("service"), LIBRARY);
        final SoftwareComponent service = single("service", of("config"), SERVICE);
        final SoftwareComponent config = single("config", CONFIG);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(library, service, config));

        final List<Issue> issues = new PreferLibraryOverServiceIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == PREFER_LIBRARY_OVER_SERVICE));
    }

    @Test
    void testNoIssueBecauseHasQueue() {
        final SoftwareComponent library = single("library", of("service"), LIBRARY);
        final SoftwareComponent service = single("service", of("queue"), SERVICE);
        final SoftwareComponent queue = single("queue", QUEUE);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(library, service, queue));

        final List<Issue> issues = new PreferLibraryOverServiceIssueFinder().find(architecture);
        assertTrue(issues.stream().noneMatch(issue -> issue.getRule() == PREFER_LIBRARY_OVER_SERVICE));
    }

    @Test
    void testIssueBecauseNoDatabase() {
        final SoftwareComponent frontend1 = single("frontend", of("service"), FRONTEND);
        final SoftwareComponent service1 = single("service", SERVICE);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(frontend1, service1));

        final List<Issue> issues = new PreferLibraryOverServiceIssueFinder().find(architecture);
        assertEquals(1, issues.stream().filter(issue -> issue.getRule() == PREFER_LIBRARY_OVER_SERVICE).count());
    }

    @Test
    void testIssueBecauseCodebaseHasNoDatabase() {
        final SoftwareComponent library = single("library", of("api1"), LIBRARY);
        final SoftwareComponent api1 = single("api1", SERVICE, of(new ComponentDependency("codebase", CODEBASE)));
        final SoftwareComponent codebase = single("codebase", LIBRARY);

        final SoftwareArchitecture architecture = new SoftwareArchitecture("ok", of(library, api1, codebase));

        final List<Issue> issues = new PreferLibraryOverServiceIssueFinder().find(architecture);
        assertEquals(1, issues.stream().filter(issue -> issue.getRule() == PREFER_LIBRARY_OVER_SERVICE).count());
    }
}
