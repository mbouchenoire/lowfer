package org.lowfer.serde;

public interface ManifestEncoder {

    String encode(String manifest);

    String decode(String encoded);
}
