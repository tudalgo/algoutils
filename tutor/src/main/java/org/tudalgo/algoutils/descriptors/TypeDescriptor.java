package org.tudalgo.algoutils.descriptors;

/**
 * Descriptor for all kinds of types, including classes, interfaces, records and generic types like
 * type variables, parameterized types, generic array types and wildcard types.
 *
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.Type Type}.
 * </p>
 */
public interface TypeDescriptor extends Descriptor {

    /**
     * Returns the name of the described type, a string that uniquely identifies the underlying type.
     *
     * @return the name of the described type
     */
    String getName();
}
