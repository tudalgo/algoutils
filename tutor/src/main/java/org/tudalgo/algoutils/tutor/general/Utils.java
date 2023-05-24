package org.tudalgo.algoutils.tutor.general;

import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

public class Utils {

    private static final Object NONE = new Object();

    public static <T> T none() {
        //noinspection unchecked
        return (T) NONE;
    }

    /**
     * Returns {@code true} if the current test is running under Jagr. Otherwise, returns {@code false}.
     *
     * @return {@code true} if the current test is running under Jagr. Otherwise, returns {@code false}.
     */
    public static boolean isJagrRun() {
        return TestCycleResolver.getTestCycle() != null;
    }
}
