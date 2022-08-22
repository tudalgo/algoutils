package org.tudalgo.algoutils.tutor.general.test;

public interface CommentBuilder<T extends Result> {

    <TS extends T> String build(TS result, Context context, PreCommentSupplier<TS> preCommentSupplier);
}
