package org.tudalgo.algoutils.tutor.general;

import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.student.CrashException;
import org.tudalgo.algoutils.tutor.general.call.Call;
import org.tudalgo.algoutils.tutor.general.call.NormalResult;
import org.tudalgo.algoutils.tutor.general.call.ThrowableResult;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class TutorAssertions {

    /**
     * Shortcut for  {@link #call(Callable)} and {@link Call#assertNormal(Supplier)}.
     *
     * @param callable        see {@link #call(Callable)}
     * @param messageSupplier see {@link Call#assertNormal(Supplier)}
     * @param <R>             see {@link Call#assertNormal(Supplier)}
     * @return see {@link Call#assertNormal(Supplier)}
     */
    public static <R> R assertNormal(final Callable<R> callable, final Supplier<String> messageSupplier) {
        return call(callable).assertNormal(messageSupplier);
    }

    /**
     * Shortcut for {@link #call(Callable)} and {@link Call#assertNormal(Supplier)}.
     *
     * @param runnable        see {@link #call(Callable)}
     * @param messageSupplier see {@link Call#assertNormal(Supplier)}
     */
    public static void assertNormal(final Runnable runnable, final Supplier<String> messageSupplier) {
        call(runnable).assertNormal(messageSupplier);
    }

    /**
     * Shortcut for  {@link #call(Callable)} and {@link Call#assertNormal(String)}.
     *
     * @param callable see {@link #call(Callable)}
     * @param message  see {@link Call#assertNormal(String)}
     * @param <R>      see {@link Call#assertNormal(String)}
     * @return see {@link Call#assertNormal(String)}
     */
    public static <R> R assertNormal(final Callable<R> callable, final String message) {
        return call(callable).assertNormal(message);
    }

    /**
     * Shortcut for {@link #call(Runnable)} and {@link Call#assertNormal(String)}.
     *
     * @param runnable see {@link #call(Runnable)}
     * @param message  see {@link Call#assertNormal(String)}
     */
    public static void assertNormal(final Runnable runnable, final String message) {
        call(runnable).assertNormal(message);
    }

    /**
     * Shortcut for {@link #call(Callable)} and {@link Call#assertNormal()}.
     *
     * @param callable see {@link #call(Callable)}
     * @param <R>      see {@link Call#assertNormal()}
     * @return see {@link Call#assertNormal()}
     */
    public static <R> R assertNormal(final Callable<R> callable) {
        return call(callable).assertNormal();
    }

    /**
     * Shortcut for {@link #call(Runnable)} and {@link Call#assertNormal()}.
     *
     * @param runnable see {@link #call(Runnable)}
     */
    public static void assertNormal(final Runnable runnable) {
        call(runnable).assertNormal();
    }

    /**
     * Shortcut for {@link #call(Callable)} and {@link Call#assertThrows(Class, Supplier)}.
     *
     * @param callable        see {@link #call(Callable)}
     * @param messageSupplier see {@link Call#assertThrows(Class, Supplier)}
     * @param <T>             see {@link Call#assertThrows(Class, Supplier)}
     * @return see {@link Call#assertThrows(Class, Supplier)}
     */
    public static <T extends Throwable> T assertThrows(
        final Callable<?> callable,
        final Class<T> type,
        final Supplier<String> messageSupplier
    ) {
        return call(callable).assertThrows(type, messageSupplier);
    }

    /**
     * Shortcut for  {@link #call(Runnable)} and {@link Call#assertThrows(Class, String)}.
     *
     * @param runnable        see {@link #call(Callable)}
     * @param type            see {@link Call#assertThrows(Class, Supplier)}
     * @param messageSupplier see {@link Call#assertThrows(Class, Supplier)}
     * @param <T>             see {@link Call#assertThrows(Class, Supplier)}
     * @return see {@link Call#assertThrows(Class, Supplier)}
     */
    public static <T extends Throwable> T assertThrows(final Runnable runnable, final Class<T> type, final Supplier<String> messageSupplier) {
        return call(runnable).assertThrows(type, messageSupplier);
    }

    /**
     * Shortcut for  {@link #call(Callable)} and {@link Call#assertThrows(Class, String)}.
     *
     * @param callable see {@link #call(Callable)}
     * @param type     see {@link Call#assertThrows(Class, String)}
     * @param message  see {@link Call#assertThrows(Class, String)}
     * @param <T>      see {@link Call#assertThrows(Class, String)}
     * @return see {@link Call#assertThrows(Class, String)}
     */
    public static <T extends Throwable> T assertThrows(final Callable<?> callable, final Class<T> type, final String message) {
        return call(callable).assertThrows(type, message);
    }

    /**
     * Shortcut for  {@link #call(Runnable)} and {@link Call#assertThrows(Class, String)}.
     *
     * @param runnable see {@link #call(Runnable)}
     * @param type     see {@link Call#assertThrows(Class, String)}
     * @param message  see {@link Call#assertThrows(Class, String)}
     * @param <T>      see {@link Call#assertThrows(Class, String)}
     * @return see {@link Call#assertThrows(Class, String)}
     */
    public static <T extends Throwable> T assertThrows(final Runnable runnable, final Class<T> type, final String message) {
        return call(runnable).assertThrows(type, message);
    }

    /**
     * Shortcut for  {@link #call(Callable)} and {@link Call#assertThrows(Class)}.
     *
     * @param callable see {@link #call(Callable)}
     * @param type     see {@link Call#assertThrows(Class)}
     * @param <E>      see {@link Call#assertThrows(Class)}
     * @return see {@link Call#assertThrows(Class)}
     */
    public static <E extends Throwable> E assertThrows(final Callable<?> callable, final Class<E> type) {
        return call(callable).assertThrows(type);
    }

    /**
     * Shortcut for  {@link #call(Runnable)} and {@link Call#assertThrows(Class)}.
     *
     * @param runnable see {@link #call(Runnable)}
     * @param type     see {@link Call#assertThrows(Class)}
     * @param <E>      see {@link Call#assertThrows(Class)}
     * @return see {@link Call#assertThrows(Class)}
     */
    public static <E extends Throwable> E assertThrows(final Runnable runnable, final Class<E> type) {
        return call(runnable).assertThrows(type);
    }

    /**
     * Calls {@code callable} and returns a corresponding {@link Call} object.
     * If the call of {@code callable} results in an {@link CrashException}, a throwable of type {@link AssertionFailedError} with message <i>not implemented</i> will be thrown directly.
     *
     * @param callable the callable to call
     * @param <R>      the type of the object returned by {@code callable}
     * @return a {@link Call} object representing the callable
     */
    public static <R> Call<R> call(final Callable<R> callable) {
        try {
            return new NormalResult<>(callable.call());
        } catch (final CrashException exception) {
            throw new AssertionFailedError("not implemented");
        } catch (final Exception exception) {
            return new ThrowableResult<>(exception);
        }
    }

    /**
     * Calls {@code runnable} and returns a corresponding {@link Call} object.
     * If the call of {@code runnable} results in an {@link CrashException}, a throwable of type {@link AssertionFailedError} with message <i>not implemented</i> will be thrown directly.
     *
     * @param runnable the runnable to call
     * @return a {@link Call} object representing the callable
     */
    public static Call<Void> call(final Runnable runnable) {
        return call(() -> {
            runnable.run();
            return null;
        });
    }
}
