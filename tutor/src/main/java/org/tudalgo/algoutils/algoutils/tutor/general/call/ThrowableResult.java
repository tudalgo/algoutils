package org.tudalgo.algoutils.algoutils.tutor.general.call;

import java.util.function.Supplier;

import static java.lang.String.format;

import org.opentest4j.AssertionFailedError;

/**
 * An instance of {@link ThrowableResult} represents a call that resulted in an {@link Throwable}.
 *
 * @param <R> the type of expected result
 */
public class ThrowableResult<R> implements Call<R> {

    private final Throwable throwable;

    public ThrowableResult(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public <T extends Throwable> T assertThrows(
        Class<T> type,
        Supplier<String> messageSupplier
    ) throws AssertionFailedError {
        if (throwable.getClass() == type) {
            //noinspection unchecked
            return (T) throwable;
        }
        var additionalMessage = messageSupplier.get();
        var message = new StringBuilder();
        if (additionalMessage != null) {
            message.append(additionalMessage).append(": ");
        }
        message.append(
            format(
                "expected throwable of type %s, but throwable of type %s was thrown",
                type.getSimpleName(),
                throwable.getClass().getSimpleName()
            )
        );
        throw new AssertionFailedError(message.toString(), throwable);
    }

    @Override
    public R assertNormal(Supplier<String> messageSupplier) throws AssertionFailedError {
        var prefix = messageSupplier.get();
        var message = new StringBuilder();
        if (prefix != null) {
            message.append(prefix).append(": ");
        }
        message.append(format(
            "expected no throwable, but throwable of type %s was thrown", throwable.getClass().getSimpleName())
        );
        throw new AssertionFailedError(message.toString(), throwable);
    }
}
