package org.tudalgo.algoutils.tutor.general.assertions.expected;

import java.util.Objects;

import static java.lang.String.format;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObject.of;

/**
 * <p>A collection of methods for building {@linkplain ExpectedObject expected objects}.</p>
 *
 * @author Dustin Glaser
 */
public final class ExpectedObjects {

    // no instantiation allowed
    private ExpectedObjects() {

    }

    /**
     * <p>Returns an <i>expected</i> where the actual result is not relevant.</p>
     *
     * @param <T> the type of the expected object
     * @return the <i>expected</i>
     */
    public static <T> ExpectedObject<T> something() {
        return of(null, o -> true);
    }

    /**
     * <p>Returns an expected object where the object is expected to be equal to the actual object.</p>
     *
     * @param object the expected object
     * @param <T>    the type of the expected object
     * @return the expected behavior
     */
    public static <T> ExpectedObject<T> equalTo(T object) {
        return of(object, o -> Objects.equals(object, o));
    }

    /**
     * <p>Returns an expected object where the object is expected to be {@link Boolean#FALSE}</p>
     *
     * @return the expected behavior
     */
    public static ExpectedObject<Boolean> equalsFalse() {
        return of(false, b -> !b);
    }

    /**
     * <p>Returns an expected object where the object is expected to be null.</p>
     *
     * @return the expected behavior
     */
    public static <T> ExpectedObject<T> equalsNull() {
        return of(null, Objects::isNull);
    }

    /**
     * <p>Returns an expected object where the object is expected to be {@link Boolean#TRUE}</p>
     *
     * @return the expected behavior
     */
    public static ExpectedObject<Boolean> equalsTrue() {
        return of(true, b -> b);
    }

    /**
     * <p>Returns an expected object where the object is expected be an instance of the given type.</p>
     *
     * @param type     the expected type
     * @param subtypes if the actual object can be a subtype of the expected type
     * @param <T>      the type of the expected object
     * @return the expected behavior
     */
    public static <T> ExpectedObject<Class<T>> instanceOf(Class<T> type, boolean subtypes) {
        if (subtypes) {
            return of(type, type::isAssignableFrom);
        }
        return of(type, t -> type == t);
    }

    /**
     * <p>Returns an expected object where the object is not null.</p>
     *
     * @param <T> the type of the expected object
     * @return the expected behavior
     */
    public static <T> ExpectedObject<T> notEqualsNull() {
        return notEqualsTo(null);
    }

    /**
     * <p>Returns an expected object where the object is expected to be not equal to the given object.</p>
     *
     * @param object the unexpected object
     * @param <T>    the type of the unexpected object
     * @return the expected behavior
     */
    public static <T> ExpectedObject<T> notEqualsTo(T object) {
        return of(object, o -> !Objects.equals(object, o), s -> format("not %s", s));
    }

    /**
     * <p>Returns an expected object where the object is expected to be the same as the given object.</p>
     *
     * @return the expected behavior
     */
    public static <T> ExpectedObject<T> notSameAs(T object) {
        return of(object, o -> o != object, s -> format("not %s", s));
    }

    /**
     * <p>Returns an expected object where the object is expected to be the same as the given object.</p>
     *
     * @return the expected behavior
     */
    public static <T> ExpectedObject<T> sameAs(T object) {
        return of(object, o -> o == object);
    }
}
