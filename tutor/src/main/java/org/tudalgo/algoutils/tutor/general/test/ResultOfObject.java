package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.test.actual.ActualObject;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedObject;

import static org.tudalgo.algoutils.tutor.general.test.actual.ActualObject.of;

/**
 * A type representing the result of a {@link TestOfObject}.
 *
 * @param <T> the type of the object under test
 * @author Dustin Glaser
 */
public interface ResultOfObject<T, RT extends ResultOfObject<T, RT, TT>, TT extends TestOfObject<T, TT, RT>> extends Result<RT, ActualObject<T>, TT, ExpectedObject<T>> {

    /**
     * <p>Returns the actual object of this result.</p>
     *
     * @return the actual object
     */
    default T object() {
        return actual().behavior();
    }

    /**
     * <p>A builder for {@linkplain Context results of objects}.</p>
     *
     * @param <T>  the type of the object under test
     * @param <RT> the type of the result of object
     * @param <TT> the type of object
     * @param <BT> the type of the builder
     */
    interface Builder<T, RT extends ResultOfObject<T, RT, TT>, TT extends TestOfObject<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> extends Result.Builder<RT, ActualObject<T>, TT, ExpectedObject<T>, BT> {

        /**
         * <p>Sets the actual object and if the actual object fulfils the expected object.</p>
         *
         * @param actual the actual object
         * @return this builder
         */
        default BT object(T actual) {
            return actual(of(actual));
        }

        /**
         * <p>A factory for {@linkplain Builder builders}.</p>
         *
         * @param <T>  the type of the expected and actual object
         * @param <RT> the type of the result
         * @param <TT> the type of the test
         * @param <BT> the type of the builder
         */
        interface Factory<T, RT extends ResultOfObject<T, RT, TT>, TT extends TestOfObject<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> {

            /**
             * <p>Returns a new builder.</p>
             *
             * @return a new builder
             */
            Builder<T, RT, TT, BT> builder();
        }
    }
}
