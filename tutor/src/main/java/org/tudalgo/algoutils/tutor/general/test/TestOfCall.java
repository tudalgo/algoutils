package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

import java.util.function.BooleanSupplier;

public interface TestOfCall extends Test {

    default void assertSuccessful(Callable callable, Context context, PreCommentSupplier<? super ResultOfCall> preCommentSupplier) {
        test(callable).assertSuccessful(context, preCommentSupplier);
    }

    ResultOfCall test(Callable callable);

    interface Builder {

        TestOfCall build();

        Builder evaluator(BooleanSupplier evaluator);

        Builder expectation(Object expectation);

        interface Factory {

            TestOfCall builder();
        }
    }
}
