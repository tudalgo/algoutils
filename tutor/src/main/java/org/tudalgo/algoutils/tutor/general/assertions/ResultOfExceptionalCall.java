package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedException;

import static org.tudalgo.algoutils.tutor.general.assertions.actual.ActualException.of;

/**
 * <p>A type representing the result of a {@linkplain TestOfExceptionalCall throwable call}.</p>
 *
 * @param <T> the type of the expected exception
 * @author Dustin Glaser
 */
public interface ResultOfExceptionalCall<T extends Exception> extends Result<ResultOfExceptionalCall<T>, ActualException<T>, TestOfExceptionalCall<T>, ExpectedException<T>> {

    /**
     * <p>If the test resulted in the {@linkplain TestOfExceptionalCall#expected()} expected exception},
     * this methods returns this exception. Otherwise this method returns null.</p>
     *
     * @return the exception or null
     */
    default T exception() {
        if (actual() == null) {
            return null;
        }
        return actual().behavior();
    }

    /**
     * <p>A builder for {@linkplain ResultOfExceptionalCall results of throwable calls}.</p>
     *
     * @param <T>  the type of the expected exception
     */
    interface Builder<T extends Exception> extends Result.Builder<ResultOfExceptionalCall<T>, ActualException<T>, TestOfExceptionalCall<T>, ExpectedException<T>, Builder<T>> {

        /**
         * <p>Set the {@linkplain ActualException actual exception} of the correct type and if this exception fulfils the expected throwable.</p>
         *
         * @param exception the actual exception
         * @return this bbuilder
         */
        default Builder<T> actual(T exception, boolean successful) {
            return actual(ActualException.of(exception, successful));
        }

        /**
         * <p>A factory for {@link Builder result of throwable call builders}.</p>
         *
         * @param <T>  the type of the expected exception
         */
        interface Factory<T extends Exception> extends Result.Builder.Factory<ResultOfExceptionalCall<T>, ActualException<T>, TestOfExceptionalCall<T>, ExpectedException<T>, Builder<T>> {

            @Override
            Builder<T> builder();
        }
    }
}
