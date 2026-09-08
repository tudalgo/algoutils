package org.tudalgo.algoutils.descriptors.types;

import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.descriptors.Descriptors;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Basic implementation of {@link TypeVariableDescriptor}.
 *
 * @param name   the name of the type variable
 * @param bounds the bounds of the type variable
 */
record TypeVariableDescriptorImpl(String name, Supplier<TypeDescriptor[]> bounds) implements TypeVariableDescriptor {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TypeDescriptor[] getBounds() {
        TypeDescriptor[] bounds = this.bounds.get();
        return bounds.length != 0 ? bounds : new TypeDescriptor[] {Descriptors.forClass(Object.class)};
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TypeVariableDescriptor that)) return false;
        return Objects.equals(getName(), that.getName()) && Objects.deepEquals(getBounds(), that.getBounds());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, Arrays.hashCode(getBounds()));
    }

    @Override
    public @NonNull String toString() {
        return "TypeVariableDescriptor{name=%s, bounds=%s}".formatted(name,
            Arrays.stream(getBounds()).map(TypeDescriptor::getName).filter(Predicate.not("java.lang.Object"::equals)).collect(Collectors.joining(", ", "[", "]")));
    }
}
