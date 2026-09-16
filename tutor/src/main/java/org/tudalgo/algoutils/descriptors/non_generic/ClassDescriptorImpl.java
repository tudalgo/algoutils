package org.tudalgo.algoutils.descriptors.non_generic;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Basic implementation of {@link ClassDescriptor}.
 *
 * @param name           the name of the class
 */
record ClassDescriptorImpl(String name) implements ClassDescriptor {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassDescriptor that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public @NonNull String toString() {
        return "ClassDescriptor{name=%s}".formatted(getName());
    }
}
