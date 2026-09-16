package org.tudalgo.algoutils.descriptors.generic;

import org.tudalgo.algoutils.descriptors.TypeDescriptor;
import org.tudalgo.algoutils.descriptors.non_generic.ClassDescriptor;

import java.util.Arrays;
import java.util.List;

/**
 * Descriptor for parameterized types.
 * Parameterized types are defined by their raw type and type arguments.
 * <p>
 * In the code snippet {@code public void m(List<String> lst, Supplier<? extends String> supplier)},
 * {@code List<String>} and {@code Supplier<? extends String>} are parameterized types.
 * {@code List} is the raw type, and {@code String} is the type argument for {@code lst}.
 * For {@code supplier}, {@code Supplier} is the raw type, and {@code ? extends String} is the type argument.
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.ParameterizedType ParameterizedType}.
 */
public interface ParameterizedTypeDescriptor extends GenericTypeDescriptor {

    /**
     * Returns a descriptor for the raw type.
     *
     * @return a descriptor for the raw type.
     */
    ClassDescriptor getRawType();

    /**
     * Returns the type arguments of the parameterized type.
     *
     * @return the type arguments of the parameterized type
     */
    List<TypeDescriptor> getTypeArguments();

    /**
     * Creates a new {@link ParameterizedTypeDescriptor} with the given raw type and type arguments.
     *
     * @param rawType       the raw type of the parameterized type
     * @param typeArguments the type arguments of the parameterized type
     * @return the new {@link ParameterizedTypeDescriptor}
     */
    static ParameterizedTypeDescriptor of(ClassDescriptor rawType, TypeDescriptor... typeArguments) {
        return new ParameterizedTypeDescriptorImpl(rawType, Arrays.asList(typeArguments));
    }
}
