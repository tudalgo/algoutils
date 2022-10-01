package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.test.actual.ActualException;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedException;

/**
 * A type representing the result of a {@link TestOfThrowableCall}.
 *
 * @param <T> the exception type of the callable under test
 * @author Dustin Glaser
 */
public interface ResultOfThrowableCall<T extends Exception, RT extends ResultOfThrowableCall<T, RT, TT>, TT extends TestOfThrowableCall<T, TT, RT>> extends Result<RT, ActualException<T>, TT, ExpectedException<T>> {

    interface Builder<T extends Exception, RT extends ResultOfThrowableCall<T, RT, TT>, TT extends TestOfThrowableCall<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> extends Result.Builder<RT, ActualException<T>, TT, ExpectedException<T>, BT> {

        default BT actual(T exception, boolean successful) {
            actual(ActualException.of(exception, successful));
            //noinspection unchecked
            return (BT) this;
        }

        interface Factory<T extends Exception, RT extends ResultOfThrowableCall<T, RT, TT>, TT extends TestOfThrowableCall<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> extends Result.Builder.Factory<RT, ActualException<T>, TT, ExpectedException<T>, BT> {

            Builder<T, RT, TT, BT> builder();
        }
    }
}
