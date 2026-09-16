package org.tudalgo.algoutils.descriptors.generic;

import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.descriptors.TypeDescriptor;
import org.tudalgo.algoutils.descriptors.non_generic.ClassDescriptor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Basic implementation of {@link ParameterizedTypeDescriptor}.
 *
 * @param rawType       the raw type of the parameterized type
 * @param typeArguments the type arguments of the parameterized type
 */
record ParameterizedTypeDescriptorImpl(ClassDescriptor rawType, List<TypeDescriptor> typeArguments) implements ParameterizedTypeDescriptor {

    @Override
    public String getName() {
        return "%s<%s>".formatted(getRawType().getName(), getTypeArguments().stream().map(TypeDescriptor::getName).collect(Collectors.joining(", ")));
    }

    @Override
    public ClassDescriptor getRawType() {
        return rawType;
    }

    @Override
    public List<TypeDescriptor> getTypeArguments() {
        return Collections.unmodifiableList(typeArguments);
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
