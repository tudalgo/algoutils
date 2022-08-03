package org.tudalgo.algoutils.algoutils.tutor.general.call;

import java.util.function.Supplier;

import static java.lang.String.format;

import org.opentest4j.AssertionFailedError;

/**
 * An instance of {@link ExceptionalResult} represents a call that resulted in an exception.
 *
 * @param <R> the type of expected result
 */
public class ExceptionalResult<R> implements Call<R> {

    private final Exception exception;

    public ExceptionalResult(Exception exception) {
        this.exception = exception;
    }

    @Override
    public <E extends Exception> E assertExceptional(
        Class<E> type,
        Supplier<String> messageSupplier
    ) throws AssertionFailedError {
        if (exception.getClass() == type) {
            //noinspection unchecked
            return (E) exception;
        }
        var additionalMessage = messageSupplier.get();
        var message = new StringBuilder();
        if (additionalMessage != null) {
            message.append(additionalMessage).append(": ");
        }
        message.append(
            format(
                "expected exception of type %s, but exception of type %s was thrown",
                type.getSimpleName(),
                exception.getClass().getSimpleName()
            )
        );
        throw new AssertionFailedError(message.toString(), exception);
    }

    @Override
    public R assertNormal(Supplier<String> messageSupplier) throws AssertionFailedError {
        var prefix = messageSupplier.get();
        var message = new StringBuilder();
        if (prefix != null) {
            message.append(prefix).append(": ");
        }
        message.append(format(
            "expected no exception, but exception of type %s was thrown", exception.getClass().getSimpleName())
        );
        throw new AssertionFailedError(message.toString(), exception);
    }
}
