package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;
import org.tudalgo.algoutils.tutor.general.test.ResultOfObjectCall;
import org.tudalgo.algoutils.tutor.general.test.TestOfObjectCall;

import java.util.Objects;
import java.util.function.Predicate;

public class BasicTestOfObjectCall<T> extends BasicTest implements TestOfObjectCall<T> {

    private final Predicate<T> evaluator;

    public BasicTestOfObjectCall(Environment environment, Object expectation, Predicate<T> evaluator) {
        super(environment, expectation);
        this.evaluator = Objects.requireNonNull(evaluator, "evaluator must not be null");
    }


    @Override
    public ResultOfObjectCall<T> test(ObjectCallable<T> callable) {
        Objects.requireNonNull(callable, "callable must not be null");
        T value;
        try {
            value = callable.call();
        } catch (Throwable throwable) {
            return new BasicResultOfObjectCall<>(environment, this, false, null, throwable);
        }
        return new BasicResultOfObjectCall<>(environment, this, evaluator.test(value), value, null);
    }

    public static final class Builder<T> extends BasicTest.Builder<Builder<T>> implements TestOfObjectCall.Builder<T> {

        private Predicate<T> evaluator;

        private Builder(Environment environment) {
            super(environment);
        }

        @Override
        public TestOfObjectCall<T> build() {
            return new BasicTestOfObjectCall<>(environment, expectation, evaluator);
        }

        @Override
        public TestOfObjectCall.Builder<T> evaluator(Predicate<T> evaluator) {
            this.evaluator = evaluator;
            return this;
        }

        public static final class Factory<T> extends BasicTest.Builder.Factory implements TestOfObjectCall.Builder.Factory<T> {

            public Factory(Environment environment) {
                super(environment);
            }

            @Override
            public Builder<T> builder() {
                return new Builder<>(environment);
            }
        }
    }
}
