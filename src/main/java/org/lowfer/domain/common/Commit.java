package org.lowfer.domain.common;

import org.eclipse.jgit.revwalk.RevCommit;

import java.time.Instant;

public final class Commit {

    private final Instant date;
    private final String message;
    private final CommitAuthor author;

    public Commit(Instant date, String message, CommitAuthor author) {
        this.date = date;
        this.message = message;
        this.author = author;
    }

    public Commit(RevCommit revCommit) {
        this(
            Instant.ofEpochSecond(revCommit.getCommitTime()),
            revCommit.getShortMessage(),
            new CommitAuthor(revCommit.getAuthorIdent()));
    }

    public Instant getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public CommitAuthor getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return date + ": " + message + " (" + author + ")";
    }
}
