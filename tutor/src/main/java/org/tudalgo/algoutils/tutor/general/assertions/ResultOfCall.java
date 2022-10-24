package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing;

public interface ResultOfCall extends Result<ResultOfCall, Nothing, TestOfCall, Nothing> {

    interface Builder extends Result.Builder<ResultOfCall, Nothing, TestOfCall, Nothing, Builder> {

        interface Factory extends Result.Builder.Factory<ResultOfCall, Nothing, TestOfCall, Nothing, Builder> {

        }
    }
}
