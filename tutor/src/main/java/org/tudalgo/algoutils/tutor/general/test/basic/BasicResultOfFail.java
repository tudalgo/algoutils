package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Result;
import org.tudalgo.algoutils.tutor.general.test.ResultOfFail;
import org.tudalgo.algoutils.tutor.general.test.actual.NoActual;
import org.tudalgo.algoutils.tutor.general.test.expected.Nothing;

public class BasicResultOfFail extends BasicResult<BasicResultOfFail, NoActual, BasicFail, Nothing> implements ResultOfFail<BasicResultOfFail, BasicFail> {

    public BasicResultOfFail(Environment environment, BasicFail test, Exception exception) {
        super(environment, test, null, exception);
    }

    public static class Builder extends BasicResult.Builder<BasicResultOfFail, NoActual, BasicFail, Nothing, Builder> implements ResultOfFail.Builder<BasicResultOfFail, BasicFail, Builder> {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicResultOfFail build() {
            return new BasicResultOfFail(environment, test, exception);
        }

        public static class Factory extends BasicResult.Builder.Factory<BasicResultOfFail, NoActual, BasicFail, Nothing, Builder> implements ResultOfFail.Builder.Factory<BasicResultOfFail, BasicFail, Builder> {

            public Factory(Environment environment) {
                super(environment);
            }

            @Override
            public Result.Builder<BasicResultOfFail, NoActual, BasicFail, Nothing, Builder> builder() {
                return new Builder(environment);
            }
        }
    }
}