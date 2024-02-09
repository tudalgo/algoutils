package org.tudalgo.algoutils.tutor.general;

import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.Stringifiable;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;
import spoon.support.compiler.VirtualFile;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.sourcegrade.jagr.api.testing.extension.TestCycleResolver.getTestCycle;

/**
 * A collection of utilities for working with spoon.
 */
public class SpoonUtils {

    private static final Pattern SPOON_NAME_PATTERN = Pattern.compile("((?!Ct|Impl)[A-Z][a-z]*)");
    private static CtModel model = null;

    private SpoonUtils() {
    }

    /**
     * <p>Returns a <code>CtElement</code> for the given source code.</p>
     *
     * @param ignoredSourceCode  the source code
     * @param ignoredKind        the kind of the element
     * @param ignoredNameMatcher the name matcher
     * @deprecated use {@link #getType(String, Class)} instead
     */
    public static <T, U extends CtType<?>> T getCtElementForSourceCode(
        final String ignoredSourceCode,
        final Class<U> ignoredKind,
        final Matcher<Stringifiable> ignoredNameMatcher
    ) {
        throw new UnsupportedOperationException("use getCtModel instead");
    }

    /**
     * <p>Returns a human-readable name of the given element.</p>
     *
     * @param element the element
     * @return the human-readable name
     */
    public static String getNameOfCtElement(final CtElement element) {
        return getNameOfCtElement(element.getClass());
    }

    /**
     * <p>Returns a human-readable name of the given element type.</p>
     *
     * @param type the element type
     * @return the human-readable name
     */
    public static String getNameOfCtElement(final Class<?> type) {
        var name = type.getSimpleName();
        final var match = SPOON_NAME_PATTERN.matcher(name);
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
     * @deprecated use {@link #getType(String, Class)} instead
     */
    public static CtModel getCtModel() {
        if (model != null) {
            return model;
        }
        final var launcher = new Launcher();
        //noinspection UnstableApiUsage
        final var cycle = getTestCycle();
        if (cycle != null) {
            launcher.getEnvironment().setInputClassLoader((ClassLoader) cycle.getClassLoader());
        }
        return model = launcher.buildModel();
    }

    /**
     * Returns the corresponding {@link CtType} in the spoon world for the given class.
     *
     * @param className the class to get the spoon type for
     * @return the corresponding {@link CtType} in the spoon world for the given class
     */
    public static CtType<?> getType(final String className) {
        return getType(className, CtType.class);
    }

    /**
     * Returns the corresponding {@link CtType} in the spoon world for the given class.
     *
     * @param className the class to get the spoon type for
     * @param type      the type of the spoon type to return
     * @param <T>       the type of the spoon type to return
     * @return the corresponding {@link CtType} in the spoon world for the given class
     */
    public static <T extends CtType<?>> T getType(final String className, final Class<T> type) {
        final Launcher launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(17);
        launcher.getEnvironment().setIgnoreSyntaxErrors(true);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.getEnvironment().setPreserveLineNumbers(true);
        launcher.getEnvironment().setAutoImports(true);

        final String sourceCode = ResourceUtils.getTypeContent(className);
        final VirtualFile file = new VirtualFile(sourceCode, className);

        @SuppressWarnings("UnstableApiUsage") final TestCycle cycle = getTestCycle();
        if (cycle != null) {
            launcher.getEnvironment().setInputClassLoader((ClassLoader) cycle.getClassLoader());
        }

        launcher.addInputResource(file);
        final CtModel model = launcher.buildModel();
        return model.getAllTypes().stream()
            .filter(type::isInstance)
            .filter(it -> it.getQualifiedName().equals(className))
            .map(type::cast)
            .findFirst()
            .orElseThrow();
    }
}
