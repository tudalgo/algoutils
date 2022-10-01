package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.assertions.Property;

/**
 * <p>A basic implementation of a property. </p>
 *
 * @param key   the key of the property
 * @param value the value of the property
 */
public record BasicProperty(
    String key,
    Object value
) implements Property {
}
