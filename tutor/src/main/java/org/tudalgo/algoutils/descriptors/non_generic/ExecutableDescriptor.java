package org.tudalgo.algoutils.descriptors.non_generic;

import java.util.List;

/**
 * Descriptor for executable members such as constructors and methods.
 */
public interface ExecutableDescriptor extends MemberDescriptor {

    /**
     * Returns the parameter types of the described executable.
     *
     * @return the parameter types of the described executable
     */
    List<ClassDescriptor> getParameterTypes();
}
