package org.tudalgo.algoutils.tutor.general.match;

public class BasicMatchers {

    public static <T> Matcher<T> sameObject(T object) {
        return Matcher.of(o -> o == object);
    }

    public static <T> Matcher<T> equalObject(T object) {
        return Matcher.of(o -> o.equals(object));
    }
}
