package org.tudalgo.algoutils.tutor.general.test.actual;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;

import static java.lang.String.format;

/**
 * <p>A type representing the expected exceptional behavior in a test.</p>
 *
 * @param <T> the type of the expected exception
 * @author Dustin Glaser
 */
public interface ActualException<T extends Throwable> extends ActualObject<T> {

    /**
     * A function enclosing an already enclosed type with default text.
     */
    Function<String, String> DEFAULT_FORMATTER = s -> format("exception of type %s", s);

    /**
     * <p>Returns an instance representing behavior where the given exception was thrown or,
     * if no exception is given, an behavior where no exception was thrown.</p>
     *
     * @param exception the exception
     * @param formatter the formatter
     * @param <T>       the type of the exception
     * @return the exceptional behavior
     */
    static <T extends Throwable> ActualException<T> of(T exception, Function<String, String> formatter) {
        //noinspection DuplicatedCode
        return new ActualException<>() {

            @Override
            public T behavior() {
                return exception;
            }

            @Override
            public String string(Stringifier stringifier) {
                return formatter.apply(BRACKET_FORMATTER.apply(stringifier.stringify(this.behavior())));
            }
        };
    }

    /**
     * <p>Returns an instance representing behavior where the given exception was thrown or
     * an behavior where no exception was thrown if no exception is given.</p>
     *
     * @param exception the exception
     * @param <T>       the type of the exception
     * @return the exceptional behavior
     */
    static <T extends Throwable> ActualException<T> of(T exception) {
        return of(exception, DEFAULT_FORMATTER);
    }

    /**
     * Returns the exception thrown in the test.
     *
     * @return the exception
     */
    default T exception() {
        return behavior();
    }


}
