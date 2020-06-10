package org.lowfer.domain.error;

public final class DependencyComponentNotFound extends ArchitectureParsingException {

    public DependencyComponentNotFound(String componentName, String dependencyName) {
        super(String.format("Component '%s' does not exist (dependency of '%s')", dependencyName, componentName));
    }
}
