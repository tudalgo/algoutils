package org.tudalgo.algoutils.tutor.general.test;

@FunctionalInterface
public interface PreCommentSupplier<TT extends Test, TR extends Result<? extends TT>> {

    String getPreComment(TR result);
}
