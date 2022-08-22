package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfObjectCall<T> extends ResultWithObject<TestOfObjectCall<T>, ResultOfObjectCall<T>, T>, ResultWithThrowable<TestOfObjectCall<T>, ResultOfObjectCall<T>, Throwable> {

    T assertSuccessful(Context context, PreCommentSupplier<? extends TestOfObjectCall<T>, ? extends ResultOfObjectCall<T>> preCommentSupplier);

    @Override
    default Object behaviorActual() {
        return actualThrowable() != null ? actualThrowable() : actualObject();
    }

    @Override
    TestOfObjectCall<T> test();
}
