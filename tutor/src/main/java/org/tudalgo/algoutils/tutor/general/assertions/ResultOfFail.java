package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;

/**
 * <p>A result of a {@linkplain Fail fail}.</p>
 */
public interface ResultOfFail extends Result<ResultOfFail, Actual, Fail, Expected> {

    /**
     * <p>A builder for {@linkplain Context results of fails}.</p>
     */
    interface Builder extends Result.Builder<ResultOfFail, Actual, Fail, Expected, Builder> {

        /**
         * <p>A factory for {@link Builder result of fail builders}.</p>
         */
        interface Factory extends Result.Builder.Factory<ResultOfFail, Actual, Fail, Expected, Builder> {

        }
    }
}
