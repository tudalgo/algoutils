package org.tudalgo.algoutils.descriptors.generic;

import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.descriptors.Descriptors;
import org.tudalgo.algoutils.descriptors.TypeDescriptor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Basic implementation of {@link TypeVariableDescriptor}.
 *
 * @param name   the name of the type variable
 * @param bounds the bounds of the type variable
 */
// TODO: handle self-referential type variables (e.g., A extends Supplier<A>)
record TypeVariableDescriptorImpl(String name, List<TypeDescriptor> bounds) implements TypeVariableDescriptor {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<TypeDescriptor> getBounds() {
        return bounds.isEmpty() ? Collections.singletonList(Descriptors.forClass(Object.class)) : Collections.unmodifiableList(bounds);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TypeVariableDescriptor that)) return false;
        return Objects.equals(getName(), that.getName()) && Objects.deepEquals(getBounds(), that.getBounds());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getBounds());
    }

    @Override
    public @NonNull String toString() {
        return "TypeVariableDescriptor{name=%s, bounds=%s}".formatted(name,
            getBounds().stream().map(TypeDescriptor::getName).filter(Predicate.not("java.lang.Object"::equals)).collect(Collectors.joining(", ", "[", "]")));
    }
}
