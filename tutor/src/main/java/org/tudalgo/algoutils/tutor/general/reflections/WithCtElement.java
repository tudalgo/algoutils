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

    /**
     * Returns if an {@linkplain CtElement} is available for the element behind this link.
     * In general, this is the case for all solution's elements.
     *
     * @return if an {@linkplain CtElement} is available
     */
    boolean isCtElementAvailable();
}
