package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfObjectCall<T> extends ResultWithObject<TestOfObjectCall<T>, T>, ResultWithThrowable<TestOfObjectCall<T>, Throwable> {

    T assertSuccessful(Context context, PreCommentSupplier<? super ResultOfObjectCall<T>> preCommentSupplier);

    @Override
    default Object behaviorActual() {
        return actualThrowable() != null ? actualThrowable() : actualObject();
    }
}
