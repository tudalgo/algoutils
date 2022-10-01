package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfExceptionalCall;
import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedException;

public class BasicResultOfExceptionalCall<T extends Exception> extends BasicResult<BasicResultOfExceptionalCall<T>, ActualException<T>, BasicTestOfExceptionalCall<T>, ExpectedException<T>> implements ResultOfExceptionalCall<T, BasicResultOfExceptionalCall<T>, BasicTestOfExceptionalCall<T>> {

    public BasicResultOfExceptionalCall(Environment environment, BasicTestOfExceptionalCall<T> test, ActualException<T> actual, Exception exception, boolean successful) {
        super(environment, test, actual, exception, successful);
    }

    public static final class Builder<T extends Exception> extends BasicResult.Builder<BasicResultOfExceptionalCall<T>, ActualException<T>, BasicTestOfExceptionalCall<T>, ExpectedException<T>, Builder<T>> implements ResultOfExceptionalCall.Builder<T, BasicResultOfExceptionalCall<T>, BasicTestOfExceptionalCall<T>, Builder<T>> {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicResultOfExceptionalCall<T> build() {
            return new BasicResultOfExceptionalCall<>(environment, test, actual, exception, successful);
        }

        public static final class Factory<T extends Exception> extends BasicResult.Builder.Factory<BasicResultOfExceptionalCall<T>, ActualException<T>, BasicTestOfExceptionalCall<T>, ExpectedException<T>, Builder<T>> implements ResultOfExceptionalCall.Builder.Factory<T, BasicResultOfExceptionalCall<T>, BasicTestOfExceptionalCall<T>, Builder<T>> {

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
