package org.tudalgo.algoutils.descriptors.types;

import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Basic implementation of {@link ClassDescriptor}.
 *
 * @param modifiers      the modifiers of the class
 * @param name           the name of the class
 * @param typeParameters the type parameters of the class
 * @param superclass     the superclass of the class
 * @param interfaces     the interfaces implemented by the class
 */
record ClassDescriptorImpl(
    int modifiers,
    String name,
    Supplier<TypeVariableDescriptor[]> typeParameters,
    Supplier<TypeDescriptor> superclass,
    Supplier<TypeDescriptor[]> interfaces
) implements ClassDescriptor {

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TypeVariableDescriptor[] getTypeParameters() {
        return typeParameters.get();
    }

    @Override
    public TypeDescriptor getSuperclass() {
        return superclass.get();
    }

    @Override
    public TypeDescriptor[] getInterfaces() {
        return interfaces.get();
    }

    @Override
    public Class<?> reflect() {
        try {
            return Class.forName(getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassDescriptor that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public @NonNull String toString() {
        return "ClassDescriptor{modifiers=%d, name=%s, typeParameters=%s, superclass=%s, interfaces=%s}".formatted(
            getModifiers(), getName(), getTypeParameters(), getSuperclass(), getInterfaces()
        );
    }
}
