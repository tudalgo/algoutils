package org.tudalgo.algoutils.assertions.verification;

import org.tudalgo.algoutils.assertions.ActualWrapper;
import org.tudalgo.algoutils.assertions.Assertions;
import org.tudalgo.algoutils.assertions.AssertionResult;

import java.util.Set;
import java.util.function.Function;

/**
 * Helper class for defining value-based verifications.
 *
 * @param <E> the type of the expected value
 * @param <A> the type of the actual value passed to the verification
 * @param <R> the type of the actual value returned by the verification
 */
public class ValueBasedVerification<E, A, R> implements AssertionVerification<E, A, R> {

    private final Set<ActualWrapper.Type> supportedTypes = Set.of(ActualWrapper.Type.OBJECT, ActualWrapper.Type.WRAPPED);
    private final Function<ActualWrapper<A>, AssertionResult<E, R>> factory;

    private ValueBasedVerification(Function<ActualWrapper<A>, AssertionResult<E, R>> factory) {
        this.factory = factory;
    }

    /**
     * @throws IllegalArgumentException if the type of the given wrapper is not supported by this verification
     */
    @Override
    public final AssertionResult<E, R> verify(ActualWrapper<A> actual) {
        if (!supportedTypes.contains(actual.getType()))
            throw new IllegalArgumentException("Wrapper type %s is not supported by this verification".formatted(actual.getType()));
        return factory.apply(actual);
    }

    /**
     * Returns a new value-based verification defined by the given factory function.
     *
     * @param factory a function that takes an {@link ActualWrapper} and returns an {@link AssertionResult} containing the result of the verification
     * @return a new value-based verification defined by the given factory function
     * @param <E> the type of the expected value
     * @param <A> the type of the actual value
     */
    public static <E, A, R> AssertionVerification<E, A, R> of(Function<ActualWrapper<A>, AssertionResult<E, R>> factory) {
        return new ValueBasedVerification<>(factory);
    }

    /**
     * A helper method that handles exceptions thrown by the given expression.
     * If any exception or error is thrown by the given expression, it is wrapped in an
     * {@link org.opentest4j.AssertionFailedError} with a message indicating that an unexpected exception occurred,
     * and then thrown.
     *
     * @param expression the expression to be evaluated and wrapped in an {@link AssertionResult}
     * @return the result of the given expression wrapped in an {@link AssertionResult}
     * @param <E> the type of the expected value
     * @param <R> the type of the actual value returned by the verification
     * @throws org.opentest4j.AssertionFailedError if the given expression throws any exception or error
     */
    public static <E, R> AssertionResult<E, R> wrap(ActualWrapper.Expression<AssertionResult<E, R>> expression) {
        try {
            return expression.evaluate();
        } catch (Throwable t) {
            throw Assertions.failBuilder("An unexpected exception was thrown").setCause(t).build();
        }
    }
}
