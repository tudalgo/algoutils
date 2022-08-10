package org.tudalgo.algoutils.tutor.general.call;

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

    public ThrowableResult(final Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public <T extends Throwable> T assertThrows(
        final Class<T> type,
        final Supplier<String> messageSupplier
    ) throws AssertionFailedError {
        if (this.throwable.getClass() == type) {
            //noinspection unchecked
            return (T) this.throwable;
        }
        final var additionalMessage = messageSupplier.get();
        final var message = new StringBuilder();
        if (additionalMessage != null) {
            message.append(additionalMessage).append(": ");
        }
        message.append(
            format(
                "expected throwable of type %s, but throwable of type %s was thrown",
                type.getSimpleName(),
                this.throwable.getClass().getSimpleName()
            )
        );
        throw new AssertionFailedError(message.toString(), this.throwable);
    }

    @Override
    public R assertNormal(final Supplier<String> messageSupplier) throws AssertionFailedError {
        final var prefix = messageSupplier.get();
        final var message = new StringBuilder();
        if (prefix != null) {
            message.append(prefix).append(": ");
        }
        message.append(format(
            "expected no throwable, but throwable of type %s was thrown", this.throwable.getClass().getSimpleName())
        );
        throw new AssertionFailedError(message.toString(), this.throwable);
    }
}
