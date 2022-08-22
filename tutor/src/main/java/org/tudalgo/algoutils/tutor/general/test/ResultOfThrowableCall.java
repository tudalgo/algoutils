package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfThrowableCall<T extends Throwable> extends ResultWithThrowable<TestOfThrowableCall<T>, T> {

    T assertSuccessful(Context context, PreCommentSupplier<? super ResultOfThrowableCall<T>> preCommentSupplier);

    @Override
    TestOfThrowableCall<T> test();
}
