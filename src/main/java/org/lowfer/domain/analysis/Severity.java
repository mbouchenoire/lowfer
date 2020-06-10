package org.lowfer.domain.analysis;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Severity {
    @JsonProperty("BLOCKER")
    BLOCKER,
    @JsonProperty("CRITICAL")
    CRITICAL,
    @JsonProperty("MAJOR")
    MAJOR,
    @JsonProperty("MINOR")
    MINOR
}
