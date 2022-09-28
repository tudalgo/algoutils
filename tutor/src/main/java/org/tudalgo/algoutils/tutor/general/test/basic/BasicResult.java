package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Context;
import org.tudalgo.algoutils.tutor.general.test.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.test.Result;
import org.tudalgo.algoutils.tutor.general.test.Test;

public abstract class BasicResult<TT extends Test> implements Result<TT> {

    protected final Environment environment;
    protected final boolean successful;
    protected final TT test;

    public BasicResult(Environment environment, TT test, boolean successful) {
        this.environment = environment;
        this.successful = successful;
        this.test = test;
    }

    protected <T extends Result<?>> void check(T result, Context context, PreCommentSupplier<? super T> preCommentSupplier) {
        if (!successful())
            throw new AssertionError(environment.getCommentFactory().comment(result, context, preCommentSupplier));
    }

    @Override
    public boolean successful() {
        return successful;
    }

    @Override
    public TT test() {
        return test;
    }
}
