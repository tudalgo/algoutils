package org.tudalgo.algoutils.tutor.general.reflections;

import org.tudalgo.algoutils.tutor.general.match.Identifiable;

/**
 * <p>An interface for links to types with a name.</p>
 *
 * @author Dustin Glaser
 */
public interface WithName extends Identifiable, Link {

    /**
     * <p>Returns the name of this link.</p>
     *
     * @return the name
     */
    String name();

    @Override
    default String identifier() {
        return name();
    }
}
