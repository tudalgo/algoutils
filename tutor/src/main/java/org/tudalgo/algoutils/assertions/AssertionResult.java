package org.tudalgo.algoutils.assertions;

import java.util.Optional;

/**
 * This interface represents the result of an assertion / a verification.
 * It holds information that is passed from the verification to the actual assertion.
 *
 * @param <E> the type of the expected value
 * @param <R> the type of the actual value returned by the verification
 */
public interface AssertionResult<E, R> {

    /**
     * Returns whether the assertion was successful or not.
     *
     * @return {@code true} if the assertion was successful and {@code false} otherwise
     */
    boolean successful();

    /**
     * Returns an optional which wraps the expected value.
     *
     * @return an optional which wraps the expected value
     */
    Optional<E> getExpected();

    /**
     * Returns an optional which wraps the actual value.
     *
     * @return an optional which wraps the actual value
     */
    Optional<R> getActual();

    /**
     * Returns the {@link ErrorBuilder} that is used in case the assertion failed.
     *
     * @return the {@link ErrorBuilder} that is used in case the assertion failed
     */
    ErrorBuilder getErrorBuilder();

    /**
     * Constructs an {@link AssertionResult} with the given success value.
     *
     * @param successful whether the assertion was successful or not
     * @param <E>        the type of the expected value
     * @param <R>        the type of the actual value returned by the verification
     * @return an {@link AssertionResult} defined by the given values
     */
    static <E, R> AssertionResult<E, R> of(boolean successful) {
        return of(successful, "Assertion failed");
    }

    /**
     * Constructs an {@link AssertionResult} with the given success value as well as fail message.
     *
     * @param successful  whether the assertion was successful or not
     * @param failMessage the message to use if the assertion failed
     * @param <E>         the type of the expected value
     * @param <R>         the type of the actual value returned by the verification
     * @return an {@link AssertionResult} defined by the given values
     */
    static <E, R> AssertionResult<E, R> of(boolean successful, String failMessage) {
        return new AssertionResultImpl<>(successful, null, null, false, failMessage);
    }

    /**
     * Constructs an {@link AssertionResult} with the given success, expected and actual values.
     *
     * @param successful whether the assertion was successful or not
     * @param expected   the expected value of the assertion, can be {@code null} if there is no expected value
     * @param actual     the actual value of the assertion, can be {@code null} if there is no actual value
     * @param <E>        the type of the expected value
     * @param <R>        the type of the actual value returned by the verification
     * @return an {@link AssertionResult} defined by the given values
     */
    static <E, R> AssertionResult<E, R> of(boolean successful, E expected, R actual) {
        return of(successful, expected, actual, "Assertion failed");
    }

    /**
     * Constructs an {@link AssertionResult} with the given success, expected and actual values as well as fail message.
     *
     * @param successful  whether the assertion was successful or not
     * @param expected    the expected value of the assertion, can be {@code null} if there is no expected value
     * @param actual      the actual value of the assertion, can be {@code null} if there is no actual value
     * @param failMessage the message to use if the assertion failed
     * @param <E>         the type of the expected value
     * @param <R>         the type of the actual value returned by the verification
     * @return an {@link AssertionResult} defined by the given values
     */
    static <E, R> AssertionResult<E, R> of(boolean successful, E expected, R actual, String failMessage) {
        return new AssertionResultImpl<>(successful, expected, actual, true, failMessage);
    }
}
