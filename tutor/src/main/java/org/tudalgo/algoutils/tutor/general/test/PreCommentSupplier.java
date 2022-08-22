package org.tudalgo.algoutils.tutor.general.test;

@FunctionalInterface
public interface PreCommentSupplier<T extends Test> {

    String getPreComment(T test);
}
