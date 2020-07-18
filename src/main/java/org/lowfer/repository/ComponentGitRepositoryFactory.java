package org.lowfer.repository;

import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.nio.file.Path;

@Component
public class ComponentGitRepositoryFactory {

    private final GitHostConfigRepository gitHostConfigRepository;

    public ComponentGitRepositoryFactory(GitHostConfigRepository gitHostConfigRepository) {
        this.gitHostConfigRepository = gitHostConfigRepository;
    }

    public ComponentGitRepository create(
        String componentName, String uri, @Nullable String branch, Path componentDirectory) {

        final GitHostConfig hostConfig = gitHostConfigRepository.fromUri(uri);

        return new ComponentGitRepository(componentName, uri, branch, hostConfig, componentDirectory);
    }
}
