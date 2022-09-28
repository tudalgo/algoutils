package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.test.TestOfObject;

import java.util.Objects;
import java.util.function.Predicate;

public class BasicTestOfObject<T> extends BasicTest implements TestOfObject<T> {

    private final Predicate<T> evaluator;

    public BasicTestOfObject(Environment environment, Object expectation, Predicate<T> evaluator) {
        super(environment, expectation);
        this.evaluator = Objects.requireNonNull(evaluator, "evaluator must not be null");
    }

    @Override
    public ResultOfObject<T> test(T object) {
        return new BasicResultOfObject<>(environment, this, evaluator.test(object), object);
    }

    public static final class Builder<T> extends BasicTest.Builder<Builder<T>> implements TestOfObject.Builder<T> {

        private Object expectation;

        private Predicate<T> evaluator;

        private Builder(Environment environment) {
            super(environment);
        }

        @Override
        public TestOfObject<T> build() {
            return new BasicTestOfObject<>(environment, expectation, evaluator);
        }

        @Override
        public TestOfObject.Builder<T> evaluator(Predicate<T> evaluator) {
            this.evaluator = evaluator;
            return this;
        }

        public static final class Factory<T> extends BasicTest.Builder.Factory implements TestOfObject.Builder.Factory<T> {

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
