package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.test.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedException;

import static org.tudalgo.algoutils.tutor.general.test.actual.ActualException.of;

/**
 * <p>A type representing the result of a {@linkplain TestOfThrowableCall throwable call}.</p>
 *
 * @param <T> the type of the expected exception
 * @author Dustin Glaser
 */
public interface ResultOfThrowableCall<T extends Exception, RT extends ResultOfThrowableCall<T, RT, TT>, TT extends TestOfThrowableCall<T, TT, RT>> extends Result<RT, ActualException<T>, TT, ExpectedException<T>> {

    /**
     * <p>If the test resulted in the {@linkplain TestOfThrowableCall#expected()} expected exception},
     * this methods returns this exception. Otherwise this method returns null.</p>
     *
     * @return the exception or null
     */
    default T exception() {
        if (actual() == null) {
            return null;
        }
        return actual().object();
    }

    /**
     * <p>A builder for {@linkplain ResultOfThrowableCall results of throwable calls}.</p>
     *
     * @param <T>  the type of the expected exception
     * @param <RT> the type of the result of throwable call
     * @param <TT> the type of throwable call
     * @param <BT> the type of the builder
     */
    interface Builder<T extends Exception, RT extends ResultOfThrowableCall<T, RT, TT>, TT extends TestOfThrowableCall<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> extends Result.Builder<RT, ActualException<T>, TT, ExpectedException<T>, BT> {

        /**
         * <p>Set the {@linkplain ActualException actual exception} of the correct type and if this exception fulfils the expected throwable.</p>
         *
         * @param exception the actual exception
         * @return this builder
         */
        default BT actual(T exception, boolean successful) {
            return actual(of(exception, successful));
        }

        /**
         * <p>A factory for {@link Builder result of throwable call builders}.</p>
         *
         * @param <T>  the type of the expected exception
         * @param <RT> the type of the result of throwable call
         * @param <TT> the type of throwable call
         * @param <BT> the type of the builder
         */
        interface Factory<T extends Exception, RT extends ResultOfThrowableCall<T, RT, TT>, TT extends TestOfThrowableCall<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> extends Result.Builder.Factory<RT, ActualException<T>, TT, ExpectedException<T>, BT> {

        }
    }
}
