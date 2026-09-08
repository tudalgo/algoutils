/**
 * This package contains interfaces that function as abstractions of generic and non-generic types as well as class members.
 * In other words, they "describe" the underlying entity without it needing to exist.
 * The general use case for these "descriptors" is to check if an entity exists and conforms to the expected constraints
 * using the verifications in the {@link org.tudalgo.algoutils.assertions.verification.ReflectionVerifications ReflectionVerifications} class.
 * <p>
 * Most descriptor interfaces have an <b>of</b> method that creates an instance of the descriptor.
 * Alternatively, instances can be obtained via the methods in {@link Descriptors}.
 * That class also provides a way to create descriptors from their Java reflection counterparts.
 */
package org.tudalgo.algoutils.descriptors;
