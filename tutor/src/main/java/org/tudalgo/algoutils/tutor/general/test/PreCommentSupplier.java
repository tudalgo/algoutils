package org.tudalgo.algoutils.tutor.general.test;

@FunctionalInterface
public interface PreCommentSupplier<TR> {

    String getPreComment(TR result);
}
