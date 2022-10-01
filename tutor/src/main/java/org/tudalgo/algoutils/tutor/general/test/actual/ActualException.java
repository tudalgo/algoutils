package org.tudalgo.algoutils.tutor.general.test.actual;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;

public interface ActualException<T extends Throwable> extends ActualObject<T> {

    Function<String, String> DEFAULT_FORMATTER = s -> "exception of type";

    default T exception() {
        return object();
    }

    static <T extends Throwable> ActualException<T> of(T exception, boolean successful, Function<String, String> formatter) {
        //noinspection DuplicatedCode
        return new ActualException<>() {

            @Override
            public T object() {
                return exception;
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

    static <T extends Throwable> ActualException<T> of(T exception, boolean successful) {
        return of(exception, successful, DEFAULT_FORMATTER);
    }


}
