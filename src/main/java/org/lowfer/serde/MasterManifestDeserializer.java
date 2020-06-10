package org.lowfer.serde;

import io.vavr.control.Try;
import org.lowfer.domain.common.SoftwareArchitecture;

import java.io.File;

public interface MasterManifestDeserializer {

    Try<SoftwareArchitecture> deserializeMasterManifest(File masterManifest);
}
