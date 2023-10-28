package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfCall;
import org.tudalgo.algoutils.tutor.general.assertions.TestOfCall;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing;
import org.tudalgo.algoutils.tutor.general.callable.Callable;

import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.successBehavior;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.text;

public class BasicTestOfCall extends BasicTest<TestOfCall, Nothing, ResultOfCall, Nothing> implements TestOfCall {

    protected BasicTestOfCall(Environment environment, Nothing expected) {
        super(environment, expected);
    }

    @Override
    public ResultOfCall run(Callable callable) {
        try {
            callable.call();
            return new BasicResultOfCall.Builder(environment()).actual(successBehavior()).test(this).build();
        } catch (Throwable throwable) {
            return new BasicResultOfCall.Builder(environment()).actual(text()).test(this).exception(throwable).build();
        }
    }

    public static class Builder extends BasicTest.Builder<TestOfCall, Nothing, ResultOfCall, Nothing, TestOfCall.Builder> implements TestOfCall.Builder {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public TestOfCall build() {
            return new BasicTestOfCall(environment, expected);
        }

        public static class Factory extends BasicTest.Builder.Factory<TestOfCall, Nothing, ResultOfCall, Nothing, TestOfCall.Builder> implements TestOfCall.Builder.Factory {

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
