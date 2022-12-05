package org.tudalgo.algoutils.tutor.general.reflections;

import java.lang.reflect.Field;

/**
 * <p>An interface used to simply access fields.</p>
 *
 * @author Dustin Glaser
 */
public interface FieldLink extends Link, WithModifiers, WithName, WithType {
    /**
     * <p>Returns the object assigned to the type of this field.</p>
     * <p>This field is required to be a class field.</p>
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
     * @deprecated use {@linkplain #reflection()} instead
     */
    @Deprecated
    default Field link() {
        return reflection();
    }

    /**
     * <p>Returns the type of the field linked by this field link.</p>
     *
     * @return the type
     */
    @Override
    Field reflection();

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

    @Override
    default Kind kind() {
        return Kind.FIELD;
    }

    @Override
    default int modifiers() {
        return link().getModifiers();
    }

    TypeLink staticType();

    @Override
    default TypeLink type() {
        return staticType();
    }
}
