package org.tudalgo.algoutils.tutor.general.test.expected;

import java.util.Objects;

import static java.lang.String.format;
import static org.tudalgo.algoutils.tutor.general.test.expected.ExpectedObject.of;

public final class ExpectedObjects {

    private ExpectedObjects() {

    }

    public static <T> ExpectedObject<T> equalTo(T object) {
        return of(object, o -> Objects.equals(object, o));
    }

    public static ExpectedObject<Boolean> equalsFalse() {
        return of(false, b -> !b);
    }

    public static <T> ExpectedObject<T> equalsNull() {
        return of(null, Objects::isNull);
    }

    public static ExpectedObject<Boolean> equalsTrue() {
        return of(true, b -> b);
    }

    public static <T> ExpectedObject<Class<T>> instanceOf(Class<T> clazz) {
        return of(clazz, c -> c == clazz);
    }

    public static <T> ExpectedObject<T> notEqualsNull() {
        return notEqualsTo(null);
    }

    public static <T> ExpectedObject<T> notEqualsTo(T object) {
        return of(object, o -> !Objects.equals(object, o), s -> format("not %s", s));
    }

    public static <T> ExpectedObject<T> notSameAs(T object) {
        return of(object, o -> o != object, s -> format("not %s", s));
    }

    public static <T> ExpectedObject<T> sameAs(T object) {
        return of(object, o -> o == object);
    }
}
