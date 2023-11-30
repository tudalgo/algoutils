package org.tudalgo.algoutils.tutor.general;

import java.util.List;

import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
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
     * Transforms the list of {@link CtParameter}s into a list of names of the corresponding types.
     *
     * @param list the list of {@link CtParameter}s
     * @return the list of names
     */
    public static List<String> listOfCtParametersToTypeNames(List<? extends CtParameter<?>> list) {
        return list.stream().map(e -> e.getType().getSimpleName()).toList();
    }

    /**
     * Transforms the list of {@link TypeLink}s into a list of names of the corresponding types.
     *
     * @param list the list of {@link TypeLink}s
     * @return the list of names
     */
    public static List<String> listOfTypeLinksToTypeNames(List<? extends TypeLink> list) {
        return list.stream().map(TypeLink::name).toList();
    }
}
