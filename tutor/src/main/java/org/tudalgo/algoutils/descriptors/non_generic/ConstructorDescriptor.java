package org.tudalgo.algoutils.descriptors.non_generic;


import java.util.Arrays;
import java.util.List;

/**
 * Descriptor for constructors of class types.
 * Constructors are defined by their declaring class and their parameter types.
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.Constructor Constructor}.
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
     * @param parameterTypes the parameter types of the constructor
     * @return the new {@link ConstructorDescriptor}
     */
    static ConstructorDescriptor of(ClassDescriptor declaringType, ClassDescriptor... parameterTypes) {
        return new ConstructorDescriptorImpl(declaringType, Arrays.asList(parameterTypes));
    }

    /**
     * Alternative implementation for {@link ConstructorDescriptor} to indicate that a constructor could not be found.
     */
    final class NotFound implements ConstructorDescriptor, org.tudalgo.algoutils.descriptors.NotFound {

        private final ClassDescriptor declaringType;
        private final List<ClassDescriptor> parameterTypes;

        public NotFound(ClassDescriptor declaringType, ClassDescriptor... parameterTypes) {
            this.declaringType = declaringType;
            this.parameterTypes = Arrays.asList(parameterTypes);
        }

        @Override
        public ClassDescriptor getDeclaringType() {
            return declaringType;
        }

        @Override
        public List<ClassDescriptor> getParameterTypes() {
            return parameterTypes;
        }
    }
}
