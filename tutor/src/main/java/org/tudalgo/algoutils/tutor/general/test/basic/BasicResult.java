package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Context;
import org.tudalgo.algoutils.tutor.general.test.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.test.Result;
import org.tudalgo.algoutils.tutor.general.test.Test;

public abstract class BasicResult<RT extends BasicResult<RT, TT>, TT extends BasicTest<TT, RT>> implements Result<RT, TT> {

    protected final Environment environment;
    protected final boolean successful;
    protected final TT test;

    public BasicResult(Environment environment, TT test, boolean successful) {
        this.environment = environment;
        this.successful = successful;
        this.test = test;
    }

    @Override
    public RT check(Context context, PreCommentSupplier<? super RT> preCommentSupplier) {
        if (!successful())
            //noinspection unchecked
            throw new AssertionError(environment.getCommentFactory().comment((RT) this, context, preCommentSupplier));
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

    public static abstract class Builder<RT extends Result<RT, TT>, TT extends Test<TT, RT>, BT extends Builder<RT, TT, BT>> implements Result.Builder<RT, TT, BT> {

        protected final Environment environment;
        protected boolean successful;
        protected TT test;

        protected Builder(Environment environment) {
            this.environment = environment;
        }

        @Override
        public BT successful(boolean successful) {
            this.successful = successful;
            //noinspection unchecked
            return (BT) this;
        }

        @Override
        public BT test(TT test) {
            this.test = test;
            //noinspection unchecked
            return (BT) this;
        }

        public static abstract class Factory<RT extends Result<RT, TT>, TT extends Test<TT, RT>, BT extends Builder<RT, TT, BT>> implements Result.Builder.Factory<RT, TT, BT> {

            protected final Environment environment;

            public Factory(Environment environment) {
                this.environment = environment;
            }
        }
    }
}
