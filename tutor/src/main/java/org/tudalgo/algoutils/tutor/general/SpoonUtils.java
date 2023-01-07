package org.tudalgo.algoutils.tutor.general;

import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.Stringifiable;
import org.tudalgo.algoutils.tutor.general.reflections.BasicConstructorLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.Link;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import spoon.Launcher;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtTypedElement;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.SpoonClassNotFoundException;
import spoon.support.compiler.VirtualFile;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     *
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
     * Collects all imports of the given element only in the method body without expanding.
     *
     * @param element the element to collect the imports from
     *
     * @return the collected imports
     */
    public static Set<TypeLink> collectImports(CtElement element) {
        @SuppressWarnings("deprecation")
        Set<TypeLink> imports = element.getReferencedTypes().stream()
            .distinct()
            // Remove null references
            .filter(Predicate.not(it -> it.getSimpleName().equals("<nulltype>")))
            // Deprecated in Spoon, since we are mapping to TypeLink, we can safely ignore this
            .map(reference -> {
                try {
                    return reference.getActualClass();
                } catch (SpoonClassNotFoundException e) {
                    return null;
                }
            })
            .filter(Predicate.not(Objects::isNull))
            .map(BasicTypeLink::of)
            .collect(Collectors.toSet());
        return imports;
    }

    /**
     * Collects imports from constructor calls only of the given element.
     *
     * @param element the element to collect the imports from
     * @param visited the links that have already been visited which should not be visited again
     *
     * @return the collected imports
     */
    public static Set<TypeLink> collectConstructorCallImports(CtElement element, Set<Link> visited) {
        Set<TypeLink> imports = new HashSet<>();

        Set<BasicConstructorLink> constructorLinks = element.getElements(CtConstructorCall.class::isInstance).stream()
            .map(CtConstructorCall.class::cast)
            .map(CtConstructorCall::getExecutable)
            // null if the constructor is not found
            .filter(Predicate.not(Objects::isNull))
            .map(CtExecutableReference::getActualConstructor)
            .map(BasicConstructorLink::of)
            .filter(Predicate.not(visited::contains))
            .distinct()
            .peek(visited::add)
            .collect(Collectors.toSet());

        if (!constructorLinks.isEmpty()) {
            constructorLinks.stream().map(BasicConstructorLink::imports).forEach(imports::addAll);
        }

        return imports;
    }

    /**
     * Collects imports from method invocations only of the given element.
     *
     * @param element the element to collect the imports from
     * @param visited the links that have already been visited which should not be visited again
     *
     * @return the collected imports
     */
    public static Set<TypeLink> collectMethodInvocationImports(CtElement element, Set<Link> visited) {
        Set<TypeLink> imports = new HashSet<>();

        Set<BasicMethodLink> methodLinks = element.getElements(CtInvocation.class::isInstance).stream()
            .map(CtInvocation.class::cast)
            .map(CtInvocation::getExecutable)
            .map(CtExecutableReference::getActualMethod)
            // null if the method is not found
            .filter(Predicate.not(Objects::isNull))
            .map(BasicMethodLink::of)
            .filter(Predicate.not(visited::contains))
            .distinct()
            .peek(visited::add)
            .collect(Collectors.toSet());

        if (!methodLinks.isEmpty()) {
            methodLinks.stream().map(BasicMethodLink::imports).forEach(imports::addAll);
        }

        return imports;
    }

    /**
     * Collects imports from fields only of the given element.
     *
     * @param element the element to collect the imports from
     * @param visited the links that have already been visited which should not be visited again
     *
     * @return the collected imports
     */
    public static Set<TypeLink> collectFieldImports(CtElement element, Set<Link> visited) {
        Set<TypeLink> imports = new HashSet<>();

        @SuppressWarnings("unchecked")
        Stream<CtTypeReference<?>> fields = element.getElements(CtField.class::isInstance).stream()
            .map(CtField.class::cast)
            .map(CtTypedElement::getType);
        @SuppressWarnings("deprecation")
        Set<BasicTypeLink> fieldLinks = fields
            // Deprecated in Spoon, since we are mapping to TypeLink, we can safely ignore this
            .map(CtTypeReference::getActualClass)
            .map(BasicTypeLink::of)
            .filter(Predicate.not(visited::contains))
            .distinct()
            .peek(visited::add)
            .collect(Collectors.toSet());

        if (!fieldLinks.isEmpty()) {
            fieldLinks.stream().map(BasicTypeLink::imports).forEach(imports::addAll);
        }

        return imports;
    }

}
