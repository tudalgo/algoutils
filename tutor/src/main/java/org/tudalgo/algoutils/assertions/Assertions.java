package org.tudalgo.algoutils.assertions;

import org.tudalgo.algoutils.assertions.options.AssertionOption;
import org.tudalgo.algoutils.assertions.options.SpecialOptions;
import org.tudalgo.algoutils.assertions.verification.AssertionVerification;

import java.util.*;

public final class Assertions {

    // Do not instantiate
    private Assertions() {}

    /**
     * Asserts that the given verification is successful.
     * This method does not check against an object, expression or statement, but rather against the state of the JVM (e.g., class exists).
     *
     * <p>
     * Options are applied in the same order they are passed to this method.
     * If two options are given and the first option appends to the error message, while the second option overwrites it completely,
     * only the second option's error message will be preserved.
     * </p>
     *
     * <p>Example usage:</p>
     * <blockquote><pre>
     * Context context = Context.of("abc", "def");
     * assertThat(classExists("java.lang.String"), withContext(context));
     * </pre></blockquote>
     *
     * @param verification an {@link AssertionVerification} object to check
     * @param options      options to modify the assertion
     * @param <E>          type of the expected value, depends on the verification used
     * @param <R>          the type of the actual value returned by the verification
     * @return an {@link AssertionResult} for this assertion
     * @throws org.opentest4j.AssertionFailedError if the verification is unsuccessful
     */
    @SafeVarargs
    public static <E, R> AssertionResult<E, R> assertThat(AssertionVerification<E, ?, R> verification, AssertionOption<E, R>... options) {
        return assertWithWrapper(ActualWrapper.ofObject(null), verification, options);
    }

    /**
     * Asserts that the actual value passes the given verification.
     *
     * <p>
     * Options are applied in the same order they are passed to this method.
     * If two options are given and the first option appends to the error message, while the second option overwrites it completely,
     * only the second option's error message will be preserved.
     * </p>
     *
     * <p>Example usage:</p>
     * <blockquote><pre>
     * String expected = "foo";
     * String actual = "bar";
     * Context context = Context.of("abc", "def");
     * assertThat(actual, isEqualTo(expected), withContext(context));
     * </pre></blockquote>
     *
     * @param actual       the object / value under test
     * @param verification an {@link AssertionVerification} object to verify correctness of {@code actual}
     * @param options      options to modify the assertion
     * @param <E>          type of the expected value, depends on the verification used
     * @param <A>          the type of the actual value passed to the verification
     * @param <R>          the type of the actual value returned by the verification
     * @return an {@link AssertionResult} for this assertion
     * @throws org.opentest4j.AssertionFailedError if {@code actual} does not pass verification
     */
    @SafeVarargs
    public static <E, A, R> AssertionResult<E, R> assertThat(A actual,
                                                             AssertionVerification<E, A, R> verification,
                                                             AssertionOption<E, R>... options) {
        return assertWithWrapper(ActualWrapper.ofObject(actual), verification, options);
    }

    /**
     * Asserts that the actual expression passes the given verification.
     *
     * <p>
     * Options are applied in the same order they are passed to this method.
     * If two options are given and the first option appends to the error message, while the second option overwrites it completely,
     * only the second option's error message will be preserved.
     * </p>
     *
     * <p>Example usage:</p>
     * <blockquote><pre>
     * String expected = "foo";
     * String actual = "bar";
     * Context context = Context.of("abc", "def");
     * assertThatExpression(() -> actual, isEqualTo(expected), withContext(context));
     * </pre></blockquote>
     *
     * @param actual       the expression under test
     * @param verification an {@link AssertionVerification} object to verify correctness of the result or behavior of {@code actual}
     * @param options      options to modify the assertion
     * @param <E>          type of the expected value, depends on the verification used
     * @param <A>          type of the value returned by the expression
     * @param <R>          the type of the actual value returned by the verification
     * @return an {@link AssertionResult} for this assertion
     * @throws org.opentest4j.AssertionFailedError if {@code actual} does not pass verification
     */
    @SafeVarargs
    public static <E, A, R> AssertionResult<E, R> assertThatExpression(ActualWrapper.Expression<A> actual,
                                                                       AssertionVerification<E, A, R> verification,
                                                                       AssertionOption<E, R>... options) {
        return assertWithWrapper(ActualWrapper.ofExpression(actual), verification, options);
    }

