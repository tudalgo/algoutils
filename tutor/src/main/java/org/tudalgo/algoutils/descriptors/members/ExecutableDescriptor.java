package org.tudalgo.algoutils.descriptors.members;

import org.tudalgo.algoutils.descriptors.WithTypeParameters;
import org.tudalgo.algoutils.descriptors.types.TypeDescriptor;

/**
 * Descriptor for executable members such as constructors and methods.
 */
public interface ExecutableDescriptor extends MemberDescriptor, WithTypeParameters {

    /**
     * Returns the parameter types of the described executable.
     *
     * @return the parameter types of the described executable
     */
    TypeDescriptor[] getParameterTypes();

    /**
     * Returns the exception types declared by the described executable.
     *
     * @return the exception types declared by the described executable
     */
    TypeDescriptor[] getExceptionTypes();
}
