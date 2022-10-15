package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing;

/**
 * <p>A result of a {@linkplain Fail fail}.</p>
 */
public interface ResultOfFail extends Result<ResultOfFail, Nothing, Fail, Nothing> {

    /**
     * <p>A builder for {@linkplain Context results of fails}.</p>
     *
     */
    interface Builder extends Result.Builder<ResultOfFail, Nothing, Fail, Nothing, Builder> {

        /**
         * <p>A factory for {@link Builder result of fail builders}.</p>
         *
         */
        interface Factory extends Result.Builder.Factory<ResultOfFail, Nothing, Fail, Nothing, Builder> {

        }
    }
}
