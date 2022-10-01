package org.tudalgo.algoutils.tutor.general.test.expected;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;
import java.util.function.Predicate;

public interface ExpectedException<T extends Throwable> extends ExpectedObject<Class<T>> {

    default Class<T> type() {
        return object();
    }

    static <T extends Exception> ExpectedException<T> of(
        Class<T> clazz,
        Predicate<Class<T>> classTest,
        Predicate<T> exceptionTest,
        Function<String, String> formatter
    ) {

        return new ExpectedException<>() {

            @Override
            public Class<T> object() {
                return clazz;
            }

            @Override
            public String string(Stringifier stringifier) {
                return formatter.apply(BRACKET_FORMATTER.apply(stringifier.stringify(clazz)));
            }

            @Override
            public boolean test(T t) {
                return exceptionTest.test(t);
            }

            @Override
            public boolean test(Class<T> clazz) {
                return classTest.test(clazz);
            }
        };
    }

    @Override
    default String string(Stringifier stringifier) {
        return String.format("exception of type %s", BRACKET_FORMATTER.apply(stringifier.stringify(this)));
    }

    boolean test(Class<T> clazz);

    boolean test(T t);
}
