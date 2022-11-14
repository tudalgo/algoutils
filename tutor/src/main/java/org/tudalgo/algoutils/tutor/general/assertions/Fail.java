package org.tudalgo.algoutils.tutor.general.assertions;


import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing;

/**
 * <p>A always-failing test.</p>>
 */
public interface Fail extends Test<Fail, Expected, ResultOfFail, Actual> {

    /**
     * <p>Fails this test (there is no other option) with the given cause.</p>
     *
     * @param cause the cause of the failure
     * @return the result of this fail
     */
    ResultOfFail run(Actual actual, Exception cause);

    /**
     * <p>Fails this test (there is no other option).</p>
     *
     * @return the result of this fail
     */
    default ResultOfFail run() {
        return run(null, null);
    }

    /**
     * <p>A builder for {@linkplain Fail fails}.</p>
     *
     * @author Dustin Glaser
     */
    interface Builder extends Test.Builder<Fail, Expected, ResultOfFail, Actual, Builder> {



        /**
         * <p>A factory for {@link Builder fail builders}.</p>
         *
         * @author Dustin Glaser
         */
        interface Factory extends Test.Builder.Factory<Fail, Expected, ResultOfFail, Actual, Builder> {

        }
    }
}
