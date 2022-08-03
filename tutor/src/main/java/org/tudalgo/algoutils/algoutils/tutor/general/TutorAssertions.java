package org.tudalgo.algoutils.algoutils.tutor.general;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.algoutils.tutor.general.call.Call;
import org.tudalgo.algoutils.algoutils.tutor.general.call.NormalResult;
import org.tudalgo.algoutils.algoutils.tutor.general.call.ThrowableResult;
import org.tudalgo.algoutils.student.CrashException;

public class TutorAssertions {

    /**
     * Calls {@code callable} and returns a corresponding {@link Call} object.
     * If the call of {@code callable} results in an {@link CrashException}, a throwable of type {@link AssertionFailedError} with message <i>not implemented</i> will be thrown directly.
     *
     * @param callable the callable to call
     * @param <R>      the type of the object returned by {@code callable}
     * @return a {@link Call} object representing the callable
     */
    public static <R> Call<R> call(Callable<R> callable) {
        try {
            return new NormalResult<>(callable.call());
        } catch (CrashException exception) {
            throw new AssertionFailedError("not implemented");
        } catch (Exception exception) {
            return new ThrowableResult<>(exception);
        }
    }

    /**
     * Shortcut for  {@link #call(Callable)} and {@link Call#assertNormal(Supplier)}.
     *
     * @param callable        see {@link #call(Callable)}
     * @param messageSupplier see {@link Call#assertNormal(Supplier)}
     * @param <R>             see {@link Call#assertNormal(Supplier)}
     * @return see {@link Call#assertNormal(Supplier)}
     */
    public static <R> R assertNormal(Callable<R> callable, Supplier<String> messageSupplier) {
        return call(callable).assertNormal(messageSupplier);
    }

    /**
     * Shortcut for  {@link #call(Callable)} and {@link Call#assertNormal(String)}.
     *
     * @param callable see {@link #call(Callable)}
     * @param message  see {@link Call#assertNormal(String)}
     * @param <R>      see {@link Call#assertNormal(String)}
     * @return see {@link Call#assertNormal(String)}
     */
    public static <R> R assertNormal(Callable<R> callable, String message) {
        return call(callable).assertNormal(message);
    }

    /**
     * Shortcut for  {@link #call(Callable)} and {@link Call#assertNormal()}.
     *
     * @param callable see {@link #call(Callable)}
     * @param <R>      see {@link Call#assertNormal()}
     * @return see {@link Call#assertNormal()}
     */
    public static <R> R assertNormal(Callable<R> callable) {
        return call(callable).assertNormal();
    }

    /**
     * Shortcut for  {@link #call(Callable)} and {@link Call#assertThrows(Class, Supplier)}.
     *
     * @param callable        see {@link #call(Callable)}
     * @param messageSupplier see {@link Call#assertThrows(Class, Supplier)}
     * @param <E>             see {@link Call#assertThrows(Class, Supplier)}
     * @return see {@link Call#assertThrows(Class, Supplier)}
     */
    public static <E extends Throwable> E assertThrowable(Callable<?> callable, Class<E> type,
                                                          Supplier<String> messageSupplier) {
        return call(callable).assertThrows(type, messageSupplier);
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
    public static <T extends Throwable> T assertThrowable(Callable<?> callable, Class<T> type, String message) {
        return call(callable).assertThrows(type, message);
    }

    /**
     * Shortcut for  {@link #call(Callable)} and {@link Call#assertThrows(Class)}.
     *
     * @param callable see {@link #call(Callable)}
     * @param type     see {@link Call#assertThrows(Class)}
     * @param <E>      see {@link Call#assertThrows(Class)}
     * @return see {@link Call#assertThrows(Class)}
     */
    public static <E extends Throwable> E assertThrowable(Callable<?> callable, Class<E> type) {
        return call(callable).assertThrows(type);
    }
}
