package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfObjectCall<T> extends ResultWithObject<T>, ResultWithThrowable<Throwable> {

    T assertSuccessful(Context context, PreCommentSupplier<? extends ResultOfObjectCall<T>> preCommentSupplier);

    @Override
    default Object behaviorActual() {
        return actualThrowable() != null ? actualThrowable() : actualObject();
    }

    @Override
    TestOfObjectCall<T> test();
}
