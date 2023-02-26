package org.tudalgo.algoutils.tutor.general;

import org.sourcegrade.jagr.api.testing.TestCycle;
import org.tudalgo.algoutils.tutor.general.io.JavaResource;
import org.tudalgo.algoutils.tutor.general.io.JavaStdlibResource;
import org.tudalgo.algoutils.tutor.general.io.JavaSubmissionResource;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.Stringifiable;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;
import spoon.support.compiler.VirtualFile;
import spoon.support.compiler.VirtualFolder;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.sourcegrade.jagr.api.testing.extension.TestCycleResolver.getTestCycle;

/**
 * A collection of utilities for working with spoon.
 */
public class SpoonUtils {

    private static final Pattern SPOON_NAME_PATTERN = Pattern.compile("((?!Ct|Impl)[A-Z][a-z]*)");
    private static final JavaResource STDLIB = new JavaStdlibResource();
    private static Launcher launcher = null;
    private static CtModel model = null;

    private SpoonUtils() {
    }

    /**
     * @deprecated use {@link #getModel()} instead
     */
    @Deprecated
    public static <T, U extends CtType<?>> T getCtElementForSourceCode(String ignoredSourceCode, Class<U> ignoredKind, Matcher<Stringifiable> ignoredNameMatcher) {
        throw new UnsupportedOperationException("use getCtModel instead");
    }

    /**
     * <p>Returns a human-readable name of the given element.</p>
     *
     * @param element the element
     *
     * @return the human-readable name
     */
    public static String getNameOfCtElement(CtElement element) {
        return getNameOfCtElement(element.getClass());
    }

    /**
     * <p>Returns a human-readable name of the given element type.</p>
     *
     * @param type the element type
     *
     * @return the human-readable name
     */
    public static String getNameOfCtElement(Class<?> type) {
        var name = type.getSimpleName();
        var match = SPOON_NAME_PATTERN.matcher(name);
        name = match.results().map(m -> m.group(1).toLowerCase()).collect(Collectors.joining(" "));
        return name;
    }

    /**
     * Initializes the spoon launcher which is used to build the spoon model.
     * <p>The launcher with the submission files which are retrieved from the test cycle if is present or else
     * locally from the file system.
     */
    private static void initLauncher() {
        launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(17);
        launcher.getEnvironment().setIgnoreSyntaxErrors(true);

        @SuppressWarnings("UnstableApiUsage") TestCycle cycle = getTestCycle();
        if (cycle != null) {
            launcher.getEnvironment().setInputClassLoader((ClassLoader) cycle.getClassLoader());
        }

        VirtualFolder folder = new VirtualFolder();
        JavaSubmissionResource resource = new JavaSubmissionResource();
        // CLass name <-> Sourcecode
        resource.stream().map((entry -> new VirtualFile(entry.getValue(), entry.getKey()))).forEach(folder::addFile);
        launcher.addInputResource(folder);
    }

    /**
     * Returns the spoon model.
     *
     * @return the spoon model
     */
    public static CtModel getModel() {
        if (launcher == null) {
            initLauncher();
        }
        return model;
    }

    /**
     * Adds the given class to the spoon model.
     *
     * @param className the name of the class to add
     */
    public static void addInputResource(String className) {
        if (launcher == null) {
            initLauncher();
        }
        launcher.addInputResource(STDLIB.get(className));
        model = launcher.buildModel();
    }

    /**
     * Returns the ct type from the spoon model which matches the given predicate.
     * <p>
     * If no type matches the predicate, the given class is added to the model and the method is called again.
     *
     * @param predicate the predicate to match
     * @param className the name of the class to add to the model if the predicate does not match
     * @param <T>       the type of the ct type
     *
     * @return the ct type from the spoon model which matches the given predicate
     */
    public static <T extends CtType<?>> T getType(Predicate<? super CtType<?>> predicate, String className) {
        Optional<CtType<?>> type = getModel().getAllTypes().stream().filter(predicate).findFirst();
        T element;
        if (type.isEmpty()) {
            // In case the type is not in the model yet, add it and try again
            addInputResource(className);
            element = getType(predicate, className);
        } else {
            //noinspection unchecked
            element = (T) type.get();
        }
        return element;
    }

}
