package org.tudalgo.algoutils.descriptors.generic;

import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.descriptors.TypeDescriptor;

import java.util.Objects;

/**
 * Basic implementation of {@link GenericArrayTypeDescriptor}.
 *
 * @param componentType the component type of the generic array
 */
record GenericArrayTypeDescriptorImpl(TypeDescriptor componentType) implements GenericArrayTypeDescriptor {

    @Override
    public String getName() {
        return "%s[]".formatted(getGenericComponentType().getName());
    }

    @Override
    public TypeDescriptor getGenericComponentType() {
        return componentType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GenericArrayTypeDescriptor that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public @NonNull String toString() {
        return "GenericArrayTypeDescriptor{name=%s, componentType=%s}".formatted(getName(), getGenericComponentType());
    }
}
