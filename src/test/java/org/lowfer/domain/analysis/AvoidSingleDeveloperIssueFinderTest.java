package org.lowfer.domain.analysis;

import io.vavr.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.lowfer.domain.SoftwareArchitectureMother;
import org.lowfer.domain.common.*;
import org.lowfer.repository.AsyncComponentGitRepository;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AvoidSingleDeveloperIssueFinderTest {

    @Test
    public void testFindIssues() {
        final AsyncComponentGitRepository repository = new AsyncComponentGitRepository() {

            private final CommitAuthor BOT_AUTHOR = new CommitAuthor("bot", "bot@gmail.com");

            @Override
            public String getComponentName() {
                return "mock";
            }

            @Override
            public Future<List<Commit>> getCommits(String branchName) {
                return Future.successful(List.of(
                    new Commit(Instant.now(), "", BOT_AUTHOR),
                    new Commit(Instant.now(), "", BOT_AUTHOR),
                    new Commit(Instant.now(), "", BOT_AUTHOR),
                    new Commit(Instant.now(), "", BOT_AUTHOR),
                    new Commit(Instant.now(), "", BOT_AUTHOR),
                    new Commit(Instant.now(), "", BOT_AUTHOR),
                    new Commit(Instant.now(), "", BOT_AUTHOR),
                    new Commit(Instant.now(), "", BOT_AUTHOR),
                    new Commit(Instant.now(), "", new CommitAuthor("author1", "author1@email.com")),
                    new Commit(Instant.now(), "", new CommitAuthor("author2", "author2@email.cm"))));
            }
        };

        final SoftwareComponent helloWorldComponent = new SoftwareComponent(
            "hello-world",
            "",
            null,
            SoftwareComponentType.SERVICE,
            null,
            repository,
            Collections.emptySet(),
            Collections.emptySet());

        final SoftwareArchitecture single = SoftwareArchitectureMother.single(helloWorldComponent);

        final List<Issue> issuesForMinimumThreshold = new AvoidSingleDeveloperIssueFinder(1).find(single);

        assertEquals(2, issuesForMinimumThreshold.stream() // we ignore the bot
            .filter(issue -> issue.getRule() == Rule.AVOID_HAVING_SINGLE_DEVELOPER)
            .count());

        final List<Issue> issuesForNormalThreshold = new AvoidSingleDeveloperIssueFinder(80).find(single);

        assertEquals(0, issuesForNormalThreshold.stream()
            .filter(issue -> issue.getRule() == Rule.AVOID_HAVING_SINGLE_DEVELOPER)
            .count());
    }

    @Test
    public void testFindIssuesWhenRepositoryNotLoaded() {
        final AsyncComponentGitRepository repository = new AsyncComponentGitRepository() {

            @Override
            public String getComponentName() {
                return "mock";
            }

            @Override
            public Future<List<Commit>> getCommits(String branchName) {
                return Future.of(() -> {
                    Thread.sleep(Integer.MAX_VALUE);
                    return Collections.emptyList();
                });
            }
        };

        final SoftwareComponent helloWorldComponent = new SoftwareComponent(
            "hello-world",
            "",
            null,
            SoftwareComponentType.SERVICE,
            null,
            repository,
            Collections.emptySet(),
            Collections.emptySet());

        final SoftwareArchitecture single = SoftwareArchitectureMother.single(helloWorldComponent);

        final List<Issue> issuesForMinimumThreshold = new AvoidSingleDeveloperIssueFinder(1).find(single);

        assertEquals(1, issuesForMinimumThreshold.stream()
            .filter(issue -> issue.getRule() == Rule.AVOID_HAVING_SINGLE_DEVELOPER)
            .filter(issue -> issue.getType() == IssueType.LOADING)
            .count());
    }
}
