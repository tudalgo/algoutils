package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.callable.Callable;
import org.tudalgo.algoutils.tutor.general.test.TestOfThrowableCall;
import org.tudalgo.algoutils.tutor.general.test.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedException;

import static org.tudalgo.algoutils.tutor.general.test.actual.ActualExceptions.noException;
import static org.tudalgo.algoutils.tutor.general.test.actual.ActualExceptions.unexpectedException;

public class BasicTestOfThrowableCall<T extends Exception> extends BasicTest<BasicTestOfThrowableCall<T>, ExpectedException<T>, BasicResultOfThrowableCall<T>, ActualException<T>> implements TestOfThrowableCall<T, BasicTestOfThrowableCall<T>, BasicResultOfThrowableCall<T>> {

    public BasicTestOfThrowableCall(Environment environment, ExpectedException<T> actual) {
        super(environment, actual);
    }

    @Override
    public BasicResultOfThrowableCall<T> run(Callable callable) {
        var builder = new BasicResultOfThrowableCall.Builder<T>(environment()).test(this);
        try {
            callable.call();
            builder.actual(noException());
        } catch (Exception throwable) {
            //noinspection unchecked
            if (expected().test((Class<T>) throwable.getClass())) {
                //noinspection unchecked
                builder.actual((T) throwable).successful(true);
            } else {
                builder.actual(unexpectedException());
                builder.exception(throwable);
            }
        }
        return builder.build();
    }

    public static final class Builder<T extends Exception>
        extends BasicTest.Builder<BasicTestOfThrowableCall<T>, ExpectedException<T>, BasicResultOfThrowableCall<T>, ActualException<T>, Builder<T>>
        implements TestOfThrowableCall.Builder<T, BasicTestOfThrowableCall<T>, BasicResultOfThrowableCall<T>, Builder<T>> {

        private Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicTestOfThrowableCall<T> build() {
            return new BasicTestOfThrowableCall<>(environment, expected);
        }


        public static final class Factory<T extends Exception>
            extends BasicTest.Builder.Factory<BasicTestOfThrowableCall<T>, ExpectedException<T>, BasicResultOfThrowableCall<T>, ActualException<T>, Builder<T>>
            implements TestOfThrowableCall.Builder.Factory<T, BasicTestOfThrowableCall<T>, BasicResultOfThrowableCall<T>, Builder<T>> {

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
