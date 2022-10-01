package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.Test;
import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;

public abstract class BasicTest<TT extends BasicTest<TT, ET, RT, AT>, ET extends Expected, RT extends BasicResult<RT, AT, TT, ET>, AT extends Actual> implements Test<TT, ET, RT, AT> {

    private final Environment environment;
    private final ET expected;

    public BasicTest(Environment environment, ET expected) {
        this.environment = environment;
        this.expected = expected;
    }

    public final Environment environment() {
        return environment;
    }

    @Override
    public ET expected() {
        return expected;
    }

    public static abstract class Builder<TT extends BasicTest<TT, ET, RT, AT>, ET extends Expected, RT extends BasicResult<RT, AT, TT, ET>, AT extends Actual, BT extends Builder<TT, ET, RT, AT, BT>> implements Test.Builder<TT, ET, RT, AT, BT> {

        protected final Environment environment;

        protected ET expected;

        protected Builder(Environment environment) {
            this.environment = environment;
        }

        @Override
        public BT expected(ET expected) {
            this.expected = expected;
            //noinspection unchecked
            return (BT) this;
        }

        public static abstract class Factory<TT extends BasicTest<TT, ET, RT, AT>, ET extends Expected, RT extends BasicResult<RT, AT, TT, ET>, AT extends Actual, BT extends Builder<TT, ET, RT, AT, BT>> implements Test.Builder.Factory<TT, ET, RT, AT, BT> {

            protected final Environment environment;

            public Factory(Environment environment) {
                this.environment = environment;
            }
        }
    }
}
