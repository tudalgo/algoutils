package org.tudalgo.algoutils.tutor.general.reflections;

import org.tudalgo.algoutils.tutor.general.match.Identifiable;

import java.lang.reflect.Field;

/**
 * <p>An interface used to simply access fields.</p>
 *
 * @author Dustin Glaser
 */
public interface FieldLink extends Link, Identifiable {

    /**
     * <p>Returns the object assigned to the type of this field.</p>
     * <p>This field is required to be an class field.</p>
     *
     * @return the object
     */
    <T> T get();

    /**
     * <p>Returns the object assigned to the given instance.</p>
     * <p>This field is required to be an instance field.</p>
     *
     * @param instance the instance
     * @return the object
     */
    <T> T get(Object instance);

    /**
     * Returns the actual {@linkplain Field field} behind this field link.
     *
     * @return the field
     */
    Field link();

    /**
     * <p>Sets the object assigned to the type of this field.</p>
     * <p>This field is required to be an class field.</p>
     *
     * @param object the object
     */
    void set(Object object);

    /**
     * <p>Sets the object assigned to the given instance.</p>
     * <p>This field is required to be an instance field.</p>
     *
     * @param instance the instance
     * @param object   the object
     */
    void set(Object instance, Object object);
}
