package org.lowfer.domain.error;

import static java.lang.String.format;

public final class InvalidDependencyTypeException extends ArchitectureParsingException {

    public InvalidDependencyTypeException(String componentName, String dependencyName, String dependencyType) {
        super(format("Dependency '%s' of component '%s' has an invalid type ('%s')", dependencyName, componentName, dependencyType));
    }
}
