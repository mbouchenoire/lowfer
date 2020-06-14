package org.lowfer.domain.common;

import org.eclipse.jgit.lib.PersonIdent;

import java.util.Objects;

public final class CommitAuthor {

    private final String name;
    private final String email;

    public CommitAuthor(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public CommitAuthor(PersonIdent personIdent) {
        this(personIdent.getName(), personIdent.getEmailAddress());
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommitAuthor commitAuthor = (CommitAuthor) o;
        return Objects.equals(email, commitAuthor.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return email;
    }
}
