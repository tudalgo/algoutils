package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.assertions.Result;
import org.tudalgo.algoutils.tutor.general.assertions.Test;
import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;

/**
 * <p>An abstract basic implementation of a test.</p>
 *
 * @param <TT> the type of the test
 * @param <ET> the type of the expected behavior
 * @param <RT> the type of the result
 * @param <AT> the type of the actual behavior
 * @author Dustin Glaser
 */
public abstract class BasicResult<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected> implements Result<RT, AT, TT, ET> {

    protected final Environment environment;

    protected final TT test;
    protected final AT actual;
    protected final Exception exception;

    /**
     * Constructs a new result with the given environment, test, actual behavior, exception and state if the test was successful.
     *
     * @param environment the environment
     * @param test        the test
     * @param actual      the actual behavior
     * @param exception   the exception
     */
    protected BasicResult(Environment environment, TT test, AT actual, Exception exception) {
        this.environment = environment;
        this.test = test;
        this.actual = actual;
        this.exception = exception;
    }

    @Override
    public AT actual() {
        return actual;
    }

    @Override
    public Exception cause() {
        return exception;
    }

    @Override
    public RT check(Context context, PreCommentSupplier<? super RT> preCommentSupplier) {
        if (!successful())
            //noinspection unchecked
            throw new AssertionFailedError(
                environment.getCommentFactory().comment((RT) this, context, preCommentSupplier),
                expected().behavior(),
                actual().behavior()
            );
        //noinspection unchecked
        return (RT) this;
    }

    @Override
    public TT test() {
        return test;
    }

    public static abstract class Builder<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected, BT extends Result.Builder<RT, AT, TT, ET, BT>> implements Result.Builder<RT, AT, TT, ET, BT> {

        protected final Environment environment;
        protected TT test;
        protected AT actual;
        protected Exception exception;
        protected boolean successful;

        protected Builder(Environment environment) {
            this.environment = environment;
        }

        @Override
        public BT actual(AT actual) {
            this.actual = actual;
            //noinspection unchecked
            return (BT) this;
        }

        @Override
        public BT exception(Exception exception) {
            this.exception = exception;
            //noinspection unchecked
            return (BT) this;
        }

        @Override
        public BT test(TT test) {
            this.test = test;
            //noinspection unchecked
            return (BT) this;
        }

        public static abstract class Factory<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected, BT extends Result.Builder<RT, AT, TT, ET, BT>> implements Result.Builder.Factory<RT, AT, TT, ET, BT> {

            protected final Environment environment;

            protected Factory(Environment environment) {
                this.environment = environment;
            }
        }
    }
}
