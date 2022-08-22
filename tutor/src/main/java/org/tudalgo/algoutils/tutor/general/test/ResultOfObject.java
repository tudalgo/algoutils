package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfObject<T> extends ResultWithObject<TestOfObject<T>, T> {

    T assertSuccessful(Context context, PreCommentSupplier<ResultOfObject<T>> preCommentSupplier);

    @Override
    TestOfObject<T> test();
}
