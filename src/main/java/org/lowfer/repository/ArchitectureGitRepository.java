package org.lowfer.repository;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.serde.MasterManifestDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(prefix = "architectures.", value = "repository.uri")
public class ArchitectureGitRepository implements ArchitectureRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ArchitectureGitRepository.class);

    private final ArchitectureDirectoryRepository architectureDirectoryRepository;

    public ArchitectureGitRepository(
        MasterManifestDeserializer masterManifestDeserializer,
        GitHostConfigRepository gitHostConfigRepository,
        @Value("${architectures.repository.uri}") String uri,
        @Value("${architectures.repository.branch:master}") String branch,
        @Value("${architectures.repository.path:/}") String path)
        throws IOException, GitAPIException, URISyntaxException {

        final File directory = Files.createTempDirectory("lowfer-versioned-architectures-").toFile();
        LOG.info("Created temp directory ({}) for repository: {}", directory, uri);

        final GitHostConfig gitHostConfig = gitHostConfigRepository.fromUri(uri);
        final GitPullConfig gitPullConfig = new GitPullConfig(uri, branch, gitHostConfig, directory);

        GitUtils.pull(gitPullConfig);

        final Path fullPath = Path.of(directory.getPath(), path);

        this.architectureDirectoryRepository =
            new ArchitectureDirectoryRepository(fullPath.toString(), masterManifestDeserializer);
    }

    @Override
    public Optional<SoftwareArchitecture> findByName(String name) {
        return architectureDirectoryRepository.findByName(name);
    }

    @Override
    public List<SoftwareArchitecture> findAll() {
        return architectureDirectoryRepository.findAll();
    }
}
