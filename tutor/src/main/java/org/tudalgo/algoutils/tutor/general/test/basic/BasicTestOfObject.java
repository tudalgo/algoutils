package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.test.TestOfObject;

import java.util.Objects;
import java.util.function.Predicate;

public class BasicTestOfObject<T> extends BasicTest implements TestOfObject<T> {

    private final Environment environment;

    private final Predicate<T> evaluator;

    public BasicTestOfObject(Environment environment, Object expectation, Predicate<T> evaluator) {
        super(expectation);
        this.environment = environment;
        this.evaluator = Objects.requireNonNull(evaluator, "evaluator must not be null");
    }

    @Override
    public ResultOfObject<T> test(T object) {
        return new BasicResultOfObject<>(environment, this, evaluator.test(object), object);
    }

    public static final class Builder<T> implements TestOfObject.Builder<T> {

        private final Environment environment;

        private Object expectation;

        private Predicate<T> evaluator;

        private Builder(Environment environment) {
            this.environment = environment;
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

        @Override
        public TestOfObject.Builder<T> expectation(Object expectation) {
            this.expectation = expectation;
            return this;
        }

        public static final class Factory implements TestOfObject.Builder.Factory {

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
