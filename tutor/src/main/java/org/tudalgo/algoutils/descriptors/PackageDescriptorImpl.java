package org.tudalgo.algoutils.descriptors;

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
}
