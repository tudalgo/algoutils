package org.tudalgo.algoutils.descriptors;

/**
 * Descriptor for entities that expose Java modifiers.
 */
public interface WithModifiers {

    /**
     * Returns the Java modifiers of the described entity.
     *
     * @return the Java modifiers of the described entity
     */
    int getModifiers();
}
