package org.tudalgo.algoutils.descriptors.members;

import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.descriptors.types.TypeDescriptor;
import org.tudalgo.algoutils.descriptors.types.TypeVariableDescriptor;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

record MethodDescriptorImpl(
    Supplier<TypeDescriptor> declaringType,
    int modifiers,
    Supplier<TypeVariableDescriptor[]> typeParameters,
    Supplier<TypeDescriptor> returnType,
    String name,
    Supplier<TypeDescriptor[]> parameterTypes,
    Supplier<TypeDescriptor[]> exceptionTypes
) implements MethodDescriptor {

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
    public TypeDescriptor getReturnType() {
        return returnType.get();
    }

    @Override
    public String getName() {
        return name;
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
        if (!(o instanceof MethodDescriptor that)) return false;
        return Objects.equals(getDeclaringType(), that.getDeclaringType()) &&
            Objects.equals(getName(), that.getName()) &&
            Objects.deepEquals(getParameterTypes(), that.getParameterTypes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeclaringType(), getName(), Arrays.hashCode(getParameterTypes()));
    }

    @Override
    public @NonNull String toString() {
        return "MethodDescriptor{declaringType=%s, modifiers=%d, typeParameters=%s, returnType=%s, name=%s, parameterTypes=%s, exceptionTypes=%s}".formatted(
            getDeclaringType(), getModifiers(), getTypeParameters(), getReturnType(), getName(), getParameterTypes(), getExceptionTypes()
        );
    }
}
