package org.tudalgo.algoutils.tutor.general.assertions;

import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;

/**
 * <p>A type representing the result of a {@link Test}.</p>
 *
 * @author Dustin Glaser
 */
public interface Result<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected> {

    /**
     * <p>Returns the {@linkplain Actual actual} of this result.</p>
     * <p>If the test has failed due to an exception, the actual might be {@code null} and the exception trace can be retrieved using {@link #cause()}.</p>
     *
     * @return the actual result of the test or {@code null}
     */
    AT actual();

    /**
     * <p>If this result is not {@linkplain #successful()} due to an exception,
     * this method returns the causing exception. Otherwise null is returned.</p>
     *
     * @return the exception or null
     */
    Throwable cause();

    /**
     * <p>If this result is not {@linkplain #successful() successful},
     * this method throws an {@link AssertionFailedError} informing about the failure.</p>
     *
     * @param context            the context of the test
     * @param preCommentSupplier the function for the pre-comment
     * @return this result
     * @throws AssertionFailedError if not successful
     */

    RT check(Context context, PreCommentSupplier<? super RT> preCommentSupplier) throws AssertionFailedError;

    /**
     * <p>Returns the {@linkplain Expected expected behavior} of this result.</p>
     *
     * @return the expected behavior of this result
     */
    default ET expected() {
        return test().expected();
    }

    /**
     * <p>Returns if the test was successful.</p>
     *
     * @return if the test was successful
     */
    default boolean successful() {
        return actual() != null && actual().successful();
    }

    /**
     * <p>Returns the test this result belongs to.</p>
     *
     * @return the test
     */
    TT test();

    /**
     * <p>A type representing a builder for {@linkplain Result results}.</p>
     *
     * @param <RT> the type of the result
     * @param <AT> the type of the actual behavior
     * @param <TT> the type of the test
     * @param <ET> the type of the expected behavior
     * @param <BT> the type of the builder
     * @author Dustin Glaser
     */
    interface Builder<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected, BT extends Builder<RT, AT, TT, ET, BT>> {

        /**
         * <p>Sets the {@linkplain Actual actual behavior} of the result.</p>
         *
         * @param actual the actual behavior
         * @return this builder
         */
        BT actual(AT actual);

        /**
         * <p>Builds a result for the given properties and returns it.</p>
         *
         * @return the result
         */
        RT build();

        /**
         * <p>Sets the {@linkplain Throwable exception} of the result.</p>
         *
         * @param exception the exception
         * @return this builder
         */
        BT exception(Throwable exception);

        /**
         * <p>Sets the {@linkplain Test test} of the result.</p>
         *
         * @param test the test
         * @return this builder
         */
        BT test(TT test);

        /**
         * <p>A type representing a factory for {@linkplain Builder builders}.</p>
         *
         * @param <RT> the type of the result
         * @param <AT> the type of the actual behavior
         * @param <TT> the type of the test
         * @param <ET> the type of the expected behavior
         * @author Dustin Glaser
         */
        interface Factory<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected, BT extends Builder<RT, AT, TT, ET, BT>> {

            /**
             * <p>Creates a new {@linkplain Builder result builder}.</p>
             *
             * @return the result builder
             */
            BT builder();
        }
    }
}
