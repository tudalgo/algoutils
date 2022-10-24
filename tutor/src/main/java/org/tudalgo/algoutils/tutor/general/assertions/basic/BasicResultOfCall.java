package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfCall;
import org.tudalgo.algoutils.tutor.general.assertions.TestOfCall;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing;

public class BasicResultOfCall extends BasicResult<ResultOfCall, Nothing, TestOfCall, Nothing> implements ResultOfCall {

    protected BasicResultOfCall(Environment environment, TestOfCall test, Nothing actual, Exception exception) {
        super(environment, test, actual, exception);
    }

    public static class Builder extends BasicResult.Builder<ResultOfCall, Nothing, TestOfCall, Nothing, Builder> {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public ResultOfCall build() {
            return new BasicResultOfCall(environment, test, actual, exception);
        }
    }
}
