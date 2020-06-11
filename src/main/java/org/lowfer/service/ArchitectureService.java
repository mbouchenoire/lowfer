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

package org.lowfer.service;

import io.vavr.control.Try;
import org.lowfer.domain.analysis.Rule;
import org.lowfer.domain.common.ArchitectureTransformer;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.common.SoftwareComponentFilter;
import org.lowfer.domain.error.ArchitectureNotFoundException;
import org.lowfer.repository.StaticArchitectureRepository;
import org.lowfer.serde.ManifestEncoder;
import org.lowfer.serde.ManifestSerializer;
import org.lowfer.web.rest.vm.IssueView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vavr.control.Try.failure;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.lowfer.web.rest.vm.EncodedArchitectureView.error;
import static org.lowfer.web.rest.vm.EncodedArchitectureView.ok;

@Service
public class ArchitectureService {

    private static final Logger LOG = LoggerFactory.getLogger(ArchitectureService.class);

    private final StaticArchitectureRepository staticArchitectureRepository;
    private final ArchitectureTransformer architectureTransformer;
    private final ManifestSerializer manifestSerializer;
    private final ManifestEncoder manifestEncoder;

    public ArchitectureService(
        StaticArchitectureRepository staticArchitectureRepository,
        ArchitectureTransformer architectureTransformer,
        ManifestSerializer manifestSerializer,
        ManifestEncoder manifestEncoder) {

        this.staticArchitectureRepository = staticArchitectureRepository;
        this.architectureTransformer = architectureTransformer;
        this.manifestSerializer = manifestSerializer;
        this.manifestEncoder = manifestEncoder;
    }

    public List<SoftwareArchitecture> findAll() {
        return staticArchitectureRepository.findAll();
    }

    public Try<SoftwareArchitecture> load(String name, String encoded) {
        if (isNotBlank(name) && isNotBlank(encoded))
            throw new IllegalArgumentException("Cannot load architecture when given both name and encoded representation");

        if (isNotBlank(name)) {
            LOG.debug("Finding static architecture with name='{}'...", name);
            return staticArchitectureRepository.findByName(name)
                .map(Try::success)
                .orElse(failure(new ArchitectureNotFoundException(name)));
        }

        if (isNotBlank(encoded)) {
            LOG.debug("Loading architecture with provided encoded manifest of length={}...", encoded.length());
            return manifestSerializer.deserializeManifest(manifestEncoder.decode(encoded));
        }

        throw new IllegalArgumentException("Cannot load architecture without name or encoded representation");
    }

    public List<IssueView> findIssues(SoftwareArchitecture architecture, SoftwareComponentFilter filter) {
        LOG.debug("Finding issues of architecture '{}' ({} components)", architecture.getName(), architecture.getComponents().size());

        final List<IssueView> issues = Rule.findAllIssues(architecture).stream()
            .flatMap(issue -> {
                final SoftwareArchitecture issueArchitecture = architectureTransformer.filter(architecture, issue);

                final boolean issueIsRelevant = issueArchitecture.getComponents().stream()
                    .anyMatch(component -> filter.test(architecture, component));

                if (!issueIsRelevant)
                    return Stream.empty();

                final IssueView issueView = manifestSerializer.serializeManifest(issueArchitecture)
                    .map(manifestEncoder::encode)
                    .map(encodedManifest -> new IssueView(issue, ok(encodedManifest)))
                    .getOrElseGet(throwable -> new IssueView(issue, error(throwable)));

                return Stream.of(issueView);
            })
            .collect(Collectors.toList());

        LOG.info("Found {} issues for architecture '{}' ({} components)", issues.size(), architecture.getName(), architecture.getComponents().size());

        return issues;
    }
}
