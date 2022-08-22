package org.tudalgo.algoutils.tutor.general.test;

@FunctionalInterface
public interface PreCommentSupplier<TR extends Result<?>> {

    String getPreComment(TR result);
}
