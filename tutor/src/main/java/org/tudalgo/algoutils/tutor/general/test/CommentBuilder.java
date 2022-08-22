package org.tudalgo.algoutils.tutor.general.test;

public interface CommentBuilder<TT extends Test, RT extends Result<? extends TT>> {

    String build(RT result, Context context, PreCommentSupplier<TT, RT> preCommentSupplier);
}
