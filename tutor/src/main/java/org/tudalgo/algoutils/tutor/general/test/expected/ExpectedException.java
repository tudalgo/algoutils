package org.tudalgo.algoutils.tutor.general.test.expected;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>A type representing the expected exceptional behavior in a test.</p>
 *
 * @param <T> the type of the expected exception
 * @author Dustin Glaser
 */
public interface ExpectedException<T extends Throwable> extends ExpectedObject<Class<T>> {

    /**
     * <p>Returns an instance representing an expected exceptional behavior.</p>
     *
     * @param clazz         the type representing the expected exception
     * @param classTest     a predicate testing if a given class is of the expected type
     * @param exceptionTest a predicate testing if a given exception is of the expected type
     * @param formatter     the formatter
     * @param <T>           the type of the expected exception
     * @return the expected exceptional behavior
     */
    static <T extends Exception> ExpectedException<T> of(
        Class<T> clazz,
        Predicate<Class<T>> classTest,
        Predicate<T> exceptionTest,
        Function<String, String> formatter
    ) {

        return new ExpectedException<>() {

            @Override
            public Object behavior() {
                return clazz;
            }

            @Override
            public String string(Stringifier stringifier) {
                return formatter.apply(BRACKET_FORMATTER.apply(stringifier.stringify(clazz)));
            }

            @Override
            public boolean test(T exception) {
                return exceptionTest.test(exception);
            }

            @Override
            public boolean test(Class<T> object) {
                return classTest.test(object);
            }
        };
    }

    @Override
    default String string(Stringifier stringifier) {
        return String.format("exception of type %s", BRACKET_FORMATTER.apply(stringifier.stringify(this)));
    }

    /**
     * Returns if the given type is of the expected type.
     *
     * @param object the type to test
     * @return if the given type is of the expected type
     */
    boolean test(Class<T> object);

    /**
     * Returns if the given exception is as expected.
     *
     * @param exception the exception to test
     * @return if the given exception is of the expected
     */
    boolean test(T exception);
}
