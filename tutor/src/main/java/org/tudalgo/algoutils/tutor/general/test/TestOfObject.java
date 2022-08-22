package org.tudalgo.algoutils.tutor.general.test;

public interface TestOfObject<T> extends Test {

    default T assertSuccessful(T object, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return test(object).assertSuccessful(context, preCommentSupplier);
    }

    ResultOfObject<T> test(T object);
}
