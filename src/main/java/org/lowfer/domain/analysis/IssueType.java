package org.lowfer.domain.analysis;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum IssueType {
    @JsonProperty MAINTENANCE,
    @JsonProperty SCALING,
    @JsonProperty SECURITY,
}
