package org.tudalgo.algoutils.descriptors.non_generic;

import org.tudalgo.algoutils.descriptors.Descriptor;

/**
 * Descriptor for members declared in a type, such as fields, constructors and methods.
 */
public interface MemberDescriptor extends Descriptor {

    /**
     * Returns the type that declares the described member.
     *
     * @return the declaring type of the described member
     */
    ClassDescriptor getDeclaringType();

    /**
     * Returns the name of the described member.
     *
     * @return the name of the described member
     */
    String getName();
}
