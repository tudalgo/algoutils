package org.tudalgo.algoutils.descriptors.non_generic;

import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

record MethodDescriptorImpl(ClassDescriptor declaringType, String name, List<ClassDescriptor> parameterTypes) implements MethodDescriptor {

    @Override
    public ClassDescriptor getDeclaringType() {
        return declaringType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<ClassDescriptor> getParameterTypes() {
        return Collections.unmodifiableList(parameterTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MethodDescriptor that)) return false;
        return Objects.equals(getDeclaringType(), that.getDeclaringType()) &&
            Objects.equals(getName(), that.getName()) &&
            Objects.equals(getParameterTypes(), that.getParameterTypes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeclaringType(), getName(), getParameterTypes());
    }

    @Override
    public @NonNull String toString() {
        return "MethodDescriptor{declaringType=%s, name=%s, parameterTypes=%s}".formatted(getDeclaringType(), getName(), getParameterTypes());
    }
}
