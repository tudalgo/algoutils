package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

import java.util.function.BooleanSupplier;

/**
 * A test type testing the behavior of a callable that is expected not to throw an exception.
 *
 * @author Dustin Glaser
 */
public interface TestOfCall extends Test {

    /**
     * Tests the behavior of the given callable and throws an error if the behavior is not as expected.
     *
     * @param callable           the callable under test
     * @param context            the context of the callable under test
     * @param preCommentSupplier a supplier for a pre-comment to be added to the comment
     * @throws Error if the object is not as expected
     */
    default void assertSuccessful(Callable callable, Context context, PreCommentSupplier<? super ResultOfCall> preCommentSupplier) {
        test(callable).assertSuccessful(context, preCommentSupplier);
    }

    /**
     * Tests if the behavior of the given callable is as expected.
     *
     * @param callable the callable to test
     * @return the result of the test
     */
    ResultOfCall test(Callable callable);

    interface Builder {

        TestOfCall build();

        Builder evaluator(BooleanSupplier evaluator);

        Builder expectation(Object expectation);

        interface Factory {

            TestOfCall.Builder builder();
        }
    }
}
