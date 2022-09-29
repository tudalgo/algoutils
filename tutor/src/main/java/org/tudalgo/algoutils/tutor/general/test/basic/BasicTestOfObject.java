package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.TestOfObject;

import java.util.Objects;
import java.util.function.Predicate;

public class BasicTestOfObject<T> extends BasicTest<BasicTestOfObject<T>, BasicResultOfObject<T>> implements TestOfObject<T, BasicTestOfObject<T>, BasicResultOfObject<T>> {

    private final Predicate<T> evaluator;

    public BasicTestOfObject(Environment environment, Object expectation, Predicate<T> evaluator) {
        super(environment, expectation);
        this.evaluator = Objects.requireNonNull(evaluator, "evaluator must not be null");
    }

    @Override
    public BasicResultOfObject<T> run(T object) {
        return new BasicResultOfObject<>(environment, this, evaluator.test(object), object);
    }

    public static final class Builder<T> extends BasicTest.Builder<BasicTestOfObject<T>, BasicResultOfObject<T>, Builder<T>> implements TestOfObject.Builder<T, BasicTestOfObject<T>, BasicResultOfObject<T>, Builder<T>> {

        private Object expectation;

        private Predicate<T> evaluator;

        private Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicTestOfObject<T> build() {
            return new BasicTestOfObject<>(environment, expectation, evaluator);
        }

        @Override
        public Builder<T> evaluator(Predicate<T> evaluator) {
            this.evaluator = evaluator;
            return this;
        }

        public static final class Factory<T> extends BasicTest.Builder.Factory<BasicTestOfObject<T>, BasicResultOfObject<T>, Builder<T>> implements TestOfObject.Builder.Factory<T, BasicTestOfObject<T>, BasicResultOfObject<T>, Builder<T>> {

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
