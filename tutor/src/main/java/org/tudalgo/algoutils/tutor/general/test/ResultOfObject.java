package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfObject<T> extends ResultWithObject<T> {

    T assertSuccessful(Context context, PreCommentSupplier<ResultOfObject<T>> preCommentSupplier);

    @Override
    TestOfObject<T> test();
}
