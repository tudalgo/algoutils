package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.ResultOfObjectCall;

import static org.tudalgo.algoutils.tutor.general.Utils.none;

public class BasicResultOfObjectCall<T> extends BasicResult<BasicResultOfObjectCall<T>, BasicTestOfObjectCall<T>> implements ResultOfObjectCall<T, BasicResultOfObjectCall<T>, BasicTestOfObjectCall<T>> {

    private final T actualObject;
    private final Throwable actualThrowable;

    public BasicResultOfObjectCall(Environment environment, BasicTestOfObjectCall<T> test, boolean successful, T actualObject, Throwable actualThrowable) {
        super(environment, test, successful);
        this.actualObject = actualObject;
        this.actualThrowable = actualThrowable;
    }

    @Override
    public Object actual() {
        return actualObject;
    }


//    @Override
//    public T assertSuccessful(Context context, PreCommentSupplier<? super ResultOfObject<T, BasicResultOfObjectCall<T>, BasicTestOfObjectCall<T>>> preCommentSupplier) {
//        check(context, preCommentSupplier);
//        return object();
//    }

    @Override
    public T object() {
        return actualObject;
    }

    @Override
    public Throwable throwable() {
        return actualThrowable;
    }

    public static final class Builder<T> extends BasicResult.Builder<BasicResultOfObjectCall<T>, BasicTestOfObjectCall<T>, Builder<T>> implements ResultOfObjectCall.Builder<T, BasicResultOfObjectCall<T>, BasicTestOfObjectCall<T>, Builder<T>> {

        private T object = none();
        private Throwable throwable = none();

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicResultOfObjectCall<T> build() {
            return new BasicResultOfObjectCall<>(environment, test, successful, object, throwable);
        }

        @Override
        public Builder<T> object(T object) {
            this.object = object;
            return this;
        }

        @Override
        public Builder<T> throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public static final class Factory<T> extends BasicResult.Builder.Factory<BasicResultOfObjectCall<T>, BasicTestOfObjectCall<T>, Builder<T>> implements ResultOfObjectCall.Builder.Factory<T, BasicResultOfObjectCall<T>, BasicTestOfObjectCall<T>, Builder<T>> {

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
