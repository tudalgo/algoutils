package org.tudalgo.algoutils.tutor.general.annotation;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to skip all tests after the first failed test of a parameterized test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@ExtendWith(SkipAfterFirstFailedTestExtension.class)
public @interface SkipAfterFirstFailedTest {
    /**
     * Whether to skip or not. Use this on methods to override the class level annotation.
     *
     * @return whether to skip or not
     */
    boolean value() default true;
}
