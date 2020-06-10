package org.lowfer.domain.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class ArchitectureTooBigToRenderException extends Exception {

    public ArchitectureTooBigToRenderException(int componentCount) {
        super(String.format("Add filters to render this architecture (%d components is too much to render)", componentCount));
    }
}
