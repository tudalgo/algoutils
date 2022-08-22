package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfCall extends ResultWithThrowable<TestOfCall, Throwable> {

    void assertSuccessful(Context context, PreCommentSupplier<? extends ResultOfCall> preCommentSupplier);
}
