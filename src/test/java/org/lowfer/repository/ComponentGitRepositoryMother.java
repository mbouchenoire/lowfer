package org.lowfer.repository;

import org.assertj.core.util.Files;

import java.nio.file.Path;

public final class ComponentGitRepositoryMother {

    private ComponentGitRepositoryMother() {
    }

    public static AsyncComponentGitRepository helloWorld() {
        final Path temp = Files.newTemporaryFolder().toPath();

        return new ComponentGitRepository(
            "HelloWorld",
            "https://github.com/octocat/Hello-World.git",
            "",
            "",
            temp);
    }
}
