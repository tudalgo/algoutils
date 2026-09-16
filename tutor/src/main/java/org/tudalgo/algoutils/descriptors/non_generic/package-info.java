/**
 * This package contains Descriptors for non-generic entities.
 * Descriptors in this package are primarily used to assert that the described entity exists.
 * For that reason they only require the absolute minimum that is required to uniquely identify an entity
 * (i.e., the fully-qualified name or signature).
 * <p>
 * The Descriptors are modeled and named after Java reflection classes for non-generic types.
 * The following table gives a quick overview of the available types, their corresponding Descriptors
 * and when they are applicable:
 * <table>
 *     <tr>
 *         <th>Generic Type</th>
 *         <th>Descriptor</th>
 *         <th>Applicable For</th>
 *     </tr>
 *     <tr>
 *         <td>{@link java.lang.Package Package}</td>
 *         <td>{@link PackageDescriptor}</td>
 *         <td>Packages, e.g., {@code org.tudalgo.algoutils.descriptors.non_generic}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link java.lang.Class Class}</td>
 *         <td>{@link ClassDescriptor}</td>
 *         <td>Classes, e.g., {@code List.class} or {@code String.class}, regardless of parameterization</td>
 *     </tr>
 *     <tr>
 *         <td>{@link java.lang.reflect.Field Field}</td>
 *         <td>{@link FieldDescriptor}</td>
 *         <td>Fields / Attributes, e.g., {@code System.out} or the {@code length} attribute of array types</td>
 *     </tr>
 *     <tr>
 *         <td>{@link java.lang.reflect.Constructor Constructor}</td>
 *         <td>{@link ConstructorDescriptor}</td>
 *         <td>Constructors, e.g., {@code new ArrayList<>()} or {@code new String("hello world")}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link java.lang.reflect.Method Method}</td>
 *         <td>{@link MethodDescriptor}</td>
 *         <td>Methods, e.g., {@code List.size()} or {@code String.length()}</td>
 *     </tr>
 * </table>
 */
package org.tudalgo.algoutils.descriptors.non_generic;
