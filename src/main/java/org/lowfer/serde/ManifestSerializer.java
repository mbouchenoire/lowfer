package org.lowfer.serde;

import io.vavr.control.Try;
import org.lowfer.domain.common.SoftwareArchitecture;

import java.io.File;

public interface ManifestSerializer {

    Try<SoftwareArchitecture> deserializeManifest(String manifest);
    Try<SoftwareArchitecture> deserializeManifest(File manifest);

    Try<String> serializeManifest(SoftwareArchitecture architecture);
}
