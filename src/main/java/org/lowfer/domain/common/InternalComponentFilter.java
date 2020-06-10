package org.lowfer.domain.common;

public final class InternalComponentFilter implements SoftwareComponentFilter {

    private final boolean apply;

    public InternalComponentFilter(boolean apply) {
        this.apply = apply;
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return !component.isExternal() || !apply;
    }

    @Override
    public boolean aggregate(SoftwareArchitecture architecture, SoftwareComponent component) {
        return false;
    }
}
