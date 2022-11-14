package org.tudalgo.algoutils.tutor.general.assertions.expected;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>A type representing the expected object in a test.</p>
 *
 * @param <T> the type of the expected object
 * @author Dustin Glaser
 */
public interface ExpectedObject<T> extends Expected {

    /**
     * <p>Returns an expected behavior where the expected object is described with the given object.</p>
     *
     * <p>If a given object is as expected is only tested by the given test.</p>
     *
     * @param object    the object describing the expected behavior
     * @param test      the test to determine if the given object is as expected
     * @param formatter the formatter
     * @param <T>       the type of the expected object
     * @return the expected behavior
     */
    static <T> ExpectedObject<T> of(
        T object,
        Predicate<T> test,
        Function<String, String> formatter
    ) {

        return new ExpectedObject<>() {

            @Override
            public Object behavior() {
                return object;
            }

            @Override
            public String string(Stringifier stringifier) {
                return formatter.apply(BRACKET_FORMATTER.apply(stringifier.stringify(object)));
            }

            @Override
            public boolean test(T object) {
                return test.test(object);
            }

            @Override
            public boolean display() {
                return formatter != null;
            }
        };
    }

    /**
     * <p>Returns an expected behavior where the expected object is described with the given object.</p>
     *
     * <p>If a given object is as expected is only tested by the given test.</p>
     *
     * @param object the object describing the expected behavior
     * @param test   the test to determine if the given object is as expected
     * @param <T>    the type of the expected object
     * @return the expected behavior
     */
    static <T> ExpectedObject<T> of(
        T object,
        Predicate<T> test
    ) {
        return of(object, test, null);
    }

    /**
     * <p>Returns true if the given object is as expected.</p>
     *
     * @param object the object
     * @return true if the given object is as expected
     */
    boolean test(T object);
}
