package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.ResultOfThrowableCall;

import static org.tudalgo.algoutils.tutor.general.Utils.none;

public class BasicResultOfThrowableCall<T extends Throwable> extends BasicResult<BasicResultOfThrowableCall<T>, BasicTestOfThrowableCall<T>> implements ResultOfThrowableCall<T, BasicResultOfThrowableCall<T>, BasicTestOfThrowableCall<T>> {

    private final T throwable;
    private final Object behavior;

    public BasicResultOfThrowableCall(Environment environment, BasicTestOfThrowableCall<T> test, boolean successful, T throwable, Object actual) {
        super(environment, test, successful);
        this.throwable = throwable;
        this.behavior = actual;
    }

    @Override
    public Object actual() {
        return behavior;
    }

    @Override
    public T throwable() {
        return throwable;
    }

    public static final class Builder<T extends Throwable> extends BasicResult.Builder<BasicResultOfThrowableCall<T>, BasicTestOfThrowableCall<T>, Builder<T>> implements ResultOfThrowableCall.Builder<T, BasicResultOfThrowableCall<T>, BasicTestOfThrowableCall<T>, Builder<T>> {

        private T throwable = none();

        private Object actual = none();

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public Builder<T> actual(Object actual) {
            this.throwable = none();
            this.actual = actual;
            return this;
        }

        @Override
        public BasicResultOfThrowableCall<T> build() {
            return new BasicResultOfThrowableCall<>(environment, test, successful, throwable, actual);
        }

        @Override
        public Builder<T> throwable(T throwable) {
            this.throwable = throwable;
            this.actual = throwable;
            return this;
        }
    }
}
