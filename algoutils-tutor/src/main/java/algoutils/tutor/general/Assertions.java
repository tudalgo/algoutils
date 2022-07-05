package algoutils.tutor.general;

import java.util.concurrent.Callable;

import algoutils.student.CrashException;
import org.opentest4j.AssertionFailedError;

public class Assertions {

    /**
     * Calls the {@link Callable} specified by {@code callable} and returns the result.
     * If {@code callable} throws an exception of type {@link CrashException}, an exception of type {@link AssertionFailedError}
     * will be thrown calling that there is no implementation.
     * If {@code callable} throws an exception of another type, the exception will be rethrown.
     *
     * @param callable the {@link Callable} to call
     * @param <R>      the type of the object returned by {@code callable}
     * @return the object returned by {@code callable}
     * @throws Exception the exception thrown by {@code callable} or {@link AssertionFailedError}
     */
    public <R> R assertImplemented(Callable<R> callable) throws Exception {
        try {
            return callable.call();
        } catch (CrashException exception) {
            throw new AssertionFailedError("not implemented");
        }
    }
}
