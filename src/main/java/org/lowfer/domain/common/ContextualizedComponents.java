package org.lowfer.domain.common;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class ContextualizedComponents {

    private final Set<SoftwareComponent> components;
    private final String context;

    public ContextualizedComponents(Set<SoftwareComponent> components, @Nullable String context) {
        this.components = components;
        this.context = context;
    }

    public Set<SoftwareComponent> getComponents() {
        return components;
    }

    public Optional<String> getContext() {
        return Optional.ofNullable(context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContextualizedComponents that = (ContextualizedComponents) o;
        return Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }
}
