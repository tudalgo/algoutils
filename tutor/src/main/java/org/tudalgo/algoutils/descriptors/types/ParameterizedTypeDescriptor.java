package org.tudalgo.algoutils.descriptors.types;

import java.util.function.Supplier;

/**
 * Descriptor for parameterized types.
 * Parameterized types are defined by their raw type and type arguments.
 *
 * <p>
 * In the code snippet {@code public void m(List<String> lst, Supplier<? extends String> supplier)},
 * {@code List<String>} and {@code Supplier<? extends String>} are parameterized types.
 * {@code List} is the raw type, and {@code String} is the type argument for {@code lst}.
 * For {@code supplier}, {@code Supplier} is the raw type, and {@code ? extends String} is the type argument.
 * </p>
 *
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.ParameterizedType ParameterizedType}.
 * </p>
 */
public interface ParameterizedTypeDescriptor extends TypeDescriptor {

    /**
     * Returns a descriptor for the raw type.
     *
     * @return a descriptor for the raw type.
     */
    TypeDescriptor getRawType();

    /**
     * Returns the type arguments of the parameterized type.
     *
     * @return the type arguments of the parameterized type
     */
    TypeDescriptor[] getTypeArguments();

    @Override
    default boolean isGeneric() {
        return true;
    }

    /**
     * Creates a new {@link ParameterizedTypeDescriptor} with the given raw type and type arguments.
     *
     * @param rawType       the raw type of the parameterized type
     * @param typeArguments the type arguments of the parameterized type
     * @return the new {@link ParameterizedTypeDescriptor}
     */
    static ParameterizedTypeDescriptor of(TypeDescriptor rawType,
                                          TypeDescriptor[] typeArguments) {
        return of(() -> rawType, () -> typeArguments);
    }

    /**
     * Creates a new {@link ParameterizedTypeDescriptor} with the given raw type and type arguments.
     *
     * @param rawType       supplier for the raw type of the parameterized type
     * @param typeArguments supplier for the type arguments of the parameterized type
     * @return the new {@link ParameterizedTypeDescriptor}
     */
    static ParameterizedTypeDescriptor of(Supplier<TypeDescriptor> rawType,
                                          Supplier<TypeDescriptor[]> typeArguments) {
        return new ParameterizedTypeDescriptorImpl(rawType, typeArguments);
    }
}
