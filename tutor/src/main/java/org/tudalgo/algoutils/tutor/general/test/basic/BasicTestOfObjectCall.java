package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;
import org.tudalgo.algoutils.tutor.general.test.ResultOfObjectCall;
import org.tudalgo.algoutils.tutor.general.test.TestOfObjectCall;

import java.util.Objects;
import java.util.function.Predicate;

public class BasicTestOfObjectCall<T> extends BasicTest implements TestOfObjectCall<T> {

    private final Environment environment;

    private final Predicate<T> evaluator;

    public BasicTestOfObjectCall(Environment environment, Object expectation, Predicate<T> evaluator) {
        super(expectation);
        this.environment = environment;
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

    public static final class Builder<T> implements TestOfObjectCall.Builder<T> {

        private final Environment environment;
        private Object expectation;
        private Predicate<T> evaluator;

        private Builder(Environment environment) {
            this.environment = environment;
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

        @Override
        public TestOfObjectCall.Builder<T> expectation(Object expectation) {
            this.expectation = expectation;
            return this;
        }

        public static final class Factory implements TestOfObjectCall.Builder.Factory {

            private final Environment environment;

            public Factory(Environment environment) {
                this.environment = environment;
            }

            @Override
            public <T> Builder<T> builder() {
                return new Builder<>(environment);
            }
        }
    }
}
