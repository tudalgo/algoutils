package org.tudalgo.algoutils.assertions.options;

import org.tudalgo.algoutils.assertions.Context;

import java.util.function.Function;

import static org.tudalgo.algoutils.assertions.options.AssertionOption.inline;


/**
 * A collection of basic {@link AssertionOption assertion options} useful for a variety of usages.
 */
public final class BasicOptions {

    // Do not instantiate
    private BasicOptions() {}

    /**
     * Sets the assertion's fail message to the given string.
     *
     * @param failMessage the fail message
     * @return the option object
     * @param <E> the type of the expected value
     * @param <R> the type of the actual value returned by the verification
     */
    public static <E, R> AssertionOption<E, R> withFailMessage(String failMessage) {
        return result -> inline(result, () -> result.getErrorBuilder().setMessage(failMessage));
    }

    /**
     * Provides context for this assertion (e.g. which method was called, the arguments it was invoked with).
     *
     * @param context the context to be added to the assertion's error message
     * @return the option object
     * @param <E> the type of the expected value
     * @param <R> the type of the actual value returned by the verification
     */
    public static <E, R> AssertionOption<E, R> withContext(Context context) {
        return result -> inline(result, () ->
            result.getErrorBuilder().appendMessage("\n").appendMessage(context.toString()));
    }

    /**
     * Replaces the string representation of the expected value in the assertion error with the given string.
     *
     * @param expectedString the string to use instead of the original
     * @return the option object
     * @param <E> the type of the expected value
     * @param <R> the type of the actual value returned by the verification
     */
    public static <E, R> AssertionOption<E, R> withExpectedString(String expectedString) {
        return result -> inline(result, () ->
            result.getErrorBuilder().setExpectedStringRepresentation(expectedString));
    }

    /**
     * Replaces the string representation of the actual value in the assertion error with the given string.
     *
     * @param actualString the string to use instead of the original
     * @return the option object
     * @param <E> the type of the expected value
     * @param <R> the type of the actual value returned by the verification
     */
    public static <E, R> AssertionOption<E, R> withActualString(String actualString) {
        return result -> inline(result, () ->
            result.getErrorBuilder().setActualStringRepresentation(actualString));
    }

    /**
     * Replaces the error messages' formatter with the given function.
     *
     * @param formatter the formatter to use
     * @return the option object
     * @param <E> the type of the expected value
     * @param <R> the type of the actual value returned by the verification
     */
    public static <E, R> AssertionOption<E, R> withFormatter(Function<String, String> formatter) {
        return result -> inline(result, () ->
            result.getErrorBuilder().setFormatter(formatter));
    }
}
