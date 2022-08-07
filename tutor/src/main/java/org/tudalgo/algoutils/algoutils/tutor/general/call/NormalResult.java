package org.tudalgo.algoutils.algoutils.tutor.general.call;

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

    public NormalResult(R result) {
        this.result = result;
    }

    @Override
    public <T extends Throwable> T assertThrows(
        Class<T> type,
        Supplier<String> messageSupplier
    ) throws AssertionFailedError {
        var prefix = messageSupplier.get();
        var message = new StringBuilder();
        if (prefix != null) {
            message.append(prefix).append(": ");
        }
        message.append(format("expected throwable of type %s, but no throwable was thrown", type.getSimpleName()));
        throw new AssertionFailedError(message.toString());
    }

    @Override
    public R assertNormal(Supplier<String> messageSupplier) throws AssertionFailedError {
        return result;
    }
}
