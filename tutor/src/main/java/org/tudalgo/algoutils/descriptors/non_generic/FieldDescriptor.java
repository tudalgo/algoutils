package org.tudalgo.algoutils.descriptors.non_generic;

/**
 * Descriptor for fields declared in a type.
 * Fields are defined by their declaring class and their name.
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.Field Field}.
 */
public interface FieldDescriptor extends MemberDescriptor {

    /**
     * Creates a new {@link FieldDescriptor} with the given attributes.
     *
     * @param declaringType the declaring type of the field
     * @param name          the name of the field
     * @return the new {@link FieldDescriptor}
     */
    static FieldDescriptor of(ClassDescriptor declaringType, String name) {
        return new FieldDescriptorImpl(declaringType, name);
    }

    /**
     * Alternative implementation for {@link FieldDescriptor} to indicate that a field could not be found.
     */
    @SuppressWarnings("ClassCanBeRecord")
    final class NotFound implements FieldDescriptor, org.tudalgo.algoutils.descriptors.NotFound {

        private final ClassDescriptor declaringType;
        private final String name;

        public NotFound(ClassDescriptor declaringType, String name) {
            this.declaringType = declaringType;
            this.name = name;
        }

        @Override
        public ClassDescriptor getDeclaringType() {
            return declaringType;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
