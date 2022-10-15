package org.tudalgo.algoutils.tutor.general.assertions.expected;

import static java.lang.String.format;

/**
 * <p>A collection of methods for building {@linkplain ExpectedExceptional expected exceptional behaviors}.</p>
 *
 * @author Dustin Glaser
 */
public final class ExpectedExceptional {

    // no instantiation allowed
    private ExpectedExceptional() {

    }

    /**
     * <p>Returns an expected behavior where an exception of the given type is expected to be thrown.</p>
     *
     * @param type the type of the expected exception
     * @param <T>  the type of the expected exception
     * @return the expected behavior
     */
    public static <T extends Exception> ExpectedException<T> instanceOf(Class<T> type) {
        return ExpectedException.of(
            type,
            c -> c == type,
            t -> true,
            s -> format("exception of type %s", s)
        );
    }
}
