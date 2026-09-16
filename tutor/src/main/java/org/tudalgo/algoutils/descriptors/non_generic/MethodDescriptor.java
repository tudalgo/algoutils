package org.tudalgo.algoutils.descriptors.non_generic;

import java.util.Arrays;
import java.util.List;

/**
 * Descriptor for methods declared in a type.
 * Methods are defined by their declaring class, name and their parameter types.
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.Method Method}.
 */
public interface MethodDescriptor extends ExecutableDescriptor {

    /**
     * Creates a new {@link MethodDescriptor} with the given attributes.
     *
     * @param declaringType  the declaring type of the method
     * @param name           the name of the method
     * @param parameterTypes the parameter types of the method
     * @return the new {@link MethodDescriptor}
     */
    static MethodDescriptor of(ClassDescriptor declaringType, String name, ClassDescriptor... parameterTypes) {
        return new MethodDescriptorImpl(declaringType, name, Arrays.asList(parameterTypes));
    }

    /**
     * Alternative implementation for {@link MethodDescriptor} to indicate that a method could not be found.
     */
    final class NotFound implements MethodDescriptor, org.tudalgo.algoutils.descriptors.NotFound {

        private final ClassDescriptor declaringType;
        private final String name;
        private final List<ClassDescriptor> parameterTypes;

        public NotFound(ClassDescriptor declaringType, String name, ClassDescriptor... parameterTypes) {
            this.declaringType = declaringType;
            this.name = name;
            this.parameterTypes = Arrays.asList(parameterTypes);
        }

        @Override
        public ClassDescriptor getDeclaringType() {
            return declaringType;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<ClassDescriptor> getParameterTypes() {
            return parameterTypes;
        }
    }
}
