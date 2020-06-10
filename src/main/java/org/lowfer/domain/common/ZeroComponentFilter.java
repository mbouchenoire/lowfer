package org.lowfer.domain.common;

public final class ZeroComponentFilter implements SoftwareComponentFilter {

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return false;
    }
}
