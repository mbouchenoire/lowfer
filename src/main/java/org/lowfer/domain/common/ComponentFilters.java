/*
 * Copyright 2020 the original author or authors from the Lowfer project.
 *
 * This file is part of the Lowfer project, see https://github.com/mbouchenoire/lowfer
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
