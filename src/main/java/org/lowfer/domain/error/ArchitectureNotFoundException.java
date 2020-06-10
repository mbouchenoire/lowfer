package org.lowfer.domain.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ArchitectureNotFoundException extends Exception {

    public ArchitectureNotFoundException(String name) {
        super("Architecture '" + name + "' not found");
    }
}
