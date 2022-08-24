package org.tudalgo.algoutils.tutor.general.test;

public interface CommentFactory<T extends Result<?>> {

    <TS extends T> String comment(TS result, Context context, PreCommentSupplier<? super TS> preCommentSupplier);
}
