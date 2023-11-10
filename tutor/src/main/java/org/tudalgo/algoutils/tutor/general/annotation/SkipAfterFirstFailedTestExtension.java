package org.tudalgo.algoutils.tutor.general.annotation;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * This extension can be used to skip all tests after the first failed test of a cartesian product test.
 */
public class SkipAfterFirstFailedTestExtension implements ExecutionCondition, AfterTestExecutionCallback {
    /**
     * A map of test names to whether they failed before.
     */
    static Map<String, Boolean> failedTests = new HashMap<>();

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(final ExtensionContext extensionContext) {
        final var testMethod = extensionContext.getTestMethod();
        if (testMethod.isEmpty()) {
            return ConditionEvaluationResult.enabled("No test method found");
        }
        final var testMethodName = String.format(
            "%s.%s",
            testMethod.get().getDeclaringClass().getName(),
            testMethod.get().getName()
        );

        final var annotationSrc = getAnnotationSrc(extensionContext, SkipAfterFirstFailedTest.class);
        if (annotationSrc == null) {
            return ConditionEvaluationResult.enabled("No SkipAfterFirstFailedTest annotation found");
        }
        if (!annotationSrc.value()) {
            return ConditionEvaluationResult.enabled("SkipAfterFirstFailedTest annotation is disabled");
        }
        return failedTests.getOrDefault(testMethodName, false)
               ? ConditionEvaluationResult.disabled("Test skipped because of previous failure")
               : ConditionEvaluationResult.enabled("Test did not fail before");
    }

    /**
     * Returns the annotation of the given type that is present on the given extension context or one of its parents.
     *
     * @param extensionContext the extension context
     * @param annotationClass  the annotation class
     * @param <T>              the annotation type
     * @return the annotation or null if it is not present
     */
    private <T extends java.lang.annotation.Annotation> @Nullable T getAnnotationSrc(
        @Nullable ExtensionContext extensionContext,
        Class<T> annotationClass
    ) {
        if (extensionContext == null) {
            return null;
        }
        if (extensionContext.getElement().map(e -> e.isAnnotationPresent(annotationClass)).orElse(false)) {
            return extensionContext.getElement().get().getAnnotation(annotationClass);
        }
        return getAnnotationSrc(extensionContext.getParent().orElse(null), annotationClass);
    }

    @Override
    public void afterTestExecution(final ExtensionContext extensionContext) {
        final var testMethod = extensionContext.getTestMethod();
        if (testMethod.isEmpty()) {
            return;
        }
        final var testMethodName = String.format(
            "%s.%s",
            testMethod.get().getDeclaringClass().getName(),
            testMethod.get().getName()
        );
        if (extensionContext.getExecutionException().isPresent()) {
            failedTests.put(testMethodName, true);
        }
    }
}
