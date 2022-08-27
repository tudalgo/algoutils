package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type consisting of a <i>key</i> and a <i>value</i>.
 *
 * @author Dustin Glaser
 */
public interface Property {

    /**
     * Returns the key of the property.
     *
     * @return the key of the property
     */
    String key();

    /**
     * Returns the value of the property.
     *
     * @return the value of the property
     */
    Object value();
}
