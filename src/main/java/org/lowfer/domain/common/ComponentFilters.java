package org.lowfer.domain.common;

public final class ComponentFilters implements SoftwareComponentFilter {

    private final ComponentNameFilter nameFilter;
    private final ComponentTypeFilter typeFilter;
    private final ComponentMaintainerFilter maintainerFilter;
    private final InternalComponentFilter internalFilter;

    private ComponentFilters(
        ComponentNameFilter nameFilter,
        ComponentTypeFilter typeFilter,
        ComponentMaintainerFilter maintainerFilter,
        InternalComponentFilter internalFilter) {

        assert nameFilter != null;
        assert typeFilter != null;
        assert maintainerFilter != null;

        this.nameFilter = nameFilter;
        this.typeFilter = typeFilter;
        this.maintainerFilter = maintainerFilter;
        this.internalFilter = internalFilter;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean test(SoftwareArchitecture architecture, SoftwareComponent component) {
        return nameFilter.test(architecture, component)
            && typeFilter.test(architecture, component)
            && maintainerFilter.test(architecture, component)
            && internalFilter.test(architecture, component);
    }

    public static class Builder {

        private ComponentNameFilter nameFilter;
        private ComponentTypeFilter typeFilter;
        private ComponentMaintainerFilter maintainerFilter;
        private InternalComponentFilter internalFilter;

        private Builder() {
            this.nameFilter = ComponentNameFilter.ofText("");
            this.typeFilter = ComponentTypeFilter.ofText("");
            this.maintainerFilter = ComponentMaintainerFilter.ofText("");
            this.internalFilter = new InternalComponentFilter(false);
        }

        public Builder name(String text) {
            this.nameFilter = ComponentNameFilter.ofText(text);
            return this;
        }

        public Builder type(String text) {
            this.typeFilter = ComponentTypeFilter.ofText(text);
            return this;
        }

        public Builder maintainer(String text) {
            this.maintainerFilter = ComponentMaintainerFilter.ofText(text);
            return this;
        }

        public Builder internal(boolean internal) {
            this.internalFilter = new InternalComponentFilter(internal);
            return this;
        }

        public ComponentFilters build() {
            return new ComponentFilters(nameFilter, typeFilter, maintainerFilter, internalFilter);
        }
    }
}
