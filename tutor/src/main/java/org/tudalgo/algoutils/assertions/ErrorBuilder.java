package org.tudalgo.algoutils.assertions;

import org.opentest4j.AssertionFailedError;

import java.util.Objects;
import java.util.function.Function;

/**
 * Builder for {@link AssertionFailedError} objects.
 * This class is used to build an {@link AssertionFailedError} object with a custom message and optionally cause and expected / actual values.
 *
 * <p>
 *     Instead of the stringified expected and actual values, custom string representations can be used to better describe
 *     what was actually required by the assertion and what was provided.
 * </p>
 */
public class ErrorBuilder {

    private static final Function<String, String> DEFAULT_FORMATTER = s -> s;

    private final StringBuilder failMessageBuilder;
    private boolean valuePresent;
    private String expectedStringRepresentation;
    private String actualStringRepresentation;
    private Throwable cause;
    private Function<String, String> formatter = DEFAULT_FORMATTER;

    private ErrorBuilder(Object expected, Object actual, boolean valuePresent) {
        this.failMessageBuilder = new StringBuilder(64);
        this.valuePresent = valuePresent;
        if (valuePresent) {
            this.expectedStringRepresentation = Objects.toString(expected);
            this.actualStringRepresentation = Objects.toString(actual);
        }
    }

    /**
     * Returns a new instance of {@link ErrorBuilder} without any expected or actual values.
     *
     * @return a new instance of {@link ErrorBuilder}
     */
    public static ErrorBuilder builder() {
        return new ErrorBuilder(null, null, false);
    }

    /**
     * Returns a new instance of {@link ErrorBuilder} with expected and / or actual values.
     *
     * @return a new instance of {@link ErrorBuilder}
     */
    public static ErrorBuilder builder(Object expected, Object actual) {
        return new ErrorBuilder(expected, actual, true);
    }

    /**
     * Overrides the string representation of the expected value in error message with the given string.
     * If this builder was configured without any initial expected or actual values,
     * calling this method will make the resulting error include both.
     * The {@code expected} value will be set to the given string and {@code actual} might default to {@code null}.
     *
     * @param expectedStringRepresentation the string representation of the expected value to be used in the error message
     * @return this builder
     */
    public ErrorBuilder setExpectedStringRepresentation(String expectedStringRepresentation) {
        this.valuePresent = true;
        this.expectedStringRepresentation = expectedStringRepresentation;
        return this;
    }

    /**
     * Overrides the string representation of the actual value in error message with the given string.
     * If this builder was configured without any initial expected or actual values,
     * calling this method will make the resulting error include both.
     * The {@code actual} value will be set to the given string and {@code expected} might default to {@code null}.
     *
     * @param actualStringRepresentation the string representation of the actual value to be used in the error message
     * @return this builder
     */
    public ErrorBuilder setActualStringRepresentation(String actualStringRepresentation) {
        this.valuePresent = true;
        this.actualStringRepresentation = actualStringRepresentation;
        return this;
    }

    /**
     * Returns the current error message.
     *
     * @return the current error message
     */
    public String getMessage() {
        return this.failMessageBuilder.toString();
    }

    /**
     * Sets the error message to the given string.
     *
     * @param message the error message to use
     * @return this builder
     */
    public ErrorBuilder setMessage(String message) {
        failMessageBuilder.setLength(0);
        failMessageBuilder.append(message);
        return this;
    }

    /**
     * Appends the given string to the current error message.
     *
     * @param message the string to append
     * @return this builder
     */
    public ErrorBuilder appendMessage(String message) {
        failMessageBuilder.append(message);
        return this;
    }

    /**
     * Sets the underlying cause of the error to be the given throwable.
     *
     * @param cause the throwable to use
     * @return this builder
     */
    public ErrorBuilder setCause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    /**
     * Sets the formatter that will be used to format the error message in the error returned by {@link #build()}.
     *
     * @param formatter the formatter to use
     * @return this builder
     */
    public ErrorBuilder setFormatter(Function<String, String> formatter) {
        this.formatter = Objects.requireNonNull(formatter, "formatter must not be null");
        return this;
    }

    /**
     * Builds an {@link AssertionFailedError} object with the current state of this builder.
     * Depending on the configuration of this builder the error might or might not include the {@code expected} and {@code actual} values
     * as well as the underlying cause.
     *
     * @return an {@link AssertionFailedError} object with the current state of this builder
     */
    public AssertionFailedError build() {
        String formattedMessage = formatter.apply(failMessageBuilder.toString());
        if (valuePresent) {
            if (cause == null) {
                return new AssertionFailedError(formattedMessage, expectedStringRepresentation, actualStringRepresentation);
            } else {
                return new AssertionFailedError(formattedMessage, expectedStringRepresentation, actualStringRepresentation, cause);
            }
        } else {
            if (cause == null) {
                return new AssertionFailedError(formattedMessage);
            } else {
                return new AssertionFailedError(formattedMessage, cause);
            }
        }
    }
}
