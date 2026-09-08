package org.tudalgo.algoutils.descriptors;

/**
 * Descriptor for packages.
 * Packages are defined by their name.
 *
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.Package Package}.
 * </p>
 */
public interface PackageDescriptor {

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
}
