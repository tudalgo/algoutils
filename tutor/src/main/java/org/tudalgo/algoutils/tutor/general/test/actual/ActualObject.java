package org.tudalgo.algoutils.tutor.general.test.actual;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;

import static java.util.function.Function.identity;

public interface ActualObject<T> extends Actual {

    static <T> ActualObject<T> of(T object, boolean successful, Function<String, String> formatter) {

        //noinspection DuplicatedCode
        return new ActualObject<>() {
            @Override
            public T object() {
                return object;
            }

            @Override
            public String string(Stringifier stringifier) {
                return formatter.apply(BRACKET_FORMATTER.apply(stringifier.stringify(object())));
            }

            @Override
            public boolean successful() {
                return successful;
            }
        };
    }

    static <T> ActualObject<T> of(T object, boolean successful) {
        return of(object, successful, identity());
    }

    @Override
    T object();
}
