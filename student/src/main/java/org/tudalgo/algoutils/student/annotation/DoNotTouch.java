package org.tudalgo.algoutils.student.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to mark classes, functions or fields, that:
 * <ul>
 *     <li>are part of the student template</li>
 *     <li>should <b>not</b> be altered in any way by the student</li>
 * </ul>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface DoNotTouch {
    String value() default "";
}
