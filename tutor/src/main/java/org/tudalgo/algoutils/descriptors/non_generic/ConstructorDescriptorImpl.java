package org.tudalgo.algoutils.descriptors.non_generic;

import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Basic implementation of {@link ConstructorDescriptor}.
 *
 * @param declaringType  the declaring type of the constructor
 * @param parameterTypes the constructor's parameter types
 */
record ConstructorDescriptorImpl(ClassDescriptor declaringType, List<ClassDescriptor> parameterTypes) implements ConstructorDescriptor {

    @Override
    public ClassDescriptor getDeclaringType() {
        return declaringType;
    }

    @Override
    public List<ClassDescriptor> getParameterTypes() {
        return Collections.unmodifiableList(parameterTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConstructorDescriptor that)) return false;
        return Objects.equals(getDeclaringType(), that.getDeclaringType()) && Objects.equals(getParameterTypes(), that.getParameterTypes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeclaringType(), getParameterTypes());
    }

    @Override
    public @NonNull String toString() {
        return "ConstructorDescriptor{declaringType=%s, parameterTypes=%s}".formatted(getDeclaringType(), getParameterTypes());
    }
}
