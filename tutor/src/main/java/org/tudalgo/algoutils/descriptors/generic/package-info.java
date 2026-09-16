/**
 * This package contains Descriptors for generic entities.
 * These Descriptors can be used to assert properties for generic entities, e.g. type parameters,
 * generic superclasses, interfaces, exceptions, return types, etc.
 * Depending on the generic type, this can include variable names, bounds, etc.
 * <p>
 * The Descriptors are modeled and named after Java reflection classes for generic types.
 * The following table gives a quick overview of the available types, their corresponding Descriptors
 * and when they are applicable:
 * <table>
 *     <tr>
 *         <th>Generic Type</th>
 *         <th>Descriptor</th>
 *         <th>Applicable For</th>
 *     </tr>
 *     <tr>
 *         <td>{@link java.lang.reflect.GenericArrayType GenericArrayType}</td>
 *         <td>{@link GenericArrayTypeDescriptor}</td>
 *         <td>Arrays with a generic component type, e.g., {@code List<T>[]} or {@code T[][]}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link java.lang.reflect.ParameterizedType ParameterizedType}</td>
 *         <td>{@link ParameterizedTypeDescriptor}</td>
 *         <td>Types with type parameters, e.g., {@code List<T>} or {@code Supplier<T>}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link java.lang.reflect.TypeVariable TypeVariable}</td>
 *         <td>{@link TypeVariableDescriptor}</td>
 *         <td>Type variables also known as the placeholder in type parameters, e.g., {@code T} in {@code List<T>}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link java.lang.reflect.WildcardType WildcardType}</td>
 *         <td>{@link WildcardTypeDescriptor}</td>
 *         <td>Bounded and unbounded wildcard types, e.g., {@code ?} in {@code List<?>} or {@code ? extends Number} in {@code List<? extends Number>}</td>
 *     </tr>
 * </table>
 */
package org.tudalgo.algoutils.descriptors.generic;
