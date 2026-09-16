package org.tudalgo.algoutils.descriptors.non_generic;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

record FieldDescriptorImpl( ClassDescriptor declaringType, String name) implements FieldDescriptor {

    @Override
    public ClassDescriptor getDeclaringType() {
        return declaringType;
    }

    @Override
    public String getName() {
        return name;
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
        return "FieldDescriptor{declaringType=%s, name=%s}".formatted(getDeclaringType(), getName());
    }
}
