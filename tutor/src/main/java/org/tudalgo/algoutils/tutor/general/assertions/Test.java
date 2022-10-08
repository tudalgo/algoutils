package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;

/**
 * <p>A type of a test.</p>
 *
 * @param <TT> the type of the test
 * @param <ET> the type of the expected behavior
 * @param <RT> the type of the result
 * @param <AT> the type of the actual behavior
 */
public interface Test<TT extends Test<TT, ET, RT, AT>, ET extends Expected, RT extends Result<RT, AT, TT, ET>, AT extends Actual> {

    /**
     * <p>Returns the expected behavior of this test.</p>
     *
     * @return the expected behavior
     */
    ET expected();

    /**
     * <p>A builder for {@linkplain Test tests}.</p>
     *
     * @param <TT> the type of the test
     * @param <ET> the type of the expected behavior
     * @param <RT> the type of the result
     * @param <AT> the type of the actual behavior
     * @param <BT> the type of the builder
     */
    interface Builder<TT extends Test<TT, ET, RT, AT>, ET extends Expected, RT extends Result<RT, AT, TT, ET>, AT extends Actual, BT extends Test.Builder<TT, ET, RT, AT, BT>> {

        /**
         * Builds a test with the configured properties and return this.
         *
         * @return the test
         */
        TT build();

        /**
         * <p>Sets the expected behavior.</p>
         *
         * @param expected the expected behavior
         * @return this builder
         */
        BT expected(ET expected);

        /**
         * <p>A factory for {@link Builder test builders}.</p>
         */
        interface Factory<TT extends Test<TT, ET, RT, AT>, ET extends Expected, RT extends Result<RT, AT, TT, ET>, AT extends Actual, BT extends Builder<TT, ET, RT, AT, BT>> {

            /**
             * <p>Creates a new builder for a test.</p>
             *
             * @return the builder
             */
            BT builder();
        }
    }
}
