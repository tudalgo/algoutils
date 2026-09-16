package org.tudalgo.algoutils.descriptors;

/**
 * Marker interface to indicate that a non-generic descriptor doesn't have a Java reflection counterpart.
 * This can happen if one of the <b>find*</b> methods in {@link Descriptors} could not find the requested entity.
 */
public interface NotFound extends Descriptor {}
