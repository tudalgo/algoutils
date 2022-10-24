package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing;
import org.tudalgo.algoutils.tutor.general.callable.Callable;

public interface TestOfCall extends Test<TestOfCall, Nothing, ResultOfCall, Nothing> {

    ResultOfCall run(Callable callable);

    interface Builder extends Test.Builder<TestOfCall, Nothing, ResultOfCall, Nothing, Builder> {

        interface Factory extends Test.Builder.Factory<TestOfCall, Nothing, ResultOfCall, Nothing, Builder> {

        }
    }
}
