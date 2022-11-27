package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.Fail;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfFail;
import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing;

/**
 * <p>A basic implementation of a result of an always failing test.</p>
 *
 * @author Dustin Glaser
 */
public class BasicResultOfFail extends BasicResult<ResultOfFail, Actual, Fail, Expected> implements ResultOfFail {

    /**
     * <p>Constructs a new result with the given environment, test and exception.</p>
     *
     * @param test the test
     */
    public BasicResultOfFail(Environment environment, Fail test, Actual actual, Exception exception) {
        super(environment, test, actual, exception);
    }

    public static class Builder extends BasicResult.Builder<ResultOfFail, Actual, Fail, Expected, ResultOfFail.Builder> implements ResultOfFail.Builder {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicResultOfFail build() {
            return new BasicResultOfFail(environment, test, actual, exception);
        }

        public static class Factory extends BasicResult.Builder.Factory<ResultOfFail, Actual, Fail, Expected, ResultOfFail.Builder> implements ResultOfFail.Builder.Factory {

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
