package org.lowfer.repository;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.serde.MasterManifestDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(prefix = "application.architectures.", value = "repository.uri")
public class ArchitectureGitRepository implements ArchitectureRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ArchitectureGitRepository.class);

    private final Git repository;
    private final ArchitectureDirectoryRepository architectureDirectoryRepository;

    public ArchitectureGitRepository(
        MasterManifestDeserializer masterManifestDeserializer,
        @Value("${application.architectures.repository.uri}") String uri,
        @Value("${application.architectures.repository.branch:master}") String branch,
        @Value("${application.architectures.repository.path:/}") String path,
        @Value("${application.architectures.repository.username:}") String username,
        @Value("${application.architectures.repository.password:}") String password) throws IOException, GitAPIException {

        final File directoryFile = Files.createTempDirectory("lowfer-versioned-architectures-").toFile();

        LOG.info("Created temp directory ({}) for repository: {}", directoryFile, uri);

        LOG.info("Cloning branch {} of git repository: {}...", branch, uri);

        this.repository = Git.cloneRepository()
            .setURI(uri)
            .setDirectory(directoryFile)
            .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
            .setBranch(branch)
            .call();

        LOG.info("Cloned git repository ({}) with {} branch(es)", uri, repository.branchList().call().size());

        final Path fullPath = Path.of(directoryFile.getPath(), path);

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
