package org.tudalgo.algoutils.descriptors.types;

import org.tudalgo.algoutils.descriptors.Descriptors;
import org.tudalgo.algoutils.descriptors.WithModifiers;
import org.tudalgo.algoutils.descriptors.WithTypeParameters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Descriptor for class types (i.e., abstract and non-abstract classes, interfaces, enum classes, records and non-generic array types).
 * Class types are defined by their fully qualified name.
 *
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.Class Class}.
 * </p>
 */
public interface ClassDescriptor extends TypeDescriptor, WithModifiers, WithTypeParameters {

    /**
     * Returns the superclass of this class type.
     *
     * @return the superclass of this class type
     */
    TypeDescriptor getSuperclass();

    /**
     * Returns the interfaces of this class type.
     *
     * @return the interfaces of this class type
     */
    TypeDescriptor[] getInterfaces();

    /**
     * Returns the Java reflection object for this descriptor.
     *
     * @return the Java reflection object for this descriptor
     */
    Class<?> reflect();

    @Override
    default boolean isGeneric() {
        return false;
    }

    /**
     * Creates a new {@link ClassDescriptor} with the given attributes.
     *
     * @param modifiers      the modifiers of the class
     * @param name           the name of the class
     * @param typeParameters the type parameters of the class
     * @param superclass     the superclass of the class
     * @param interfaces     the interfaces implemented by the class
     * @return the new {@link ClassDescriptor}
     */
    static ClassDescriptor of(int modifiers,
                              String name,
                              TypeVariableDescriptor[] typeParameters,
                              TypeDescriptor superclass,
                              TypeDescriptor[] interfaces) {
        return of(modifiers, name, () -> typeParameters, () -> superclass, () -> interfaces);
    }

    /**
     * Creates a new {@link ClassDescriptor} with the given attributes.
     *
     * @param modifiers      the modifiers of the class
     * @param name           the name of the class
     * @param typeParameters the type parameters of the class
     * @param superclass     the superclass of the class
     * @param interfaces     the interfaces implemented by the class
     * @return the new {@link ClassDescriptor}
     */
    static ClassDescriptor of(int modifiers,
                              String name,
                              Supplier<TypeVariableDescriptor[]> typeParameters,
                              Supplier<TypeDescriptor> superclass,
                              Supplier<TypeDescriptor[]> interfaces) {
        return new ClassDescriptorImpl(modifiers, name, typeParameters, superclass, interfaces);
    }

    /**
     * Creates a new builder for a class descriptor.
     *
     * @param name the name of the class
     * @return a builder for the class descriptor
     */
    static Builder builder(String name) {
        return new Builder(name);
    }

    /**
     * Builder for {@link ClassDescriptor} instances.
     */
    class Builder {

        private int modifiers = 0;
        private final String name;
        private final List<TypeVariableDescriptor> typeParameters = new ArrayList<>();
        private TypeDescriptor superclass = null;
        private final List<TypeDescriptor> interfaces = new ArrayList<>();

        /**
         * Creates a new builder for a class descriptor.
         *
         * @param name the name of the class
         */
        public Builder(String name) {
            this.name = name;
        }

        /**
         * Sets the modifiers of the class.
         *
         * @param modifiers the modifiers of the class
         * @return this builder
         */
        public Builder setModifiers(int modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        /**
         * Adds a type variable to the class.
         *
         * @param typeVariableDescriptor the type variable to add
         * @return this builder
         */
        public Builder addTypeVariable(TypeVariableDescriptor typeVariableDescriptor) {
            typeParameters.add(typeVariableDescriptor);
            return this;
        }

        /**
         * Adds a type variable to the class.
         *
         * @param name the name of the type variable
         * @param bounds the bounds of the type variable
         * @return this builder
         */
        public Builder addTypeVariable(String name, TypeDescriptor... bounds) {
            return addTypeVariable(new TypeVariableDescriptorImpl(name, () -> bounds));
        }

        /**
         * Sets the superclass of the class.
         *
         * @param typeDescriptor the superclass of the class
         * @return this builder
         */
        public Builder setSuperclass(TypeDescriptor typeDescriptor) {
            superclass = typeDescriptor;
            return this;
        }

        /**
         * Sets the superclass of the class.
         *
         * @param type the Java reflection superclass of the class
         * @return this builder
         */
        public Builder setSuperclass(Type type) {
            return setSuperclass(Descriptors.forType(type));
        }

        /**
         * Adds an interface to the class.
         *
         * @param typeDescriptor the interface to add
         * @return this builder
         */
        public Builder addInterface(TypeDescriptor typeDescriptor) {
            interfaces.add(typeDescriptor);
            return this;
        }

        /**
         * Adds an interface to the class.
         *
         * @param type the Java reflection interface type to add
         * @return this builder
         */
        public Builder addInterface(Type type) {
            return addInterface(Descriptors.forType(type));
        }

        /**
         * Builds the class descriptor.
         *
         * @return the built class descriptor
         */
        public ClassDescriptor build() {
            return new ClassDescriptorImpl(modifiers,
                name,
                () -> typeParameters.toArray(TypeVariableDescriptor[]::new),
                () -> superclass,
                () -> interfaces.toArray(TypeDescriptor[]::new));
        }
    }
}
