package org.tudalgo.algoutils.student.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to mark methods from the standard library,
 * that are forbidden to be used by the student in order to implement the annotated method.
 *
 * @example The following example shows how to use this annotation:
 *
 *         <pre>
 *             &#64;Forbidden("java.lang.Math.max")
 *             public static int max(int a, int b) {
 *                 if (a &gt; b) {
 *                     return a;
 *                 } else {
 *                     return b;
 *                 }
 *             }
 *         </pre>
 *         <p>
 * @example Alternatively the entire class or package can be forbidden:
 *
 *         <pre>
 *             &#64;Forbidden("java.lang.Math")
 *             public static int max(int a, int b) {
     *             if (a &gt; b) {
     *                  return a;
     *             } else {
     *                 return b;
     *             }
 *             }
 *        </pre>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface Forbidden {
    String[] value() default {};
}
