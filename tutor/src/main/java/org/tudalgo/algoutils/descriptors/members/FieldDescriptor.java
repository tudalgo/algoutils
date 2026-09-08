package org.tudalgo.algoutils.descriptors.members;

import org.tudalgo.algoutils.descriptors.Descriptors;
import org.tudalgo.algoutils.descriptors.types.TypeDescriptor;

import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * Descriptor for fields declared in a type.
 */
public interface FieldDescriptor extends MemberDescriptor {

    /**
     * Returns the type of the described field.
     *
     * @return the type of the described field
     */
    TypeDescriptor getType();

    /**
     * Creates a new {@link FieldDescriptor} with the given attributes.
     *
     * @param declaringType the declaring type of the field
     * @param modifiers     the modifiers of the field
     * @param type          the type of the field
     * @param name          the name of the field
     * @return the new {@link FieldDescriptor}
     */
    static FieldDescriptor of(TypeDescriptor declaringType,
                              int modifiers,
                              TypeDescriptor type,
                              String name) {
        return of(() -> declaringType, modifiers, () -> type, name);
    }

    /**
     * Creates a new {@link FieldDescriptor} with the given attributes.
     *
     * @param declaringType supplier for the declaring type of the field
     * @param modifiers     the modifiers of the field
     * @param type          supplier for the type of the field
     * @param name          the name of the field
     * @return the new {@link FieldDescriptor}
     */
    static FieldDescriptor of(Supplier<TypeDescriptor> declaringType,
                              int modifiers,
                              Supplier<TypeDescriptor> type,
                              String name) {
        return new FieldDescriptorImpl(declaringType, modifiers, type, name);
    }

    /**
     * Creates a new builder for a field descriptor.
     *
     * @param declaringType the declaring type of the field
     * @param name          the name of the field
     * @return a builder for the field descriptor
     */
    static Builder builder(TypeDescriptor declaringType, String name) {
        return new Builder(declaringType, name);
    }

    /**
     * Builder for {@link FieldDescriptor} instances.
     */
    class Builder {

        private final TypeDescriptor declaringType;
        private int modifiers = 0;
        private final String name;
        private TypeDescriptor type = null;

        /**
         * Creates a new builder for a field descriptor.
         *
         * @param declaringType the declaring type of the field
         * @param name          the name of the field
         */
        public Builder(TypeDescriptor declaringType, String name) {
            this.declaringType = declaringType;
            this.name = name;
        }

        /**
         * Sets the modifiers of the field.
         *
         * @param modifiers the modifiers of the field
         * @return this builder
         */
        public Builder setModifiers(int modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        /**
         * Sets the type of the field.
         *
         * @param typeDescriptor the type of the field
         * @return this builder
         */
        public Builder setType(TypeDescriptor typeDescriptor) {
            this.type = typeDescriptor;
            return this;
        }

        /**
         * Sets the type of the field.
         *
         * @param type the Java reflection type of the field
         * @return this builder
         */
        public Builder setType(Type type) {
            return setType(Descriptors.forType(type));
        }

        /**
         * Builds the field descriptor.
         *
         * @return the built field descriptor
         */
        public FieldDescriptor build() {
            return new FieldDescriptorImpl(() -> declaringType, modifiers, () -> type, name);
        }
    }
}
