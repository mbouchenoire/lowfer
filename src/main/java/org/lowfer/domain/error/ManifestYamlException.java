package org.lowfer.domain.error;

import org.yaml.snakeyaml.scanner.ScannerException;

public final class ManifestYamlException extends ArchitectureParsingException {

    public ManifestYamlException(ScannerException scannerException) {
        super(scannerException.getMessage());
    }
}
