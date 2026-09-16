package org.tudalgo.algoutils.descriptors.non_generic;

import org.tudalgo.algoutils.descriptors.TypeDescriptor;

/**
 * Descriptor for class types (i.e., abstract and non-abstract classes, interfaces, enum classes, records and non-generic array types).
 * Class types are defined by their fully qualified name.
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.Class Class}.
 */
public interface ClassDescriptor extends TypeDescriptor {

    /**
     * Creates a new {@link ClassDescriptor} with the given fully-qualified name.
     *
     * @param name the name of the class
     * @return the new {@link ClassDescriptor}
     */
    static ClassDescriptor of(String name) {
        return new ClassDescriptorImpl(name);
    }

    /**
     * Alternative implementation for {@link ClassDescriptor} to indicate that a class could not be found.
     */
    @SuppressWarnings("ClassCanBeRecord")
    final class NotFound implements ClassDescriptor, org.tudalgo.algoutils.descriptors.NotFound {

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
