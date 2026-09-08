package org.tudalgo.algoutils.descriptors;

import org.tudalgo.algoutils.descriptors.types.TypeVariableDescriptor;

/**
 * Descriptor for entities that expose type parameters.
 */
public interface WithTypeParameters {

    /**
     * Returns the type parameters of the described entity.
     *
     * @return the type parameters of the described entity
     */
    TypeVariableDescriptor[] getTypeParameters();
}
