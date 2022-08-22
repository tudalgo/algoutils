package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfThrowableCall<T extends Throwable> extends ResultWithThrowable<TestOfThrowableCall<T>, ResultOfThrowableCall<T>, T> {

    T assertSuccessful(Context context, PreCommentSupplier<? extends TestOfThrowableCall<T>, ? extends ResultOfThrowableCall<T>> preCommentSupplier);

    @Override
    TestOfThrowableCall<T> test();
}
