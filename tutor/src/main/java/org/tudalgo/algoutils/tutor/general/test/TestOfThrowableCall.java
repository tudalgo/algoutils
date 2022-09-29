package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

import java.util.function.Predicate;

/**
 * A test type testing the behavior of a callable that is expected to throw an exception.
 *
 * @param <T> the type of the exception to test
 * @author Dustin Glaser
 */
public interface TestOfThrowableCall<T extends Throwable, TT extends TestOfThrowableCall<T, TT, RT>, RT extends ResultOfThrowableCall<T, RT, TT>> extends Test<TT, RT> {

    /**
     * Tests if the callable throws an exception and if the exception is as expected.
     *
     * @param callable the callable under test
     * @return the result of the test
     */
    RT run(Callable callable);

    interface Builder<T extends Throwable, TT extends TestOfThrowableCall<T, TT, RT>, RT extends ResultOfThrowableCall<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder<TT, RT, BT> {

        BT evaluator(Class<T> throwable, Predicate<T> evaluator);

        interface Factory<T extends Throwable, TT extends TestOfThrowableCall<T, TT, RT>, RT extends ResultOfThrowableCall<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder.Factory<TT, RT, BT> {

        }
    }
}
