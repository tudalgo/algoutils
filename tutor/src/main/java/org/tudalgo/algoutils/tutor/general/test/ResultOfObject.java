package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfObject<T> extends ResultWithObject<TestOfObject<T>, ResultOfObject<T>, T> {

    T assertSuccessful(Context context, PreCommentSupplier<? extends TestOfObject<T>, ? extends ResultOfObject<T>> preCommentSupplier);

    @Override
    TestOfObject<T> test();
}
