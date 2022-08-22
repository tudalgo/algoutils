package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfCall extends ResultWithThrowable<ResultOfCall, Throwable> {

    @Override
    TestOfCall test();
}
