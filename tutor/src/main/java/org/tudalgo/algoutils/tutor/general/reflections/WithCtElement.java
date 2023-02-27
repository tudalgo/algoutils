package org.tudalgo.algoutils.tutor.general.reflections;

import spoon.reflect.declaration.CtElement;

/**
 * A link with an {@linkplain CtElement}.
 */
public interface WithCtElement extends Link {

    /**
     * Returns the {@linkplain CtElement} associated with the element behind this link.
     *
     * @return the {@linkplain CtElement}
     */
    CtElement getCtElement();
}
