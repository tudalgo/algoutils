package org.tudalgo.algoutils;

import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.util.Set;

public final class AlgoUtils {

    public static final String SUBMISSION_ID = System.getProperty("org.tudalgo.algoutils.submissionId").toLowerCase();
    public static final double SIMILARITY_THRESHOLD = Double.parseDouble(System.getProperty("org.tudalgo.algoutils.similarityThreshold", "0.8"));

    @SuppressWarnings("UnstableApiUsage")
    public static final TestCycle JAGR_TEST_CYCLE = TestCycleResolver.getTestCycle();
    public static final boolean JAGR_PRESENT = JAGR_TEST_CYCLE != null;
    public static final Set<String> JAGR_SUBMISSION_CLASSES = JAGR_PRESENT ? JAGR_TEST_CYCLE.getClassLoader().getClassNames() : Set.of();
}
