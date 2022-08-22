package org.tudalgo.algoutils.tutor.general.test;

public interface TestOfObject<T> extends Test {

    default void assertSuccessful(T object, Context context, PreCommentSupplier<TestOfObject<T>, ResultOfObject<T>> preCommentSupplier) {
        test(object).assertSuccessful(context, preCommentSupplier);
    }

    ResultOfObject<T> test(T object);
}
