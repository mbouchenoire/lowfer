/*
 * Copyright 2020 the original author or authors from the Lowfer project.
 *
 * This file is part of the Lowfer project, see https://github.com/mbouchenoire/lowfer
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lowfer.serde;

import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.lowfer.domain.common.*;
import org.lowfer.domain.error.ComponentMissingTypeException;
import org.lowfer.domain.error.InvalidDependencyTypeException;
import org.lowfer.domain.error.ManifestYamlException;
import org.lowfer.repository.AsyncComponentGitRepository;
import org.lowfer.repository.ComponentGitRepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.scanner.ScannerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Predicates.instanceOf;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.control.Try.failure;

@Component
public class ManifestYamlParser implements ManifestSerializer, MasterManifestDeserializer {

    private static final Logger LOG = LoggerFactory.getLogger(ManifestYamlParser.class);

    private static final Yaml YAML = new Yaml(new Constructor(SoftwareArchitectureYaml.class));
    private static final Yaml MASTER_YAML = new Yaml(new Constructor(Master.class));
    private static final Path COMMON_PATH = Path.of(System.getProperty("java.io.tmpdir"), "lowfer");

    private final ComponentGitRepositoryFactory componentGitRepositoryFactory;

    public ManifestYamlParser(ComponentGitRepositoryFactory componentGitRepositoryFactory) {
        this.componentGitRepositoryFactory = componentGitRepositoryFactory;
    }

    @Override
    public Try<SoftwareArchitecture> deserializeManifest(String manifest) {
        //noinspection unchecked
        return Try.of(() -> (SoftwareArchitectureYaml) YAML.load(manifest))
            .mapFailure(Case($(instanceOf(ScannerException.class)), ManifestYamlException::new))
            .flatMap(yaml -> load(yaml, false));
    }

    @Override
    public Try<SoftwareArchitecture> deserializeManifest(File manifest) {
        return Try.of(() -> Files.readAllBytes(manifest.toPath()))
            .map(String::new)
            .flatMap(this::deserializeManifest);
    }

    @Override
    public Try<String> serializeManifest(SoftwareArchitecture architecture) {
        return Try.of(() -> {
            LOG.trace("Serializing architecture of {} component(s)...", architecture.getComponents().size());
            final String manifest = YAML.dump(new SoftwareArchitectureYaml(architecture));
            LOG.debug("Serialized architecture of {} component(s) to manifest of length={}", architecture.getComponents().size(), manifest.length());
            return manifest;
        });
    }

    @Override
    public Try<SoftwareArchitecture> deserializeMasterManifest(File masterManifest) {
        return Try.of(() -> {
            LOG.debug("Deserializing master manifest from file: {}...", masterManifest);

            final InputStream targetStream = new FileInputStream(masterManifest);
            final Master master = MASTER_YAML.load(targetStream);

            return master.getInclude().stream()
                .map(include -> {
                    LOG.debug("Including manifest file: {}...", include);
                    final File includeFile = include.toFile(masterManifest);
                    return parseFile(includeFile).getOrElseThrow(throwable ->
                        new IllegalArgumentException("Failed to parse architecture included file", throwable));
                })
                .reduce(SoftwareArchitecture::concat)
                .orElseThrow(() -> new IllegalArgumentException("Failed to parse architecture master file"))
                .named(master.getName());
        });
    }

    private Try<SoftwareArchitecture> parseFile(File file) {
        LOG.debug("Parsing manifest file: {}...", file);
        return Try.of(() -> new FileInputStream(file))
            .map(fileInputStream -> (SoftwareArchitectureYaml) YAML.load(fileInputStream))
            .flatMap(yaml -> load(yaml, true));
    }

    private Try<SoftwareArchitecture> load(SoftwareArchitectureYaml architectureYaml, boolean lazy) {
        LOG.debug("Loading architecture from yaml representation (name: {})...", architectureYaml.getName());

        final Set<Try<SoftwareComponent>> componentTries = architectureYaml.getComponents().stream()
            .<Try<SoftwareComponent>>map(componentYaml -> {
                if (StringUtils.isBlank(componentYaml.getType())) {
                    return failure(new ComponentMissingTypeException(componentYaml.getName()));
                }

                final Set<Try<ComponentDependency>> dependencyTries = componentYaml.getDependencies().stream()
                    .map(dependencyYaml -> {
                        final Try<DependencyType> dependencyTypeTry = DependencyType
                            .fromSerializedName(dependencyYaml.getType())
                            .map(Try::success)
                            .orElse(failure(new InvalidDependencyTypeException(componentYaml.getName(), dependencyYaml.getComponent(), dependencyYaml.getType())));

                        return dependencyTypeTry
                            .map(dependencyType -> new ComponentDependency(dependencyYaml.getComponent(), dependencyType));
                    })
                    .collect(Collectors.toSet());

                final Set<Maintainer> maintainers = componentYaml.getMaintainers().stream()
                    .map(maintainerYaml -> {
                        final SemanticUIColor color = SemanticUIColor.randomFromSeed(maintainerYaml.getName());
                        return new Maintainer(maintainerYaml.getName(), color);
                    })
                    .collect(Collectors.toSet());

                final AsyncComponentGitRepository repository = Optional.ofNullable(componentYaml.getRepository())
                    .flatMap(repositoryUri -> getOrcreateComponentDirectory(componentYaml.getName())
                        .map(path -> componentGitRepositoryFactory.create(componentYaml.getName(), repositoryUri, null, path))
                        .map(Optional::of)
                        .getOrElseGet(throwable -> {
                            LOG.error("Failed to load Git repository of component: {}", componentYaml.getName(), throwable);
                            return Optional.empty();
                        }))
                    .orElse(null);

                return Try.sequence(dependencyTries)
                    .map(dependencies -> new SoftwareComponent(
                        componentYaml.getName(),
                        componentYaml.getLabel(),
                        SoftwareComponentType.fromSerializedName(componentYaml.getType()).orElseThrow(),
                        componentYaml.getContext(),
                        repository,
                        maintainers,
                        dependencies.toJavaSet()));
            })
            .collect(Collectors.toSet());

        return Try.sequence(componentTries)
            .flatMap(components -> SoftwareArchitecture.of(architectureYaml.getName(), components.toJavaSet(), lazy));
    }

    private static Try<Path> getOrcreateComponentDirectory(String componentName) {
        final Path componentDirectoryPath = COMMON_PATH.resolve(componentName);

        return Try.of(() -> {
            if (!Files.exists(componentDirectoryPath)) {
                final Path directoryPath = Files.createDirectories(componentDirectoryPath);
                LOG.info("Created directory ({}) for component: {}", directoryPath, componentName);
                return directoryPath;
            }

            return componentDirectoryPath;
        }).onFailure(throwable -> LOG.error("Failed to create directory for component '{}'", componentName, throwable));
    }
}
