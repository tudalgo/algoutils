package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;
import org.tudalgo.algoutils.tutor.general.test.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedException;

/**
 * A test type testing the behavior of a callable that is expected to throw an exception.
 *
 * @param <T> the type of the exception to test
 * @author Dustin Glaser
 */
public interface TestOfThrowableCall<T extends Exception, TT extends TestOfThrowableCall<T, TT, RT>, RT extends ResultOfThrowableCall<T, RT, TT>> extends Test<TT, ExpectedException<T>, RT, ActualException<T>> {

    /**
     * Tests if the callable throws an exception and if the exception is as expected.
     *
     * @param callable the callable under test
     * @return the result of the test
     */
    RT run(Callable callable);

    interface Builder<T extends Exception, TT extends TestOfThrowableCall<T, TT, RT>, RT extends ResultOfThrowableCall<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder<TT, ExpectedException<T>, RT, ActualException<T>, BT> {

        BT expected(ExpectedException<T> expected);

        interface Factory<T extends Exception, TT extends TestOfThrowableCall<T, TT, RT>, RT extends ResultOfThrowableCall<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder.Factory<TT, ExpectedException<T>, RT, ActualException<T>, BT> {

            Builder<T, TT, RT, BT> builder();
        }
    }
}
