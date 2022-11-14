package org.tudalgo.algoutils.tutor.general.reflections;

/**
 * <p>An interface for links to types with modifiers.</p>
 *
 * @author Dustin Glaser
 */
public interface WithModifiers extends Link {

    /**
     * <p>Return the bit representation of the modifiers of this link.</p>
     *
     * @return the modifiers
     */
    int modifiers();
}
