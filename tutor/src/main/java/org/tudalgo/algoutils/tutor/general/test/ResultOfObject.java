package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.test.actual.ActualObject;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedObject;

/**
 * A type representing the result of a {@link TestOfObject}.
 *
 * @param <T> the type of the object under test
 * @author Dustin Glaser
 */
public interface ResultOfObject<T, RT extends ResultOfObject<T, RT, TT>, TT extends TestOfObject<T, TT, RT>> extends Result<RT, ActualObject<T>, TT, ExpectedObject<T>> {

    ActualObject<T> actual();

    default T object() {
        return actual().object();
    }

    interface Builder<T, RT extends ResultOfObject<T, RT, TT>, TT extends TestOfObject<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> extends Result.Builder<RT, ActualObject<T>, TT, ExpectedObject<T>, BT> {

        BT actual(ActualObject<T> actual);

        default BT actual(T actual, boolean successful) {
            return actual(ActualObject.of(actual, successful));
        }

        interface Factory<T, RT extends ResultOfObject<T, RT, TT>, TT extends TestOfObject<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> {

            Builder<T, RT, TT, BT> builder();
        }
    }
}
