package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.TestOfExceptionalCall;
import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedException;
import org.tudalgo.algoutils.tutor.general.callable.Callable;

import static org.tudalgo.algoutils.tutor.general.assertions.actual.ActualExceptions.noException;
import static org.tudalgo.algoutils.tutor.general.assertions.actual.ActualExceptions.unexpectedException;

/**
 * <p>A basic implementation of a test of a throwable call.</p>
 *
 * @param <T> the type of the expected exception
 */
public class BasicTestOfExceptionalCall<T extends Exception> extends BasicTest<BasicTestOfExceptionalCall<T>, ExpectedException<T>, BasicResultOfExceptionalCall<T>, ActualException<T>> implements TestOfExceptionalCall<T, BasicTestOfExceptionalCall<T>, BasicResultOfExceptionalCall<T>> {

    /**
     * <p>Constructs a new test of a throwable call with the given environment and expected exception.</p>
     *
     * @param environment the environment
     * @param expected    the expected exception
     */
    public BasicTestOfExceptionalCall(Environment environment, ExpectedException<T> expected) {
        super(environment, expected);
    }

    @Override
    public BasicResultOfExceptionalCall<T> run(Callable callable) {
        var builder = new BasicResultOfExceptionalCall.Builder<T>(environment()).test(this);
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
        extends BasicTest.Builder<BasicTestOfExceptionalCall<T>, ExpectedException<T>, BasicResultOfExceptionalCall<T>, ActualException<T>, Builder<T>>
        implements TestOfExceptionalCall.Builder<T, BasicTestOfExceptionalCall<T>, BasicResultOfExceptionalCall<T>, Builder<T>> {

        private Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicTestOfExceptionalCall<T> build() {
            return new BasicTestOfExceptionalCall<>(environment, expected);
        }


        public static final class Factory<T extends Exception>
            extends BasicTest.Builder.Factory<BasicTestOfExceptionalCall<T>, ExpectedException<T>, BasicResultOfExceptionalCall<T>, ActualException<T>, Builder<T>>
            implements TestOfExceptionalCall.Builder.Factory<T, BasicTestOfExceptionalCall<T>, BasicResultOfExceptionalCall<T>, Builder<T>> {

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
