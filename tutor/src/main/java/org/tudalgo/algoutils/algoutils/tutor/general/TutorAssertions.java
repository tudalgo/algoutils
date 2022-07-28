package org.tudalgo.algoutils.algoutils.tutor.general;

import java.util.concurrent.Callable;

import org.tudalgo.algoutils.student.CrashException;
import org.tudalgo.algoutils.algoutils.tutor.general.call.Call;
import org.tudalgo.algoutils.algoutils.tutor.general.call.CallImpl;
import org.opentest4j.AssertionFailedError;

public class TutorAssertions {

    /**
     * Calls {@code callable} and returns a corresponding {@link Call} object.
     * If the call of {@code callable} results in an {@link CrashException}, an exception of type {@link AssertionFailedError} with message <i>not implemented</i> will be thrown directly.
     *
     * @param callable the callable to call
     * @param <R>      the type of the object returned by {@code callable}
     * @return a {@link Call} object representing the callable
     */
    public static <R> Call<R> call(Callable<R> callable) {
        try {
            return new CallImpl.NormalResult<>(callable.call());
        } catch (CrashException exception) {
            throw new AssertionFailedError("not implemented");
        } catch (Exception exception) {
            return new CallImpl.ExceptionalResult<>(exception);
        }
    }
}
