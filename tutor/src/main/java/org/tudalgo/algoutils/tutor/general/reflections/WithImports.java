package org.tudalgo.algoutils.tutor.general.reflections;

import java.util.Set;

/**
 * An interface for links to types with imports.
 *
 * @author Nhan Huynh
 */
public interface WithImports extends Link {

    /**
     * Returns the set of imports of this link.
     *
     * @param recursive whether to expand the retrieval of imports recursively
     *
     * @return the set of imports of this link
     */
    Set<TypeLink> imports(boolean recursive);

    /**
     * Return all imports used in this link.
     *
     * @return the imports
     */
    default Set<TypeLink> imports() {
        return imports(true);
    }

}
