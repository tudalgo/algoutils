package org.tudalgo.algoutils.tutor.general.call;

import java.util.function.Supplier;

import static java.lang.String.format;

import org.opentest4j.AssertionFailedError;

/**
 * An instance of {@link NormalResult} represents a call that resulted in no exception.
 *
 * @param <R> the type of expected result
 */
public class NormalResult<R> implements Call<R> {

    private final R result;

    public NormalResult(final R result) {
        this.result = result;
    }

    @Override
    public <T extends Throwable> T assertThrows(
            final Class<T> type,
            final Supplier<String> messageSupplier
    ) throws AssertionFailedError {
        final var prefix = messageSupplier.get();
        final var message = new StringBuilder();
        if (prefix != null) {
            message.append(prefix).append(": ");
        }
        message.append(format("expected throwable of type %s, but no throwable was thrown", type.getSimpleName()));
        throw new AssertionFailedError(message.toString());
    }

    @Override
    public R assertNormal(final Supplier<String> messageSupplier) throws AssertionFailedError {
        return this.result;
    }
}
