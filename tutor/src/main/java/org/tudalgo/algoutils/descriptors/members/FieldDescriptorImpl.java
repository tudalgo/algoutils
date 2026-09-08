package org.tudalgo.algoutils.descriptors.members;

import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.descriptors.types.TypeDescriptor;

import java.util.Objects;
import java.util.function.Supplier;

record FieldDescriptorImpl(
    Supplier<TypeDescriptor> declaringType,
    int modifiers,
    Supplier<TypeDescriptor> type,
    String name
) implements FieldDescriptor {

    @Override
    public TypeDescriptor getDeclaringType() {
        return declaringType.get();
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TypeDescriptor getType() {
        return type.get();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FieldDescriptor that)) return false;
        return Objects.equals(getDeclaringType(), that.getDeclaringType()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeclaringType(), getName());
    }

    @Override
    public @NonNull String toString() {
        return "FieldDescriptor{declaringType=%s, modifiers=%d, type=%s, name=%s}".formatted(
            getDeclaringType(), getModifiers(), getType(), getName()
        );
    }
}
