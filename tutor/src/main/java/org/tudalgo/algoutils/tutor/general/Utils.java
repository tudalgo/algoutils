package org.tudalgo.algoutils.tutor.general;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import spoon.reflect.declaration.CtParameter;

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

    /**
     * Transforms the list of {@link CtParameter}s into a list of corresponding {@link TypeLink}s.
     *
     * @param list the list of {@link CtParameter}s
     * @return the list of {@link TypeLink}s
     */
    public static List<TypeLink> listOfCtParametersToTypeLinks(List<CtParameter<?>> list) {
        return list.stream().map(e -> BasicTypeLink.of(e.getType().getActualClass())).collect(toUnmodifiableList());
    }
}
