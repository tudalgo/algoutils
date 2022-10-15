package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedException;
import org.tudalgo.algoutils.tutor.general.callable.Callable;

/**
 * <p>A type for testing the throwing behavior of callables.</p>
 *
 * @param <T> the type of the expected exception
 * @author Dustin Glaser
 */
public interface TestOfExceptionalCall<T extends Exception> extends Test<TestOfExceptionalCall<T>, ExpectedException<T>, ResultOfExceptionalCall<T>, ActualException<T>> {

    /**
     * Tests if the callable throws an exception and if the exception is as expected.
     *
     * @param callable the callable under test
     * @return the result of the test
     */
    ResultOfExceptionalCall<T> run(Callable callable);

    /**
     * <p>A builder for {@linkplain TestOfExceptionalCall tests of throwable calls}.</p>
     *
     * @param <T>  the type of the expected exception
     */
    interface Builder<T extends Exception> extends Test.Builder<TestOfExceptionalCall<T>, ExpectedException<T>, ResultOfExceptionalCall<T>, ActualException<T>, Builder<T>> {

        interface Factory<T extends Exception> extends Test.Builder.Factory<TestOfExceptionalCall<T>, ExpectedException<T>, ResultOfExceptionalCall<T>, ActualException<T>, Builder<T>> {

        }
    }
}
