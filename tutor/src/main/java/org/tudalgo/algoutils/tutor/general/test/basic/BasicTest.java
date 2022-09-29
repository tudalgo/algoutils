package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Test;

public abstract class BasicTest<TT extends BasicTest<TT, RT>, RT extends BasicResult<RT, TT>> implements Test<TT, RT> {

    protected final Environment environment;

    private final Object expectation;

    public BasicTest(Environment environment, Object expectation) {
        this.environment = environment;
        this.expectation = expectation;
    }

    public Object expectation() {
        return expectation;
    }

    public static abstract class Builder<TT extends BasicTest<TT, RT>, RT extends BasicResult<RT, TT>, BT extends Builder<TT, RT, BT>> implements Test.Builder<TT, RT, BT> {

        protected final Environment environment;

        protected Object expectation;

        protected Builder(Environment environment) {
            this.environment = environment;
        }

        @Override
        public BT expectation(Object expectation) {
            this.expectation = expectation;
            //noinspection unchecked
            return (BT) this;
        }

        public static abstract class Factory<TT extends BasicTest<TT, RT>, RT extends BasicResult<RT, TT>, BT extends Builder<TT, RT, BT>> implements Test.Builder.Factory<TT, RT, BT> {

            protected final Environment environment;

            public Factory(Environment environment) {
                this.environment = environment;
            }
        }
    }
}
