package org.tudalgo.algoutils.tutor.general.call;

import java.util.function.Supplier;

import org.opentest4j.AssertionFailedError;

public interface Call<R> {

    /**
     * Asserts that the call resulted in a throwable of type {@code type} and returns the throwable.
     * Otherwise, an {@link AssertionFailedError} containing the message supplied by {@code messageSupplier} will be thrown.
     * <br>
     * {@code messageSupplier} is allowed to supply {@code null}.
     *
     * @param type            the type of the expected throwable
     * @param messageSupplier a supplier for the message if the throwable was not thrown
     * @param <E>             the type of the expected throwable
     * @return the throwable resulted by the call
     * @throws AssertionFailedError if the call did not result in a throwable of type {@code type}
     * @see Call#assertThrows(Class, String)
     * @see Call#assertThrows(Class)
     */
    <E extends Throwable> E assertThrows(Class<E> type, Supplier<String> messageSupplier) throws AssertionFailedError;

    /**
     * Asserts that the call resulted in a throwable of type {@code type} and returns the throwable.
     * Otherwise, an {@link AssertionFailedError} containing {@code message} will be thrown.
     *
     * @param type    the type of the expected throwable
     * @param message the message if the expected throwable was not thrown
     * @param <E>     the type of the expected throwable
     * @return the throwable resulted by the call
     * @throws AssertionFailedError if the call did not result in a throwable of type {@code type}
     */
    default <E extends Throwable> E assertThrows(final Class<E> type, final String message) throws AssertionFailedError {
        return assertThrows(type, () -> message);
    }

    /**
     * Asserts that the call resulted in n throwable of type {@code type} and returns the throwable.
     * Otherwise, an {@link AssertionFailedError} will be thrown.
     *
     * @param type the type of the expected throwable
     * @param <E>  the type of the expected throwable
     * @return the throwable resulted by the call
     * @throws AssertionFailedError if the call did not result in a throwable of type {@code type}
     */
    default <E extends Throwable> E assertThrows(final Class<E> type) throws AssertionFailedError {
        return assertThrows(type, () -> null);
    }

    /**
     * Asserts that the call resulted in a result of type {@code type} and returns the result.
     * Otherwise, an {@link AssertionFailedError} containing the message supplied by {@code messageSupplier} will be thrown.
     * <br>
     * {@code messageSupplier} is allowed to supply {@code null}.
     *
     * @param messageSupplier a supplier for the message if the throwable was not thrown
     * @return the result resulted by the call
     * @throws AssertionFailedError if the call resulted in a throwable
     */
    R assertNormal(Supplier<String> messageSupplier) throws AssertionFailedError;

    /**
     * Asserts that the call resulted in a result of type {@code type} and returns the result.
     * Otherwise, an {@link AssertionFailedError} containing {@code message} will be thrown.
     *
     * @param message the message if the expected throwable was not thrown
     * @return the result resulted by the call
     * @throws AssertionFailedError if the call resulted in a throwable
     */
    default R assertNormal(final String message) throws AssertionFailedError {
        return assertNormal(() -> message);
    }

    /**
     * Asserts that the call resulted in a result of type {@code type} and returns the result.
     * Otherwise, an {@link AssertionFailedError} will be thrown.
     *
     * @return the result resulted by the call
     * @throws AssertionFailedError if the call resulted in a throwable
     */
    default R assertNormal() throws AssertionFailedError {
        return assertNormal(() -> null);
    }
}
