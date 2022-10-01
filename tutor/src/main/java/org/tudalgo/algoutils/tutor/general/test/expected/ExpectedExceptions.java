package org.tudalgo.algoutils.tutor.general.test.expected;

import static java.lang.String.format;

public final class ExpectedExceptions {

    private ExpectedExceptions() {

    }

    public static <T extends Exception> ExpectedException<T> instanceOf(Class<T> clazz) {
        return ExpectedException.of(
            clazz,
            c -> c == clazz,
            t -> true,
            s -> format("exception of type %s", s)
        );
    }
}
