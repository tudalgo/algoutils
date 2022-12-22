package org.tudalgo.algoutils.tutor.general.reflections;

import java.util.Set;

/**
 * An interface for links to types with imports.
 *
 * @author Dustin Glaser
 */
public interface WithImports extends Link {

    /**
     * Return all imports used in this link.
     *
     * @return the imports
     */
    Set<TypeLink> imports();

}
