package org.tudalgo.algoutils.assertions.verification;

import org.tudalgo.algoutils.assertions.ActualWrapper;
import org.tudalgo.algoutils.assertions.AssertionResult;
import org.tudalgo.algoutils.assertions.Assertions;
import org.tudalgo.algoutils.assertions.options.AssertionOption;

import java.util.*;

import static org.tudalgo.algoutils.assertions.verification.ValueBasedVerification.wrap;

/**
 * Provides basic assertion verifications that can be used to verify common assertions such as equality, nullity, type checks, and exception throwing.
 * These verifications can be used with the {@link org.tudalgo.algoutils.assertions.Assertions} class to assert properties of values and behaviors in tests.
 */
public final class BasicVerifications {

    // Do not instantiate
    private BasicVerifications() {}

    /**
     * Verifies that the actual value is {@code true}.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Boolean, Boolean, Boolean> isTrue() {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Boolean actualValue = actual.getValue();
            if (actualValue == null) {
                return AssertionResult.of(false, true, null,
                    "The actual value is null");
            } else {
                return AssertionResult.of(actualValue, true, actualValue,
                    "The actual value is not true");
            }
        }));
    }

    /**
     * Verifies that the actual value is {@code false}.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Boolean, Boolean, Boolean> isFalse() {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Boolean actualValue = actual.getValue();
            if (actualValue == null) {
                return AssertionResult.of(false, false, null,
                    "The actual value is null");
            } else {
                return AssertionResult.of(!actualValue, false, actualValue,
                    "The actual value is not false");
            }
        }));
    }

    /**
     * Verifies that the actual value is {@code null}.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <A> type of the actual value
     */
    public static <A> AssertionVerification<?, A, A> isNull() {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            A actualValue = actual.getValue();
            return AssertionResult.of(actualValue == null, null, actualValue,
                "The actual value is not null");
        }));
    }

    /**
     * Verifies that the actual value is not {@code null}.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <A> type of the actual value
     */
    public static <A> AssertionVerification<?, A, A> isNotNull() {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            A actualValue = actual.getValue();
            return AssertionResult.of(actualValue != null, "a non-null value", actualValue,
                "The actual value is null");
        }));
    }

    /**
     * Verifies that the actual value is equal to the given expected value.
     * Equality is tested using {@link Objects#equals(Object, Object)}.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param expected the value to compare against
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> type of the expected value
     * @param <A> type of the actual value
     */
    public static <E, A> AssertionVerification<E, A, A> isEqualTo(E expected) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            A actualValue = actual.getValue();
            return AssertionResult.of(Objects.equals(expected, actualValue), expected, actualValue,
                "The actual value does not equal the expected one");
        }));
    }

    /**
     * Verifies that the actual value is not equal to the given unexpected value.
     * Equality is tested using {@link Objects#equals(Object, Object)}.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param unexpected the value to compare against
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> type of the unexpected value
     * @param <A> type of the actual value
     */
    public static <E, A> AssertionVerification<E, A, A> isNotEqualTo(E unexpected) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            A actualValue = actual.getValue();
            return AssertionResult.of(!Objects.equals(unexpected, actualValue), unexpected, actualValue,
                "The actual value equals the unexpected value");
        }));
    }

    /**
     * Verifies that the actual value is the same as the given expected value.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param expected the value to compare against
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> type of the expected value
     * @param <A> type of the actual value
     */
    public static <E, A> AssertionVerification<E, A, A> isSameAs(E expected) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            A actualValue = actual.getValue();
            return AssertionResult.of(expected == actualValue, expected, actualValue,
                "The actual value is not the same as the expected one");
        }));
    }

    /**
     * Verifies that the actual value is not the same as the given unexpected value.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param unexpected the value to compare against
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> type of the unexpected value
     * @param <A> type of the actual value
     */
    public static <E, A> AssertionVerification<E, A, A> isNotSameAs(E unexpected) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            A actualValue = actual.getValue();
            return AssertionResult.of(unexpected != actualValue, unexpected, actualValue,
                "The actual value is the same as the unexpected one");
        }));
    }

    /**
     * Verifies that the actual array is equal to the given expected array.
     * Equality is tested using {@link Arrays#equals(Object[], Object[])}.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param expected the value to compare against
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> component type of the expected array
     * @param <A> component type of the actual array
     */
    public static <E, A> AssertionVerification<E[], A[], A[]> isEqualToArray(E[] expected) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            A[] actualValue = actual.getValue();
            return AssertionResult.of(Arrays.equals(expected, actualValue), expected, actualValue,
                "The actual array does not equal the expected one");
        }));
    }

    /**
     * Verifies that the actual array is not equal to the given unexpected array.
     * Equality is tested using {@link Arrays#equals(Object[], Object[])}.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param unexpected the value to compare against
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> component type of the unexpected array
     * @param <A> component type of the actual array
     */
    public static <E, A> AssertionVerification<E[], A[], A[]> isNotEqualToArray(E[] unexpected) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            A[] actualValue = actual.getValue();
            return AssertionResult.of(!Arrays.equals(unexpected, actualValue), unexpected, actualValue,
                "The actual array equals the unexpected one");
        }));
    }

    /**
     * Verifies that the actual value is an instance of the given type or a subtype of it.
     * In other words, it behaves in the same way as the {@code instanceof} operator.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param expected the expected type of the actual value
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> expected type or supertype
     */
    public static <E extends Class<?>> AssertionVerification<E, ?, Class<?>> isInstanceOf(E expected) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Object actualValue = actual.getValue();
            AssertionResult<E, Class<?>> result;
            if (actualValue == null) {
                result = AssertionResult.of(false, expected, null, "The actual value is null");
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("instance of %s or a subclass of it".formatted(expected.getName()));
            } else {
                result = AssertionResult.of(expected.isInstance(actualValue), expected, actualValue.getClass(),
                    "The actual value is not an instance of " + expected.getName());
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("instance of %s or a subclass of it".formatted(expected.getName()))
                    .setActualStringRepresentation("instance of " + actualValue.getClass().getName());
            }
            return result;
        }));
    }

    /**
     * Asserts that the actual value is an instance of the given type, not a subtype.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param expected the expected type of the actual value
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> expected type
     */
    public static <E extends Class<?>> AssertionVerification<E, ?, Class<?>> isInstanceOfExact(E expected) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Object actualValue = actual.getValue();
            AssertionResult<E, Class<?>> result;
            if (actualValue == null) {
                result = AssertionResult.of(false, expected, null, "The actual value is null");
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("instance of " + expected.getName());
            } else {
                result = AssertionResult.of(actualValue.getClass() == expected, expected, actualValue.getClass(),
                    "The actual value is not an instance of exactly " + expected.getName());
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("instance of " + expected.getName())
                    .setActualStringRepresentation("instance of " + actualValue.getClass().getName());
            }
            return result;
        }));
    }

    /**
     * Verifies that the actual value is not an instance of the given type or a subtype of it.
     * In other words, it behaves as a negation of the {@code instanceof} operator.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param unexpected the unexpected type of the actual value
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> unexpected type or supertype
     */
    public static <E extends Class<?>> AssertionVerification<E, ?, Class<?>> isNotInstanceOf(E unexpected) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Object actualValue = actual.getValue();
            AssertionResult<E, Class<?>> result = AssertionResult.of(!unexpected.isInstance(actualValue),
                unexpected,
                actualValue == null ? null : actualValue.getClass(),
                "The actual value is an instance of " + unexpected.getName());
            result.getErrorBuilder()
                .setExpectedStringRepresentation("not an instance of %s or a subclass of it".formatted(unexpected.getName()))
                .setActualStringRepresentation(actualValue == null ? "null" : "instance of " + actualValue.getClass().getName());
            return result;
        }));
    }

    /**
     * Asserts that the actual value's wrapper threw an exception and that it is an instance
     * of the given type or a subtype of it.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatStatement(ActualWrapper.Statement, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param expected the expected type of the thrown exception
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> type or supertype of the expected exception
     */
    public static <E extends Class<? extends Throwable>> AssertionVerification<E, ?, Class<? extends Throwable>> throwsException(E expected) {
        return BehaviorBasedVerification.of(actual -> {
            AssertionResult<E, Class<? extends Throwable>> result;
            try {
                actual.call();
                result = AssertionResult.of(false, expected, null, "No exception was thrown");
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("exception of type %s or subclass of it".formatted(expected.getName()));
            } catch (Throwable t) {
                result = AssertionResult.of(expected.isInstance(t), expected, t.getClass(),
                    "The type of the thrown exception is not an instance of %s or a subclass of it".formatted(expected.getName()));
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("exception of type %s or subclass of it".formatted(expected.getName()))
                    .setActualStringRepresentation("exception of type " + t.getClass().getName())
                    .setCause(t);
            }
            return result;
        });
    }

    /**
     * Asserts that the actual value's wrapper threw an exception and that it is an instance
     * of the given type, not a subtype.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatStatement(ActualWrapper.Statement, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param expected the expected type of the thrown exception
     * @return an {@link AssertionVerification} object to verify the assertion
     * @param <E> type of the expected exception
     */
    public static <E extends Class<? extends Throwable>> AssertionVerification<E, ?, Class<? extends Throwable>> throwsExceptionExact(E expected) {
        return BehaviorBasedVerification.of(actual -> {
            AssertionResult<E, Class<? extends Throwable>> result;
            try {
                actual.call();
                result = AssertionResult.of(false, expected, null, "No exception was thrown");
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("exception of type " + expected.getName());

            } catch (Throwable t) {
                result = AssertionResult.of(t.getClass() == expected, expected, t.getClass(),
                    "The type of the thrown exception is not an instance of exactly " + expected.getName());
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("exception of type " + expected.getName())
                    .setActualStringRepresentation("exception of type " + t.getClass().getName())
                    .setCause(t);
            }
            return result;
        });
    }

    /**
     * Asserts that the actual value was obtained without an exception being thrown.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatStatement(ActualWrapper.Statement, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Object, Class<? extends Throwable>> doesNotThrow() {
        return BehaviorBasedVerification.of(actual -> {
            AssertionResult<?, Class<? extends Throwable>> result;
            try {
                actual.call();
                result = AssertionResult.of(true);
            } catch (Throwable t) {
                result = AssertionResult.of(false, null, t.getClass(), "An exception was thrown");
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("no exception to be thrown")
                    .setActualStringRepresentation("exception of type " + t.getClass().getName())
                    .setCause(t);
            }
            return result;
        });
    }
}
