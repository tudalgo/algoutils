package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfExceptionalCall;
import org.tudalgo.algoutils.tutor.general.assertions.TestOfExceptionalCall;
import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedException;

public class BasicResultOfExceptionalCall<T extends Exception> extends BasicResult<ResultOfExceptionalCall<T>, ActualException<T>, TestOfExceptionalCall<T>, ExpectedException<T>> implements ResultOfExceptionalCall<T> {

    public BasicResultOfExceptionalCall(Environment environment, TestOfExceptionalCall<T> test, ActualException<T> actual, Throwable exception, boolean successful) {
        super(environment, test, actual, exception);
    }

    public static final class Builder<T extends Exception> extends BasicResult.Builder<ResultOfExceptionalCall<T>, ActualException<T>, TestOfExceptionalCall<T>, ExpectedException<T>, ResultOfExceptionalCall.Builder<T>> implements ResultOfExceptionalCall.Builder<T> {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicResultOfExceptionalCall<T> build() {
            return new BasicResultOfExceptionalCall<>(environment, test, actual, exception, successful);
        }

        public static final class Factory<T extends Exception> extends BasicResult.Builder.Factory<ResultOfExceptionalCall<T>, ActualException<T>, TestOfExceptionalCall<T>, ExpectedException<T>, ResultOfExceptionalCall.Builder<T>> implements ResultOfExceptionalCall.Builder.Factory<T> {

            public Factory(Environment environment) {
                super(environment);
            }

            @Override
            public Builder<T> builder() {
                return new Builder<>(environment);
            }
        }
    }
}
