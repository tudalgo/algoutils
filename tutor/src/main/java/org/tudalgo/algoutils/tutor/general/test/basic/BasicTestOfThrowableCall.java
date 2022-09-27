package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.callable.Callable;
import org.tudalgo.algoutils.tutor.general.test.TestOfThrowableCall;

import java.util.Objects;
import java.util.function.Predicate;

public class BasicTestOfThrowableCall<T extends Throwable> extends BasicTest implements TestOfThrowableCall<T> {

    private final Environment environment;

    private final Class<T> throwableClass;

    private final Predicate<T> evaluator;

    public BasicTestOfThrowableCall(Environment environment, Object expectation, Class<T> throwableClass, Predicate<T> evaluator) {
        super(expectation);
        this.environment = environment;
        this.throwableClass = Objects.requireNonNull(throwableClass, "throwableClass must not be null");
        this.evaluator = Objects.requireNonNull(evaluator, "evaluator must not be null");
    }

    @Override
    public BasicResultOfThrowableCall<T> test(Callable callable) {
        Objects.requireNonNull(callable, "callable must not be null");
        try {
            callable.call();
            return new BasicResultOfThrowableCall<>(environment, this, false, null, null);
        } catch (Throwable throwable) {
            //noinspection unchecked
            if (throwableClass.isAssignableFrom(throwable.getClass()) && evaluator.test((T) throwable)) {
                //noinspection unchecked
                return new BasicResultOfThrowableCall<>(environment, this, true, (T) throwable, throwable);
            }
            return new BasicResultOfThrowableCall<>(environment, this, false, null, throwable);
        }
    }

    public static final class Builder<T extends Throwable> implements TestOfThrowableCall.Builder<T> {

        private final Environment environment;
        private Object expectation;
        private Class<T> throwableClass;
        private Predicate<T> evaluator;

        private Builder(Environment environment) {
            this.environment = environment;
        }

        @Override
        public TestOfThrowableCall<T> build() {
            return new BasicTestOfThrowableCall<>(environment, expectation, throwableClass, evaluator);
        }

        @Override
        public TestOfThrowableCall.Builder<T> evaluator(Class<T> throwableClass, Predicate<T> evaluator) {
            this.throwableClass = throwableClass;
            this.evaluator = evaluator;
            return this;
        }

        @Override
        public TestOfThrowableCall.Builder<T> expectation(Object expectation) {
            this.expectation = expectation;
            return this;
        }

        public static final class Factory implements TestOfThrowableCall.Builder.Factory {

            private final Environment environment;

            public Factory(Environment environment) {
                this.environment = environment;
            }

            @Override
            public <T extends Throwable> Builder<T> builder() {
                return new Builder<>(environment);
            }
        }
    }
}
