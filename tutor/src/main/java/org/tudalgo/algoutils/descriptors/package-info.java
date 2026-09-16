/**
 * This package contains interfaces that function as abstractions of generic and non-generic types, packages and class members.
 * They are modeled and named after the Java reflection classes.
 * The Descriptors are organized into two packages {@link org.tudalgo.algoutils.descriptors.generic generic} and
 * {@link org.tudalgo.algoutils.descriptors.non_generic non_generic}, for generic and non-generic entities respectively.
 * Descriptors in the {@code generic} package are used to assert properties for generic entities, e.g. type parameters,
 * generic superclasses, interfaces, exceptions, return types, etc.
 * Depending on the generic type, this can include variable names, bounds, etc.
 * Descriptors in the {@code non_generic} package are primarily used to assert that the described entity exists.
 * For that reason they only require the absolute minimum that is required to uniquely identify an entity
 * (i.e., the fully-qualified name or signature).
 * <p>
 * Descriptors with a basic implementation have an <b>of</b> method that creates an instance of the Descriptor.
 * Alternatively, instances can be obtained via the methods in {@link Descriptors}.
 * That class also provides a way to create Descriptors from their Java reflection counterparts.
 * <p>
 * Earlier versions of this library used a similar concept to Descriptors called Links,
 * which were also based on reflection objects and indirect access.
 * However, unlike Links, Descriptors cannot be resolved to their reflection counterpart directly.
 * This is intentional to make the user verify that the described entity actually exists instead of assuming it does.
 * To get the reflection objects, use the verifications in the
 * {@link org.tudalgo.algoutils.assertions.verification.ReflectionVerifications ReflectionVerifications} class.
 */
package org.tudalgo.algoutils.descriptors;
