package org.tudalgo.algoutils.tutor.general.test;

public interface Result<T extends Result<T>> {

    void assertSuccessful(Context context, PreCommentSupplier<T> preCommentSupplier);

    Object behaviorActual();

    default Object behaviorExpected() {
        return test().expected();
    }

    boolean successful();

    Test test();
}
