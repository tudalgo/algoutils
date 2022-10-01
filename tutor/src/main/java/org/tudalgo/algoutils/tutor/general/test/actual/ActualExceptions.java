package org.tudalgo.algoutils.tutor.general.test.actual;

public final class ActualExceptions {

    private static final ActualException<?> NOTHING = ActualException.of(null, false, s -> "no exception was thrown");
    private static final ActualException<?> UNEXPECTED = ActualException.of(null, false, s -> "unexpected exception was thrown");

    private ActualExceptions() {

    }

    public static <T extends Throwable> ActualException<T> noException() {
        //noinspection unchecked
        return (ActualException<T>) NOTHING;
    }

    public static <T extends Throwable> ActualException<T> unexpectedException() {
        //noinspection unchecked
        return (ActualException<T>) UNEXPECTED;
    }


}
