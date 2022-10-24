package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.assertions.Property;

import java.util.Objects;

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

    /**
     * <p>Returns true iff the object is a property with the same key. </p>
     *
     * @param o the reference object with which to compare.
     * @return true iff the object is a property with the same key
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicProperty that = (BasicProperty) o;
        return Objects.equals(key, that.key);
    }
}
