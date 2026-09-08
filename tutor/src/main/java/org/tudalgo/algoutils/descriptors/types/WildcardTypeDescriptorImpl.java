package org.tudalgo.algoutils.descriptors.types;

import org.jspecify.annotations.NonNull;
import org.tudalgo.algoutils.descriptors.Descriptors;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Basic implementation of {@link WildcardTypeDescriptor}.
 *
 * @param boundsType  the type of bounds
 * @param lowerBounds the lower bounds
 * @param upperBounds the upper bounds
 */
record WildcardTypeDescriptorImpl(
    BoundsType boundsType,
    Supplier<TypeDescriptor[]> lowerBounds,
    Supplier<TypeDescriptor[]> upperBounds
) implements WildcardTypeDescriptor {

    @Override
    public String getName() {
        return switch (boundsType) {
            case NONE -> "?";
            case LOWER -> "? super " + getLowerBounds()[0].getName();
            case UPPER -> "? extends " + getUpperBounds()[0].getName();
        };
    }

    @Override
    public TypeDescriptor[] getLowerBounds() {
        return lowerBounds.get();
    }

    @Override
    public TypeDescriptor[] getUpperBounds() {
        TypeDescriptor[] upperBounds = this.upperBounds.get();
        return upperBounds.length != 0 ? upperBounds : new TypeDescriptor[] {Descriptors.forClass(Object.class)};
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
