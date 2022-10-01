package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.ResultOfThrowableCall;
import org.tudalgo.algoutils.tutor.general.test.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedException;

public class BasicResultOfThrowableCall<T extends Exception> extends BasicResult<BasicResultOfThrowableCall<T>, ActualException<T>, BasicTestOfThrowableCall<T>, ExpectedException<T>> implements ResultOfThrowableCall<T, BasicResultOfThrowableCall<T>, BasicTestOfThrowableCall<T>> {

    public BasicResultOfThrowableCall(Environment environment, BasicTestOfThrowableCall<T> test, ActualException<T> actual, Exception exception) {
        super(environment, test, actual, exception);
    }

    public static final class Builder<T extends Exception> extends BasicResult.Builder<BasicResultOfThrowableCall<T>, ActualException<T>, BasicTestOfThrowableCall<T>, ExpectedException<T>, Builder<T>> implements ResultOfThrowableCall.Builder<T, BasicResultOfThrowableCall<T>, BasicTestOfThrowableCall<T>, Builder<T>> {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicResultOfThrowableCall<T> build() {
            return new BasicResultOfThrowableCall<>(environment, test, actual, exception);
        }

        public static final class Factory<T extends Exception> extends BasicResult.Builder.Factory<BasicResultOfThrowableCall<T>, ActualException<T>, BasicTestOfThrowableCall<T>, ExpectedException<T>, Builder<T>> implements ResultOfThrowableCall.Builder.Factory<T, BasicResultOfThrowableCall<T>, BasicTestOfThrowableCall<T>, Builder<T>> {

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
