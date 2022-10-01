package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.Fail;
import org.tudalgo.algoutils.tutor.general.assertions.actual.NoActual;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing;

public class BasicFail extends BasicTest<BasicFail, Nothing, BasicResultOfFail, NoActual> implements Fail<BasicFail, BasicResultOfFail> {

    public BasicFail(Environment environment) {
        super(environment, null);
    }

    @Override
    public BasicResultOfFail run(Exception cause) {
        return new BasicResultOfFail.Builder(environment()).test(this).exception(cause).build();
    }

    public static class Builder extends BasicTest.Builder<BasicFail, Nothing, BasicResultOfFail, NoActual, Builder> implements Fail.Builder<BasicFail, BasicResultOfFail, Builder> {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicFail build() {
            return new BasicFail(environment);
        }

        public static class Factory extends BasicTest.Builder.Factory<BasicFail, Nothing, BasicResultOfFail, NoActual, Builder> implements Fail.Builder.Factory<BasicFail, BasicResultOfFail, Builder> {

            public Factory(Environment environment) {
                super(environment);
            }

            @Override
            public Builder builder() {
                return new Builder(environment);
            }
        }
    }
}
