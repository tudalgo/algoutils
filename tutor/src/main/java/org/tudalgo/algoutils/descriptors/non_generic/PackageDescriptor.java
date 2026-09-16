package org.tudalgo.algoutils.descriptors.non_generic;

import org.tudalgo.algoutils.descriptors.Descriptor;

/**
 * Descriptor for packages.
 * Packages are defined by their fully-qualified name.
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.Package Package}.
 */
public interface PackageDescriptor extends Descriptor {

    /**
     * Returns the name / path of the package.
     *
     * @return the name / path of the package
     */
    String getName();

    /**
     * Creates a {@link PackageDescriptor} with the given name.
     *
     * @param packageName the package name
     * @return the new {@link PackageDescriptor}
     */
    static PackageDescriptor of(String packageName) {
        return new PackageDescriptorImpl(packageName);
    }

    /**
     * Alternative implementation for {@link PackageDescriptor} to indicate that a package could not be found.
     */
    @SuppressWarnings("ClassCanBeRecord")
    final class NotFound implements PackageDescriptor, org.tudalgo.algoutils.descriptors.NotFound {

        private final String name;

        public NotFound(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
