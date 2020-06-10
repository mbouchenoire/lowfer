package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareArchitecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.lowfer.domain.common.SoftwareComponentType.LIBRARY;

/**
 * Finds libraries that are unstable (I>0.5).
 * This is not applicable to services / frontends as they are not made to share code.
 */
public final class AvoidUnstableLibraryIssueFinder implements IssueFinder {

    private static final Logger LOG = LoggerFactory.getLogger(AvoidUnstableLibraryIssueFinder.class);

    private static final double UNSTABLE_THRESHOLD = 0.5d;

    @Override
    public List<Issue> find(SoftwareArchitecture architecture) {
        LOG.trace("Finding unstable dependencies (I>{}) (architecture: {})...", UNSTABLE_THRESHOLD, architecture.getName());

        return architecture.getComponents().stream()
            .filter(component -> component.getType() == LIBRARY)
            .filter(component -> !component.isExternal())
            .map(architecture::instability)
            .filter(instability -> instability.getDoubleValue() > UNSTABLE_THRESHOLD)
            .map(UnstableLibrary::new)
            .collect(Collectors.toUnmodifiableList());
    }
}
