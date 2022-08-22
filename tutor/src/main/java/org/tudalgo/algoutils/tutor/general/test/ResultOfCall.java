package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfCall extends ResultWithThrowable<Throwable> {

    void assertSuccessful(Context context, PreCommentSupplier<? extends ResultOfCall> preCommentSupplier);

    @Override
    TestOfCall test();
}