    /**
     * Asserts that the actual statement passes the given verification.
     *
     * <p>
     * Options are applied in the same order they are passed to this method.
     * If two options are given and the first option appends to the error message, while the second option overwrites it completely,
     * only the second option's error message will be preserved.
     * </p>
     *
     * <p>Example usage:</p>
     * <blockquote><pre>
     * String actual = "bar";
     * Context context = Context.of("abc", "def");
     * assertThatStatement(() -> System.out.println(actual), doesNotThrow(), withContext(context));
     * </pre></blockquote>
     *
     * @param actual       the statement under test
     * @param verification an {@link AssertionVerification} object to verify correctness of the behavior of {@code actual}
     * @param options      options to modify the assertion
     * @param <E>          type of the expected value, depends on the verification used
     * @param <R>          the type of the actual value returned by the verification
     * @return an {@link AssertionResult} for this assertion
     * @throws org.opentest4j.AssertionFailedError if {@code actual} does not pass verification
     */
    @SafeVarargs
    public static <E, R> AssertionResult<E, R> assertThatStatement(ActualWrapper.Statement actual,
                                                                   AssertionVerification<E, ?, R> verification,
                                                                   AssertionOption<E, R>... options) {
        return assertWithWrapper(ActualWrapper.ofStatement(actual), verification, options);
    }

    /**
     * Asserts that the value or behavior wrapped by {@code actual} passes the given verification.
     * You should probably use {@link #assertThat(Object, AssertionVerification, AssertionOption[])},
     * {@link #assertThatExpression} or {@link #assertThatStatement}.
     *
     * <p>
     * Options are applied in the same order they are passed to this method.
     * If two options are given and the first option appends to the error message, while the second option overwrites it completely,
     * only the second option's error message will be preserved.
     * </p>
     *
     * @param actual       the wrapped value / expression / statement under test
     * @param verification an {@link AssertionVerification} object to verify correctness of {@code actual}
     * @param options      options to modify the assertion
     * @param <E>          type of the expected value, depends on the verification used
     * @param <A>          type of the value produced by unwrapping {@code actual}
     * @param <R>          the type of the actual value returned by the verification
     * @return an {@link AssertionResult} for this assertion
     * @throws org.opentest4j.AssertionFailedError if {@code actual} does not pass verification
     */
    @SafeVarargs
    public static <E, A, R> AssertionResult<E, R> assertWithWrapper(ActualWrapper<A> actual,
                                                                    AssertionVerification<E, A, R> verification,
                                                                    AssertionOption<E, R>... options) {
        AssertionResult<E, R> result = verification.verify(actual);

        if (!result.successful()) {
            boolean throwException = true;
            for (AssertionOption<E, R> option : options) {
                if (option instanceof SpecialOptions.DontThrowOnFailOption<E, R>) {
                    throwException = false;
                }
                result = option.apply(result);
            }

            if (throwException) {
                throw result.getErrorBuilder().build();
            }
        }

        return result;
    }

    /**
     * Creates a generic {@link ErrorBuilder} with the given message.
     *
     * <p>
     * Options are applied in the same order they are passed to this method.
     * If two options are given and the first option appends to the error message, while the second option overwrites it completely,
     * only the second option's error message will be preserved.
     * </p>
     *
     * @param failMessage the message to initialize the builder with
     * @param options     options to modify the {@link ErrorBuilder}
     * @return a modifiable {@link ErrorBuilder}, initialized with the given message
     */
    @SafeVarargs
    public static ErrorBuilder failBuilder(String failMessage, AssertionOption<Object, Object>... options) {
        AssertionResult<Object, Object> result = AssertionResult.of(false, null, null, failMessage);
        for (AssertionOption<Object, Object> option : options) {
            result = option.apply(result);
        }
        return result.getErrorBuilder();
    }

    /**
     * A shorthand to fail the current test.
     *
     * <p>
     * Options are applied in the same order they are passed to this method.
     * If two options are given and the first option appends to the error message, while the second option overwrites it completely,
     * only the second option's error message will be preserved.
     * </p>
     *
     * <p>Example usage:</p>
     * <blockquote><pre>
     * String msg = "Verification failed";
     * Context context = Context.of("abc", "def");
     * fail(msg, withContext(context));
     * </pre></blockquote>
     *
     * @param failMessage the message to fail with
     * @param options     options to modify the underlying {@link ErrorBuilder}
     * @throws org.opentest4j.AssertionFailedError always throws this error
     */
    @SafeVarargs
    public static void fail(String failMessage, AssertionOption<Object, Object>... options) {
        throw failBuilder(failMessage, options).build();
    }
}
