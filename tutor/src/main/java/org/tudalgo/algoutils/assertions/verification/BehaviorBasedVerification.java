package org.tudalgo.algoutils.assertions.verification;

import org.tudalgo.algoutils.assertions.ActualWrapper;
import org.tudalgo.algoutils.assertions.AssertionResult;

import java.util.Set;
import java.util.function.Function;

/**
 * Helper class for defining behavior-based verifications.
 *
 * @param <E> the type of the expected value
 * @param <R> the type of the actual value returned by the verification
 */
public class BehaviorBasedVerification<E, R> implements AssertionVerification<E, Object, R> {

    private final Set<ActualWrapper.Type> supportedTypes = Set.of(ActualWrapper.Type.WRAPPED, ActualWrapper.Type.CALLABLE);
    private final Function<ActualWrapper<Object>, AssertionResult<E, R>> factory;

    private BehaviorBasedVerification(Function<ActualWrapper<Object>, AssertionResult<E, R>> factory) {
        this.factory = factory;
    }

    /**
     * Verifies the behavior of the code wrapped by {@code actual} and returns an {@link AssertionResult} containing the result of the verification.
     *
     * @param actual the code snipped to be verified, wrapped in an {@link ActualWrapper}
     * @throws IllegalArgumentException if the type of the given wrapper is not supported by this verification
     */
    @Override
    public AssertionResult<E, R> verify(ActualWrapper<Object> actual) {
        if (!supportedTypes.contains(actual.getType()))
            throw new IllegalArgumentException("Wrapper type %s is not supported by this verification".formatted(actual.getType()));
        return factory.apply(actual);
    }

    /**
     * Returns a new behavior-based verification defined by the given factory function.
     *
     * @param factory a function that takes an {@link ActualWrapper} and returns an {@link AssertionResult} containing the result of the verification
     * @return a new behavior-based verification defined by the given factory function
     * @param <E> the type of the expected value
     * @param <R> the type of the actual value returned by the verification
     */
    public static <E, R> AssertionVerification<E, Object, R> of(Function<ActualWrapper<Object>, AssertionResult<E, R>> factory) {
        return new BehaviorBasedVerification<>(factory);
    }
}
