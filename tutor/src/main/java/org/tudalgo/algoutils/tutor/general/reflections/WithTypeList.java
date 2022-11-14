package org.tudalgo.algoutils.tutor.general.reflections;

import java.util.List;

/**
 * <p>An interface for links to types with a list of types.</p>
 *
 * @author Dustin Glaser
 */
public interface WithTypeList extends Link {

    /**
     * <p>Returns the list of this link.</p>
     *
     * @return the list of types
     */
    List<? extends TypeLink> typeList();
}
