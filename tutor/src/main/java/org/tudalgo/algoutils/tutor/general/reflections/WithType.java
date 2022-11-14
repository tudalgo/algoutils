package org.tudalgo.algoutils.tutor.general.reflections;

/**
 * <p>An interface for links to types with a type.</p>
 *
 * @author Dustin Glaser
 */
public interface WithType extends Link {

    /**
     * <p>Returns the type of this link.</p>
     *
     * @return the type
     */
    TypeLink type();
}
