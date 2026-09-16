package org.tudalgo.algoutils.descriptors.non_generic;

import java.util.Objects;

/**
 * Basic implementation of {@link PackageDescriptor}.
 *
 * @param name the package name
 */
record PackageDescriptorImpl(String name) implements PackageDescriptor {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PackageDescriptor that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
