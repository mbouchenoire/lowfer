package org.lowfer.domain.analysis;

import io.vavr.concurrent.Future;
import org.lowfer.domain.common.Commit;
import org.lowfer.domain.common.CommitAuthor;
import org.lowfer.domain.common.SoftwareArchitecture;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class AvoidSingleDeveloperIssueFinder implements IssueFinder {

    private static final Predicate<Commit> IGNORE_BOT_COMMITS = commit ->
        !commit.getAuthor().getEmail().contains("bot@")
            && !commit.getAuthor().getEmail().contains("build@")
            && !commit.getAuthor().getEmail().contains("builds@")
            && !commit.getAuthor().getEmail().contains("noreply@")
            && !(commit.getMessage().toUpperCase().contains("MERGE") && commit.getMessage().toUpperCase().contains("BRANCH"));

    private final int percentageThreshold;

    public AvoidSingleDeveloperIssueFinder(int percentageThreshold) {
        this.percentageThreshold = percentageThreshold;
    }

    @Override
    public List<Issue> find(SoftwareArchitecture architecture) {
        final Stream<Issue> master = architecture.getComponents().stream()
            .flatMap(component -> component.getRepository().stream()
                .flatMap(repository -> {
                    final Future<List<Commit>> commitsFuture = repository.getCommits("master");

                    if (commitsFuture.isFailure()) {
                        return commitsFuture.getCause()
                            .map(t -> new ExceptionIssue(t, (a, c) -> c.getName().equals(component.getName())))
                            .map(Stream::of)
                            .getOrElse(Stream.empty());
                    } else if (!commitsFuture.isCompleted()) {
                        return Stream.of(new SimpleIssue(
                            "Lowfer is currently loading the Git commits of '" + component.getLabel() + "'...",
                            Rule.AVOID_HAVING_SINGLE_DEVELOPER,
                            IssueType.LOADING,
                            Severity.MINOR,
                            (a, c) -> c.getName().equals(component.getName())));
                    }

                    return commitsFuture
                        .map(commits -> getIssues(commits, repository.getComponentName()))
                        .map(List::stream)
                        .get();
                }));

        return master
            .collect(toUnmodifiableList());
    }

    private List<Issue> getIssues(List<Commit> commits, String componentName) {
        final List<Commit> relevantCommits = commits.stream()
            .filter(IGNORE_BOT_COMMITS)
            .collect(toUnmodifiableList());

        final List<CommitAuthor> authors = relevantCommits.stream()
            .map(Commit::getAuthor)
            .distinct()
            .collect(toUnmodifiableList());

        final var commitsByAuthor = authors.stream()
            .collect(Collectors.toMap(
                author -> author,
                author -> relevantCommits.stream()
                    .filter(commit -> commit.getAuthor().equals(author))
                    .collect(toUnmodifiableList())));

        final double totalCommitCount = relevantCommits.size();

        return commitsByAuthor.entrySet().stream()
            .flatMap(authorCommits -> {
                final double authorCommitCount = authorCommits.getValue().size();
                final double authorCommitPercentage = (authorCommitCount / totalCommitCount) * 100d;

                if (authorCommitPercentage >= percentageThreshold) {
                    return Stream.of(new SingleDeveloperIssue(
                        componentName,
                        authorCommits.getKey(),
                        (int) authorCommitPercentage));
                } else {
                    return Stream.empty();
                }
            })
            .collect(toUnmodifiableList());
    }
}
