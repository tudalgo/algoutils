package org.tudalgo.algoutils.descriptors.members;

import org.tudalgo.algoutils.descriptors.Descriptors;
import org.tudalgo.algoutils.descriptors.types.TypeDescriptor;
import org.tudalgo.algoutils.descriptors.types.TypeVariableDescriptor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Descriptor for constructors declared in a type.
 */
public interface ConstructorDescriptor extends ExecutableDescriptor {

    @Override
    default String getName() {
        return "<init>";
    }

    /**
     * Creates a new {@link ConstructorDescriptor} with the given attributes.
     *
     * @param declaringType  the declaring type of the constructor
     * @param modifiers      the modifiers of the constructor
     * @param typeParameters the type parameters of the constructor
     * @param parameterTypes the parameter types of the constructor
     * @param exceptionTypes the exception types declared by the constructor
     * @return the new {@link ConstructorDescriptor}
     */
    static ConstructorDescriptor of(TypeDescriptor declaringType,
                                    int modifiers,
                                    TypeVariableDescriptor[] typeParameters,
                                    TypeDescriptor[] parameterTypes,
                                    TypeDescriptor[] exceptionTypes) {
        return of(() -> declaringType, modifiers, () -> typeParameters, () -> parameterTypes, () -> exceptionTypes);
    }

    /**
     * Creates a new {@link ConstructorDescriptor} with the given attributes.
     *
     * @param declaringType  supplier for the declaring type of the constructor
     * @param modifiers      the modifiers of the constructor
     * @param typeParameters supplier for the type parameters of the constructor
     * @param parameterTypes supplier for the parameter types of the constructor
     * @param exceptionTypes supplier for the exception types declared by the constructor
     * @return the new {@link ConstructorDescriptor}
     */
    static ConstructorDescriptor of(Supplier<TypeDescriptor> declaringType,
                                    int modifiers,
                                    Supplier<TypeVariableDescriptor[]> typeParameters,
                                    Supplier<TypeDescriptor[]> parameterTypes,
                                    Supplier<TypeDescriptor[]> exceptionTypes) {
        return new ConstructorDescriptorImpl(declaringType, modifiers, typeParameters, parameterTypes, exceptionTypes);
    }

    /**
     * Creates a new builder for a constructor descriptor.
     *
     * @param declaringType  the declaring type of the constructor
     * @param parameterTypes the parameter types of the constructor
     * @return a builder for the constructor descriptor
     */
    static Builder builder(TypeDescriptor declaringType, TypeDescriptor... parameterTypes) {
        return new Builder(declaringType, parameterTypes);
    }

    /**
     * Builder for {@link ConstructorDescriptor} instances.
     */
    class Builder {

        private final TypeDescriptor declaringType;
        private int modifiers = 0;
        private final List<TypeVariableDescriptor> typeParameters = new ArrayList<>();
        private final List<TypeDescriptor> parameterTypes;
        private final List<TypeDescriptor> exceptions = new ArrayList<>();

        /**
         * Creates a new builder for a constructor descriptor.
         *
         * @param declaringType  the declaring type of the constructor
         * @param parameterTypes the parameter types of the constructor
         */
        public Builder(TypeDescriptor declaringType, TypeDescriptor... parameterTypes) {
            this.declaringType = declaringType;
            this.parameterTypes = List.of(parameterTypes);
        }

        /**
         * Sets the modifiers of the constructor.
         *
         * @param modifiers the modifiers of the constructor
         * @return this builder
         */
        public Builder setModifiers(int modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        /**
         * Adds a type variable to the constructor.
         *
         * @param typeVariableDescriptor the type variable to add
         * @return this builder
         */
        public Builder addTypeVariable(TypeVariableDescriptor typeVariableDescriptor) {
            typeParameters.add(typeVariableDescriptor);
            return this;
        }

        /**
         * Adds a type variable to the constructor.
         *
         * @param name   the name of the type variable
         * @param bounds the bounds of the type variable
         * @return this builder
         */
        public Builder addTypeVariable(String name, TypeDescriptor... bounds) {
            return addTypeVariable(TypeVariableDescriptor.of(name, bounds));
        }

        /**
         * Adds an exception type to the constructor.
         *
         * @param typeDescriptor the exception type to add
         * @return this builder
         */
        public Builder addException(TypeDescriptor typeDescriptor) {
            exceptions.add(typeDescriptor);
            return this;
        }

        /**
         * Adds an exception type to the constructor.
         *
         * @param type the Java reflection exception type to add
         * @return this builder
         */
        public Builder addException(Type type) {
            return addException(Descriptors.forType(type));
        }

        /**
         * Builds the constructor descriptor.
         *
         * @return the built constructor descriptor
         */
        public ConstructorDescriptor build() {
            return new ConstructorDescriptorImpl(() -> declaringType,
                modifiers,
                () -> typeParameters.toArray(TypeVariableDescriptor[]::new),
                () -> parameterTypes.toArray(TypeDescriptor[]::new),
                () -> exceptions.toArray(TypeDescriptor[]::new));
        }
    }
}
