package org.tudalgo.algoutils.descriptors.members;

import org.tudalgo.algoutils.descriptors.Descriptors;
import org.tudalgo.algoutils.descriptors.types.TypeDescriptor;
import org.tudalgo.algoutils.descriptors.types.TypeVariableDescriptor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Descriptor for methods declared in a type.
 */
public interface MethodDescriptor extends ExecutableDescriptor {

    /**
     * Returns the return type of the described method.
     *
     * @return the return type of the described method
     */
    TypeDescriptor getReturnType();

    /**
     * Creates a new {@link MethodDescriptor} with the given attributes.
     *
     * @param declaringType the declaring type of the method
     * @param modifiers the modifiers of the method
     * @param typeParameters the type parameters of the method
     * @param returnType the return type of the method
     * @param name the name of the method
     * @param parameterTypes the parameter types of the method
     * @param exceptionTypes the exception types declared by the method
     * @return the new {@link MethodDescriptor}
     */
    static MethodDescriptor of(TypeDescriptor declaringType,
                               int modifiers,
                               TypeVariableDescriptor[] typeParameters,
                               TypeDescriptor returnType,
                               String name,
                               TypeDescriptor[] parameterTypes,
                               TypeDescriptor[] exceptionTypes) {
        return of(() -> declaringType, modifiers, () -> typeParameters, () -> returnType, name, () -> parameterTypes, () -> exceptionTypes);
    }

    /**
     * Creates a new {@link MethodDescriptor} with the given attributes.
     *
     * @param declaringType supplier for the declaring type of the method
     * @param modifiers the modifiers of the method
     * @param typeParameters supplier for the type parameters of the method
     * @param returnType supplier for the return type of the method
     * @param name the name of the method
     * @param parameterTypes supplier for the parameter types of the method
     * @param exceptionTypes supplier for the exception types declared by the method
     * @return the new {@link MethodDescriptor}
     */
    static MethodDescriptor of(Supplier<TypeDescriptor> declaringType,
                               int modifiers,
                               Supplier<TypeVariableDescriptor[]> typeParameters,
                               Supplier<TypeDescriptor> returnType,
                               String name,
                               Supplier<TypeDescriptor[]> parameterTypes,
                               Supplier<TypeDescriptor[]> exceptionTypes) {
        return new MethodDescriptorImpl(declaringType, modifiers, typeParameters, returnType, name, parameterTypes, exceptionTypes);
    }

    /**
     * Creates a new builder for a method descriptor.
     *
     * @param declaringType the declaring type of the method
     * @param name the name of the method
     * @param parameterTypes the parameter types of the method
     * @return a builder for the method descriptor
     */
    static Builder builder(TypeDescriptor declaringType, String name, TypeDescriptor... parameterTypes) {
        return new Builder(declaringType, name, parameterTypes);
    }

    /**
     * Builder for {@link MethodDescriptor} instances.
     */
    class Builder {

        private final TypeDescriptor declaringType;
        private int modifiers = 0;
        private final List<TypeVariableDescriptor> typeParameters = new ArrayList<>();
        private TypeDescriptor returnType = null;
        private final String name;
        private final List<TypeDescriptor> parameterTypes;
        private final List<TypeDescriptor> exceptions = new ArrayList<>();

        /**
         * Creates a new builder for a method descriptor.
         *
         * @param declaringType the declaring type of the method
         * @param name the name of the method
         * @param parameterTypes the parameter types of the method
         */
        public Builder(TypeDescriptor declaringType, String name, TypeDescriptor... parameterTypes) {
            this.declaringType = declaringType;
            this.name = name;
            this.parameterTypes = List.of(parameterTypes);
        }

        /**
         * Sets the modifiers of the method.
         *
         * @param modifiers the modifiers of the method
         * @return this builder
         */
        public Builder setModifiers(int modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        /**
         * Adds a type variable to the method.
         *
         * @param typeVariableDescriptor the type variable to add
         * @return this builder
         */
        public Builder addTypeVariable(TypeVariableDescriptor typeVariableDescriptor) {
            typeParameters.add(typeVariableDescriptor);
            return this;
        }

        /**
         * Adds a type variable to the method.
         *
         * @param name the name of the type variable
         * @param bounds the bounds of the type variable
         * @return this builder
         */
        public Builder addTypeVariable(String name, TypeDescriptor... bounds) {
            return addTypeVariable(Descriptors.describeTypeVariable(name, bounds));
        }

        /**
         * Sets the return type of the method.
         *
         * @param typeDescriptor the return type of the method
         * @return this builder
         */
        public Builder setReturnType(TypeDescriptor typeDescriptor) {
            this.returnType = typeDescriptor;
            return this;
        }

        /**
         * Sets the return type of the method.
         *
         * @param type the Java reflection return type of the method
         * @return this builder
         */
        public Builder setReturnType(Type type) {
            return setReturnType(Descriptors.forType(type));
        }

        /**
         * Adds an exception type to the method.
         *
         * @param typeDescriptor the exception type to add
         * @return this builder
         */
        public Builder addException(TypeDescriptor typeDescriptor) {
            exceptions.add(typeDescriptor);
            return this;
        }

        /**
         * Adds an exception type to the method.
         *
         * @param type the Java reflection exception type to add
         * @return this builder
         */
        public Builder addException(Type type) {
            return addException(Descriptors.forType(type));
        }

        /**
         * Builds the method descriptor.
         *
         * @return the built method descriptor
         */
        public MethodDescriptor build() {
            return new MethodDescriptorImpl(() -> declaringType,
                modifiers,
                () -> typeParameters.toArray(TypeVariableDescriptor[]::new),
                () -> returnType,
                name,
                () -> parameterTypes.toArray(TypeDescriptor[]::new),
                () -> exceptions.toArray(TypeDescriptor[]::new));
        }
    }
}
