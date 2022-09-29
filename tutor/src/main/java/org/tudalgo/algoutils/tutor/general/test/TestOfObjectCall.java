package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;

import java.util.function.Predicate;

/**
 * <p>A test type testing the behavior of a callable and the object returned by this callable.</p>
 * <p>Because this test type tests the object returned by the the callable, exceptional calls result in negative results.</p>
 *
 * @param <T> the type of the callable object
 * @author Dustin Glaser
 */
public interface TestOfObjectCall<T, TT extends TestOfObjectCall<T, TT, RT>, RT extends ResultOfObjectCall<T, RT, TT>> extends Test<TT, RT> {

    /**
     * Tests if the callable behavior and the object returned by the callable are as expected.
     *
     * @param callable           the callable under test
     * @param context            the context of the callable under test
     * @param preCommentSupplier a supplier for a pre-comment to be added to the comment
     * @throws Error if the object is not as expected
     */
    default T assertSuccessful(ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<T, RT, TT>> preCommentSupplier) {
        return run(callable).check(context, preCommentSupplier).object();
    }

    /**
     * Tests if the callable behavior and the exception thrown by the callable are as expected.
     *
     * @param callable the callable under test
     * @return the result of the test
     */
    RT run(ObjectCallable<T> callable);


    interface Builder<T, TT extends TestOfObjectCall<T, TT, RT>, RT extends ResultOfObjectCall<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder<TT, RT, BT> {

        BT evaluator(Predicate<T> evaluator);

        interface Factory<T, TT extends TestOfObjectCall<T, TT, RT>, RT extends ResultOfObjectCall<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder.Factory<TT, RT, BT> {

        }
    }
}
