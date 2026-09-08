package org.tudalgo.algoutils.descriptors.members;

import org.tudalgo.algoutils.descriptors.WithModifiers;
import org.tudalgo.algoutils.descriptors.types.TypeDescriptor;

/**
 * Descriptor for members declared in a type, such as fields, constructors and methods.
 */
public interface MemberDescriptor extends WithModifiers {

    /**
     * Returns the type that declares the described member.
     *
     * @return the declaring type of the described member
     */
    TypeDescriptor getDeclaringType();

    /**
     * Returns the name of the described member.
     *
     * @return the name of the described member
     */
    String getName();
}
