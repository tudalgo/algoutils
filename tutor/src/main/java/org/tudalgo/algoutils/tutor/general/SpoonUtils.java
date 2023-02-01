package org.tudalgo.algoutils.tutor.general;

import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.Stringifiable;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A collection of utilities for working with spoon.
 */
public class SpoonUtils {

    private static final Pattern SPOON_NAME_PATTERN = Pattern.compile("((?!Ct|Impl)[A-Z][a-z]*)");
    private static CtModel model = null;

    private SpoonUtils() {
    }

    /**
     * @deprecated use {@link #getCtModel()} instead
     */
    public static <T, U extends CtType<?>> T getCtElementForSourceCode(
        String ignoredSourceCode,
        Class<U> ignoredKind,
        Matcher<Stringifiable> ignoredNameMatcher
    ) {
        throw new UnsupportedOperationException("use getCtModel instead");
    }

    /**
     * <p>Returns a human-readable name of the given element.</p>
     *
     * @param element the element
     * @return the human-readable name
     */
    public static String getNameOfCtElement(CtElement element) {
        return getNameOfCtElement(element.getClass());
    }

    /**
     * <p>Returns a human-readable name of the given element type.</p>
     *
     * @param type the element type
     * @return the human-readable name
     */
    public static String getNameOfCtElement(Class<?> type) {
        var name = type.getSimpleName();
        var match = SPOON_NAME_PATTERN.matcher(name);
        name = match.results().map(m -> m.group(1).toLowerCase()).collect(Collectors.joining(" "));
        return name;
    }

    /**
     * <p>Returns a <code>CtModel</code> for submission.</p>
     *
     * <p>If the test run is a test cycle run,
     * the class loader of the test cycle is used to load the submission.</p>
     *
     * @return the <code>CtModel</code>
     * @see TestCycleResolver
     */
    public static CtModel getCtModel() {
        if (model != null) {
            return model;
        }
        var launcher = new Launcher();
        //noinspection UnstableApiUsage
        var cycle = TestCycleResolver.getTestCycle();
        if (cycle != null) {
            launcher.getEnvironment().setInputClassLoader((ClassLoader) cycle.getClassLoader());
        }
        return model = launcher.buildModel();
    }
}
