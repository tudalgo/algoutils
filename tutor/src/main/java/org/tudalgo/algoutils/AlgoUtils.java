package org.tudalgo.algoutils;

import com.google.common.reflect.ClassPath;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public final class AlgoUtils {

    /**
     * Name of the submission (required, e.g. "h01").
     * This is equal to the name of the base package where all submission classes reside.
     * <p>
     * Property name: {@code org.tudalgo.algoutils.submissionId}
     */
    public static final String SUBMISSION_ID;
    /**
     * Minimum value at which two strings are still considered equal when similarity matching (default: 0.90).
     * <p>
     * Certain methods in this library can work with classes and class members that have the wrong name or
     * are declared in the wrong package.
     * The similarity of two strings (expected and actual value) is determined via the Levenshtein distance.
     * This yields a number between 0 and 1, with 1 meaning an exact match.
     * The value of this field determines the minimum similarity for those two strings to be considered equal.
     * <p>
     * Property name: {@code org.tudalgo.algoutils.similarityThreshold}
     */
    public static final double SIMILARITY_THRESHOLD;

    /**
     * The Jagr test cycle, if running in Jagr's environment.
     */
    public static final TestCycle JAGR_TEST_CYCLE;
    /**
     * {@code true} if running in Jagr's environment, {@code false} otherwise.
     */
    public static final boolean JAGR_PRESENT;

    /**
     * The set of names for all classes in the submission.
     */
    public static final Set<String> SUBMISSION_CLASSES;

    static {
        SUBMISSION_ID = getProperty("submissionId");
        SIMILARITY_THRESHOLD = Double.parseDouble(getProperty("similarityThreshold", "0.9"));

        JAGR_TEST_CYCLE = TestCycleResolver.getTestCycle();
        JAGR_PRESENT = JAGR_TEST_CYCLE != null;

        if (JAGR_PRESENT) {
            SUBMISSION_CLASSES = Collections.unmodifiableSet(JAGR_TEST_CYCLE.getClassLoader().getClassNames());
        } else {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                ClassPath classPath = ClassPath.from(classLoader);
                SUBMISSION_CLASSES = classPath.getTopLevelClassesRecursive(SUBMISSION_ID)
                    .stream()
                    .map(ClassPath.ClassInfo::getName)
                    .collect(Collectors.toUnmodifiableSet());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private static String getProperty(String property) {
        String propertyValue = System.getProperty("org.tudalgo.algoutils." + property);
        if (propertyValue != null) {
            return propertyValue;
        } else {
            throw new IllegalStateException("Property org.tulalgo.algoutils.%s is required but undefined".formatted(property));
        }
    }

    private static String getProperty(String property, String defaultValue) {
        return System.getProperty("org.tudalgo.algoutils." + property, defaultValue);
    }
}
