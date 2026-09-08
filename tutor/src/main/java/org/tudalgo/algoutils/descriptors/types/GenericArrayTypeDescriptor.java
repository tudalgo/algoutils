package org.tudalgo.algoutils.descriptors.types;

import java.util.function.Supplier;

/**
 * Descriptor for generic array types.
 * Generic array types are defined by their component type.
 *
 * <p>
 * In the code snippet {@code public <T> void m(T[] ts)}, {@code T[]} is a generic array type.
 * {@code T} is the component type of that generic array.
 * </p>
 *
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.GenericArrayType GenericArrayType}.
 * </p>
 */
public interface GenericArrayTypeDescriptor extends TypeDescriptor {

    /**
     * Returns the generic component type.
     *
     * @return the generic component type
     */
    TypeDescriptor getGenericComponentType();

    @Override
    default boolean isGeneric() {
        return true;
    }

    /**
     * Creates a new {@link GenericArrayTypeDescriptor} with the given component type.
     *
     * @param componentType the component type of the generic array type
     * @return the new {@link GenericArrayTypeDescriptor}
     */
    static GenericArrayTypeDescriptor of(TypeDescriptor componentType) {
        return of(() -> componentType);
    }

    /**
     * Creates a new {@link GenericArrayTypeDescriptor} with the given component type.
     *
     * @param componentType supplier for the component type of the generic array type
     * @return the new {@link GenericArrayTypeDescriptor}
     */
    static GenericArrayTypeDescriptor of(Supplier<TypeDescriptor> componentType) {
        return new GenericArrayTypeDescriptorImpl(componentType);
    }
}
