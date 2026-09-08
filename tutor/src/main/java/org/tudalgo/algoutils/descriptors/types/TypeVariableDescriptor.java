package org.tudalgo.algoutils.descriptors.types;

import java.util.function.Supplier;

/**
 * Descriptor for type variables.
 * Type variables are defined by their name and optionally their (upper) bounds.
 *
 * <p>
 * In the code snippet {@code class X<A, B extends Number, C extends Number & Comparable<? extends Number>>},
 * {@code A}, {@code B} and {@code C} are type variables.
 * {@code A} is unbounded (implicitly {@link Object}).
 * {@code B} is bounded by {@link Number}.
 * {@code C} is bounded by both {@link Number} and {@link Comparable}.
 * </p>
 *
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.TypeVariable TypeVariable}.
 * </p>
 */
public interface TypeVariableDescriptor extends TypeDescriptor {

    /**
     * Returns the (upper) bounds for this descriptor.
     *
     * @return the bounds for this descriptor
     */
    TypeDescriptor[] getBounds();

    @Override
    default boolean isGeneric() {
        return true;
    }

    /**
     * Creates a new {@link TypeVariableDescriptor} with the given name and bounds.
     *
     * @param name   the name of the type variable
     * @param bounds the bounds of the type variable
     * @return the new {@link TypeVariableDescriptor}
     */
    static TypeVariableDescriptor of(String name, TypeDescriptor[] bounds) {
        return of(name, () -> bounds);
    }

    /**
     * Creates a new {@link TypeVariableDescriptor} with the given name and bounds.
     * Lazy variant of {@link #of(String, TypeDescriptor[])}.
     *
     * @param name   the name of the type variable
     * @param bounds a supplier for the bounds of the type variable
     * @return the new {@link TypeVariableDescriptor}
     */
    static TypeVariableDescriptor of(String name, Supplier<TypeDescriptor[]> bounds) {
        return new TypeVariableDescriptorImpl(name, bounds);
    }
}
