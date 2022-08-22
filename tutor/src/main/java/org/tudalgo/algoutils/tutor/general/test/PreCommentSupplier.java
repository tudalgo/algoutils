package org.tudalgo.algoutils.tutor.general.test;

@FunctionalInterface
public interface PreCommentSupplier<T extends Result<T>> {

    String getPreComment(T test);
}
