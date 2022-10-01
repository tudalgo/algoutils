package org.tudalgo.algoutils.tutor.general.test.expected;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;
import java.util.function.Predicate;

public interface ExpectedObject<T> extends Expected {

    static <T> ExpectedObject<T> of(
        T object,
        Predicate<T> test,
        Function<String, String> formatter
    ) {

        return new ExpectedObject<>() {

            @Override
            public T object() {
                return object;
            }

            @Override
            public String string(Stringifier stringifier) {
                return formatter.apply(BRACKET_FORMATTER.apply(stringifier.stringify(object)));
            }

            @Override
            public boolean test(T t) {
                return test.test(t);
            }
        };
    }

    static <T> ExpectedObject<T> of(
        T object,
        Predicate<T> test
    ) {
        return of(object, test, Function.identity());
    }

    @Override
    T object();

    boolean test(T t);
}
