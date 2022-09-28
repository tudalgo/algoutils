package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.callable.Callable;
import org.tudalgo.algoutils.tutor.general.test.TestOfThrowableCall;

import java.util.Objects;
import java.util.function.Predicate;

public class BasicTestOfThrowableCall<T extends Throwable> extends BasicTest implements TestOfThrowableCall<T> {

    private final Class<T> throwableClass;

    private final Predicate<T> evaluator;

    public BasicTestOfThrowableCall(Environment environment, Object expectation, Class<T> throwableClass, Predicate<T> evaluator) {
        super(environment, expectation);
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

    public static final class Builder<T extends Throwable> extends BasicTest.Builder<Builder<T>> implements TestOfThrowableCall.Builder<T> {

        private Class<T> throwableClass;
        private Predicate<T> evaluator;

        private Builder(Environment environment) {
            super(environment);
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

        public static final class Factory<T extends Throwable> extends BasicTest.Builder.Factory implements TestOfThrowableCall.Builder.Factory<T> {

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
