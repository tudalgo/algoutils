package org.tudalgo.algoutils.descriptors.types;

import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Basic implementation of {@link GenericArrayTypeDescriptor}.
 *
 * @param componentType the component type of the generic array
 */
record GenericArrayTypeDescriptorImpl(Supplier<TypeDescriptor> componentType) implements GenericArrayTypeDescriptor {

    @Override
    public String getName() {
        return "%s[]".formatted(getGenericComponentType().getName());
    }

    @Override
    public TypeDescriptor getGenericComponentType() {
        return componentType.get();
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
