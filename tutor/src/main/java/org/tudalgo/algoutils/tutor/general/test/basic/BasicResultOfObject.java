package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.ResultOfObject;

import static org.tudalgo.algoutils.tutor.general.Utils.none;

public final class BasicResultOfObject<T> extends BasicResult<BasicResultOfObject<T>, BasicTestOfObject<T>> implements ResultOfObject<T, BasicResultOfObject<T>, BasicTestOfObject<T>> {

    private final T actualObject;

    public BasicResultOfObject(Environment environment, BasicTestOfObject<T> test, boolean successful, T actualObject) {
        super(environment, test, successful);
        this.actualObject = actualObject;
    }

    @Override
    public T actual() {
        return actualObject;
    }

    @Override
    public T object() {
        return actualObject;
    }

    public static final class Builder<T> extends BasicResult.Builder<BasicResultOfObject<T>, BasicTestOfObject<T>, Builder<T>> implements ResultOfObject.Builder<T, BasicResultOfObject<T>, BasicTestOfObject<T>, Builder<T>> {

        private T actualObject = none();

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicResultOfObject<T> build() {
            return new BasicResultOfObject<>(environment, test, successful, actualObject);
        }

        @Override
        public Builder<T> object(T actualObject) {
            this.actualObject = actualObject;
            return this;
        }

        public static final class Factory<T> extends BasicResult.Builder.Factory<BasicResultOfObject<T>, BasicTestOfObject<T>, Builder<T>> implements ResultOfObject.Builder.Factory<T, BasicResultOfObject<T>, BasicTestOfObject<T>, Builder<T>> {

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
