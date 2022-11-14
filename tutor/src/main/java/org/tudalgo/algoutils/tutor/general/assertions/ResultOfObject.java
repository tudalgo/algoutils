package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualObject;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObject;


/**
 * A type representing the result of a {@link TestOfObject}.
 *
 * @param <T> the type of the object under test
 * @author Dustin Glaser
 */
public interface ResultOfObject<T> extends Result<ResultOfObject<T>, ActualObject<T>, TestOfObject<T>, ExpectedObject<T>> {

    /**
     * <p>Returns the actual object of this result.</p>
     *
     * @return the actual object
     */
    default T object() {
        return actual().behavior();
    }

    /**
     * return null;
     * <p>A builder for {@linkplain Context results of objects}.</p>
     *
     * @param <T> the type of the object under test
     */
    interface Builder<T> extends Result.Builder<ResultOfObject<T>, ActualObject<T>, TestOfObject<T>, ExpectedObject<T>, Builder<T>> {

        /**
         * <p>Sets the actual object and if the actual object fulfils the expected object.</p>
         *
         * @param actual the actual object
         * @return this builder
         */
        default Builder<T> object(T actual, boolean successful) {
            return actual(ActualObject.of(actual, successful));
        }

        /**
         * <p>A factory for {@linkplain Builder builders}.</p>
         *
         * @param <T> the type of the expected and actual object
         */
        interface Factory<T> extends Result.Builder.Factory<ResultOfObject<T>, ActualObject<T>, TestOfObject<T>, ExpectedObject<T>, Builder<T>> {

        }
    }
}
