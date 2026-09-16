package org.tudalgo.algoutils.assertions;

import java.util.Optional;

/**
 * Basic implementation of the {@link AssertionResult} interface.
 *
 * @param <E> the type of the expected value
 * @param <R> the type of the actual value returned by the verification
 */
public class AssertionResultImpl<E, R> implements AssertionResult<E, R> {

    private final boolean successful;
    private final E expected;
    private final R actual;
    private final ErrorBuilder errorBuilder;

    /**
     * Constructs a new instance of {@link AssertionResultImpl} with the given success, expected and actual values as well as fail message.
     *
     * @param successful    whether the assertion was successful or not
     * @param expected      the expected value of the assertion, can be {@code null} if there is no expected value
     * @param actual        the actual value of the assertion, can be {@code null} if there is no actual value
     * @param valuesPresent whether {@code expected} and {@code actual} are present ({@code true} includes them in the error message, {@code false} doesn't)
     * @param failMessage   the message to be used in case the assertion failed
     */
    public AssertionResultImpl(boolean successful, E expected, R actual, boolean valuesPresent, String failMessage) {
        this.successful = successful;
        this.expected = expected;
        this.actual = actual;
        this.errorBuilder = (valuesPresent ? ErrorBuilder.builder(expected, actual) : ErrorBuilder.builder()).setMessage(failMessage);
    }

    @Override
    public boolean successful() {
        return successful;
    }

    @Override
    public Optional<E> getExpected() {
        return Optional.ofNullable(expected);
    }

    @Override
    public Optional<R> getActual() {
        return Optional.ofNullable(actual);
    }

    @Override
    public ErrorBuilder getErrorBuilder() {
        return errorBuilder;
    }
}
