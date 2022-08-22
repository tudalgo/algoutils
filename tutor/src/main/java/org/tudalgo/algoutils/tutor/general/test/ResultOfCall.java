package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfCall extends ResultWithThrowable<TestOfCall, ResultOfCall, Throwable> {

    void assertSuccessful(Context context, PreCommentSupplier<? extends TestOfCall, ? extends ResultOfCall> preCommentSupplier);

    @Override
    TestOfCall test();
}
