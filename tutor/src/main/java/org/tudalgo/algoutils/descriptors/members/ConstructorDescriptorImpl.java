package org.tudalgo.algoutils.descriptors.members;

import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.descriptors.*;
import org.tudalgo.algoutils.descriptors.types.TypeDescriptor;
import org.tudalgo.algoutils.descriptors.types.TypeVariableDescriptor;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Basic implementation of {@link ConstructorDescriptor}.
 *
 * @param declaringType  the declaring type of the constructor
 * @param modifiers      the constructor's modifiers
 * @param typeParameters the constructor's type parameters
 * @param parameterTypes the constructor's parameter types
 * @param exceptionTypes the constructor's exception types
 */
record ConstructorDescriptorImpl(
    Supplier<TypeDescriptor> declaringType,
    int modifiers,
    Supplier<TypeVariableDescriptor[]> typeParameters,
    Supplier<TypeDescriptor[]> parameterTypes,
    Supplier<TypeDescriptor[]> exceptionTypes
) implements ConstructorDescriptor {

    @Override
    public TypeDescriptor getDeclaringType() {
        return declaringType.get();
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public TypeVariableDescriptor[] getTypeParameters() {
        return typeParameters.get();
    }

    @Override
    public TypeDescriptor[] getParameterTypes() {
        return parameterTypes.get();
    }

    @Override
    public TypeDescriptor[] getExceptionTypes() {
        return exceptionTypes.get();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConstructorDescriptor that)) return false;
        return Objects.equals(getDeclaringType(), that.getDeclaringType()) && Objects.deepEquals(getParameterTypes(), that.getParameterTypes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeclaringType(), Arrays.hashCode(getParameterTypes()));
    }

    @Override
    public @NonNull String toString() {
        return "ConstructorDescriptor{declaringType=%s, modifiers=%d, typeParameters=%s, parameterTypes=%s, exceptionTypes=%s}".formatted(
            getDeclaringType(), getModifiers(), getTypeParameters(), getParameterTypes(), getExceptionTypes()
        );
    }
}
