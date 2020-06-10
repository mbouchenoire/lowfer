package org.lowfer.domain.common;

public interface SoftwareComponentFilter {

    boolean test(SoftwareArchitecture architecture, SoftwareComponent component);

    // TODO ?
    default boolean include(SoftwareArchitecture architecture, SoftwareComponent component) {
        return true;
    }

    // TODO ?
    default boolean aggregate(SoftwareArchitecture architecture, SoftwareComponent component) {
        return true;
    }
}
