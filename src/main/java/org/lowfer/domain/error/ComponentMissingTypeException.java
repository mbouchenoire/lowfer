package org.lowfer.domain.error;

public final class ComponentMissingTypeException extends ArchitectureParsingException {

    public ComponentMissingTypeException(String componentName) {
        super(String.format("Component '%s' is missing attribute 'type'", componentName));
    }
}
