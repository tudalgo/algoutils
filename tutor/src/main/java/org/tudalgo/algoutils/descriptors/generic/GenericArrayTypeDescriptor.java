package org.tudalgo.algoutils.descriptors.generic;

import org.tudalgo.algoutils.descriptors.TypeDescriptor;

/**
 * Descriptor for generic array types.
 * Generic array types are defined by their component type.
 * <p>
 * In the code snippet {@code public <T> void m(T[] ts)}, {@code T[]} is a generic array type.
 * {@code T} is the component type of that generic array.
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.GenericArrayType GenericArrayType}.
 */
public interface GenericArrayTypeDescriptor extends GenericTypeDescriptor {

    /**
     * Returns the generic component type.
     *
     * @return the generic component type
     */
    TypeDescriptor getGenericComponentType();

    /**
     * Creates a new {@link GenericArrayTypeDescriptor} with the given component type.
     *
     * @param componentType the component type of the generic array type
     * @return the new {@link GenericArrayTypeDescriptor}
     */
    static GenericArrayTypeDescriptor of(TypeDescriptor componentType) {
        return new GenericArrayTypeDescriptorImpl(componentType);
    }
}
