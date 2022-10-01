package org.tudalgo.algoutils.tutor.general.test.basic;

import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Context;
import org.tudalgo.algoutils.tutor.general.test.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.test.Result;
import org.tudalgo.algoutils.tutor.general.test.Test;
import org.tudalgo.algoutils.tutor.general.test.actual.Actual;
import org.tudalgo.algoutils.tutor.general.test.expected.Expected;

public abstract class BasicResult<RT extends BasicResult<RT, AT, TT, ET>, AT extends Actual, TT extends BasicTest<TT, ET, RT, AT>, ET extends Expected> implements Result<RT, AT, TT, ET> {

    protected final Environment environment;

    protected final TT test;
    protected final AT actual;
    protected final Exception exception;
    protected final boolean successful;

    public BasicResult(Environment environment, TT test, AT actual, Exception exception, boolean successful) {
        this.environment = environment;
        this.test = test;
        this.actual = actual;
        this.exception = exception;
        this.successful = successful;
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
            throw new AssertionFailedError(environment.getCommentFactory().comment((RT) this, context, preCommentSupplier));
        //noinspection unchecked
        return (RT) this;
    }

    @Override
    public boolean successful() {
        return successful;
    }

    @Override
    public TT test() {
        return test;
    }

    public static abstract class Builder<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected, BT extends Builder<RT, AT, TT, ET, BT>> implements Result.Builder<RT, AT, TT, ET, BT> {

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

        public BT successful(boolean successful) {
            this.successful = successful;
            //noinspection unchecked
            return (BT) this;
        }

        public static abstract class Factory<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected, BT extends Builder<RT, AT, TT, ET, BT>> implements Result.Builder.Factory<RT, AT, TT, ET, BT> {

            protected final Environment environment;

            public Factory(Environment environment) {
                this.environment = environment;
            }
        }
    }
}
