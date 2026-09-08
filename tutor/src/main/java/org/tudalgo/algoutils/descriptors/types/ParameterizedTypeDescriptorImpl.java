package org.tudalgo.algoutils.descriptors.types;

import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Basic implementation of {@link ParameterizedTypeDescriptor}.
 *
 * @param rawType       the raw type of the parameterized type
 * @param typeArguments the type arguments of the parameterized type
 */
record ParameterizedTypeDescriptorImpl(
    Supplier<TypeDescriptor> rawType,
    Supplier<TypeDescriptor[]> typeArguments
) implements ParameterizedTypeDescriptor {

    @Override
    public String getName() {
        return "%s<%s>".formatted(getRawType().getName(), Arrays.stream(getTypeArguments()).map(TypeDescriptor::getName).collect(Collectors.joining(", ")));
    }

    @Override
    public TypeDescriptor getRawType() {
        return rawType.get();
    }

    @Override
    public TypeDescriptor[] getTypeArguments() {
        return typeArguments.get();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ParameterizedTypeDescriptor that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public @NonNull String toString() {
        return "ParameterizedTypeDescriptor{name=%s, rawType=%s, typeArguments=%s}".formatted(getName(), getRawType(), getTypeArguments());
    }
}
