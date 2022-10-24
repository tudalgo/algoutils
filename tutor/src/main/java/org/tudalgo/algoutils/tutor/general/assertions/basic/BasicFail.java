package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.Fail;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfFail;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing;

/**
 * <p>A basic implementation an always failing test.</p>
 *
 * @author Dustin Glaser
 */
public class BasicFail extends BasicTest<Fail, Nothing, ResultOfFail, Nothing> implements Fail {

    /**
     * <p>Constructs a new fail with the given environment.</p>
     *
     * @param environment the environment
     */
    public BasicFail(Environment environment, Nothing expected) {
        super(environment, expected);
    }

    @Override
    public ResultOfFail run(Nothing actual, Exception cause) {
        return new BasicResultOfFail.Builder(environment()).test(this).actual(actual).exception(cause).build();
    }

    public static class Builder extends BasicTest.Builder<Fail, Nothing, ResultOfFail, Nothing, Fail.Builder> implements Fail.Builder {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicFail build() {
            return new BasicFail(environment, expected);
        }

        public static class Factory extends BasicTest.Builder.Factory<Fail, Nothing, ResultOfFail, Nothing, Fail.Builder> implements Fail.Builder.Factory {

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
