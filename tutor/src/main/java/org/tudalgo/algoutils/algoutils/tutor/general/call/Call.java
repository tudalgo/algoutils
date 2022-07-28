package org.tudalgo.algoutils.algoutils.tutor.general.call;

import java.util.function.Supplier;

import org.opentest4j.AssertionFailedError;

public interface Call<R> {

    /**
     * Asserts that the call resulted in an exception of type {@code type} and returns the exception.
     * Otherwise, an {@link AssertionFailedError} containing the message supplied by {@code messageSupplier} will be thrown.
     * <br>
     * {@code messageSupplier} is allowed to supply {@code null}.
     *
     * @param type            the type of the expected exception
     * @param messageSupplier a supplier for the message if the exception was not thrown
     * @param <E>             the type of the expected exception
     * @return the exception resulted by the call
     * @throws AssertionFailedError if the call did not result in an exception of type {@code type}
     * @see Call#assertException(Class, String)
     * @see Call#assertException(Class)
     */
    <E extends Exception> E assertException(Class<E> type, Supplier<String> messageSupplier) throws AssertionFailedError;

    /**
     * Asserts that the call resulted in an exception of type {@code type} and returns the exception.
     * Otherwise, an {@link AssertionFailedError} containing {@code message} will be thrown.
     *
     * @param type    the type of the expected exception
     * @param message the message if the expected exception was not thrown
     * @param <E>     the type of the expected exception
     * @return the exception resulted by the call
     * @throws AssertionFailedError if the call did not result in an exception of type {@code type}
     */
    default <E extends Exception> E assertException(Class<E> type, String message) throws AssertionFailedError {
        return assertException(type, () -> message);
    }

    /**
     * Asserts that the call resulted in an exception of type {@code type} and returns the exception.
     * Otherwise, an {@link AssertionFailedError} will be thrown.
     *
     * @param type the type of the expected exception
     * @param <E>  the type of the expected exception
     * @return the exception resulted by the call
     * @throws AssertionFailedError if the call did not result in an exception of type {@code type}
     */
    default <E extends Exception> E assertException(Class<E> type) throws AssertionFailedError {
        return assertException(type, () -> null);
    }

    /**
     * Asserts that the call resulted in a result of type {@code type} and returns the result.
     * Otherwise, an {@link AssertionFailedError} containing the message supplied by {@code messageSupplier} will be thrown.
     * <br>
     * {@code messageSupplier} is allowed to supply {@code null}.
     *
     * @param messageSupplier a supplier for the message if the exception was not thrown
     * @return the result resulted by the call
     * @throws AssertionFailedError if the call resulted in an exception
     */
    R assertResult(Supplier<String> messageSupplier) throws AssertionFailedError;

    /**
     * Asserts that the call resulted in a result of type {@code type} and returns the result.
     * Otherwise, an {@link AssertionFailedError} containing {@code message} will be thrown.
     *
     * @param message the message if the expected exception was not thrown
     * @return the result resulted by the call
     * @throws AssertionFailedError if the call resulted in an exception
     */
    default R assertResult(String message) throws AssertionFailedError {
        return assertResult(() -> message);
    }

    /**
     * Asserts that the call resulted in a result of type {@code type} and returns the result.
     * Otherwise, an {@link AssertionFailedError} will be thrown.
     *
     * @return the result resulted by the call
     * @throws AssertionFailedError if the call resulted in an exception
     */
    default R assertResult() throws AssertionFailedError {
        return assertResult(() -> null);
    }
}
