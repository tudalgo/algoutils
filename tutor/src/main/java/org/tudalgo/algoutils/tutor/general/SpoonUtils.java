package org.tudalgo.algoutils.tutor.general;

import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.Stringifiable;
import spoon.Launcher;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;
import spoon.support.compiler.VirtualFile;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A collection of utilities for working with spoon.
 */
public class SpoonUtils {

    private static final Pattern SPOON_NAME_PATTERN = Pattern.compile("((?!Ct|Impl)[A-Z][a-z]*)");

    private SpoonUtils() {
    }

    /**
     * <p>Returns a {@linkplain CtElement} of the specified type for the specified source code.</p>
     *
     * @param sourceCode  the source code
     * @param kind        the kind of the element to find
     * @param nameMatcher a matcher for matching the name of the element
     * @param <T>         the type of the element to return (subtype of {@code U})
     * @param <U>         the type of the element to search for
     * @return the element
     */
    public static <T, U extends CtType<?>> T getCtElementForSourceCode(String sourceCode, Class<U> kind, Matcher<Stringifiable> nameMatcher) {
        var launcher = new Launcher();
        launcher.addInputResource(new VirtualFile(sourceCode));
        var matches = launcher.buildModel().getElements(e -> {
            if (!kind.isAssignableFrom(e.getClass())) {
                return false;
            }
            return nameMatcher.match(() -> (((CtType<?>) e).getSimpleName())).matched();
        });
        if (matches.size() == 0) {
            throw new IllegalArgumentException("no source code match");
        } else if (matches.size() > 1) {
            throw new IllegalArgumentException("multiple source code matches");
        }
        //noinspection unchecked
        return (T) matches.get(0);
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
}
