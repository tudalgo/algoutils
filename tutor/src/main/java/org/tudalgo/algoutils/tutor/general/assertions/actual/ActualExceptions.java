package org.tudalgo.algoutils.tutor.general.assertions.actual;

/**
 * <p>A collection of methods for building {@linkplain ActualException actual exceptions}.</p>
 *
 * @author Dustin Glaser
 */
public final class ActualExceptions {

    private static final ActualException<?> NOTHING = ActualException.of(null, s -> "no exception was thrown");
    private static final ActualException<?> UNEXPECTED = ActualException.of(null, s -> "unexpected exception was thrown");

    // no instantiation allowed
    private ActualExceptions() {

    }

    /**
     * <p>Returns a behavior where no exception was thrown.</p>
     *
     * @param <T> the type of the expected exception
     * @return the behavior
     */
    public static <T extends Throwable> ActualException<T> noException() {
        //noinspection unchecked
        return (ActualException<T>) NOTHING;
    }

    /**
     * <p>Returns a behavior where an unexpected exception was thrown.</p>
     *
     * @param <T> the type of the expected exception
     * @return the behavior
     */
    public static <T extends Throwable> ActualException<T> unexpectedException() {
        //noinspection unchecked
        return (ActualException<T>) UNEXPECTED;
    }
}
