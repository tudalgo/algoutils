package org.tudalgo.algoutils.descriptors.generic;

import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.descriptors.Descriptors;
import org.tudalgo.algoutils.descriptors.TypeDescriptor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Basic implementation of {@link WildcardTypeDescriptor}.
 *
 * @param boundsType  the type of bounds
 * @param lowerBounds the lower bounds
 * @param upperBounds the upper bounds
 */
record WildcardTypeDescriptorImpl(BoundsType boundsType, List<TypeDescriptor> lowerBounds, List<TypeDescriptor> upperBounds) implements WildcardTypeDescriptor {

    @Override
    public String getName() {
        return switch (boundsType) {
            case NONE -> "?";
            case LOWER -> "? super " + getLowerBounds().getFirst().getName();
            case UPPER -> "? extends " + getUpperBounds().getFirst().getName();
        };
    }

    @Override
    public List<TypeDescriptor> getLowerBounds() {
        return Collections.unmodifiableList(lowerBounds);
    }

    @Override
    public List<TypeDescriptor> getUpperBounds() {
        return upperBounds.isEmpty() ? Collections.singletonList(Descriptors.forClass(Object.class)) : Collections.unmodifiableList(upperBounds);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WildcardTypeDescriptor that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public @NonNull String toString() {
        return "WildcardTypeDescriptor{name=%s, lowerBounds=%s, upperBounds=%s}".formatted(getName(), getLowerBounds(), getUpperBounds());
    }
}
