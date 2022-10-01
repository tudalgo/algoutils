package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.callable.Callable;
import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedException;

/**
 * <p>A type for testing the throwing behavior of callables.</p>
 *
 * @param <T>  the type of the expected exception
 * @param <TT> the type of the test
 * @param <RT> the type of the result
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

    /**
     * <p>A builder for {@linkplain TestOfThrowableCall tests of throwable calls}.</p>
     *
     * @param <T>  the type of the expected exception
     * @param <TT> the type of the test
     * @param <RT> the type of the result
     * @param <BT> the type of the builder
     */
    interface Builder<T extends Exception, TT extends TestOfThrowableCall<T, TT, RT>, RT extends ResultOfThrowableCall<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder<TT, ExpectedException<T>, RT, ActualException<T>, BT> {

        /**
         * <p>A factory for {@link Builder test of throwable call builders}.</p>
         *
         * @param <T>  the type of the expected exception
         * @param <TT> the type of the test
         * @param <RT> the type of the result
         * @param <BT> the type of the builder
         */
        interface Factory<T extends Exception, TT extends TestOfThrowableCall<T, TT, RT>, RT extends ResultOfThrowableCall<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder.Factory<TT, ExpectedException<T>, RT, ActualException<T>, BT> {

        }
    }
}
