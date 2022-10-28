package org.tudalgo.algoutils.tutor.general.reflections;

import org.tudalgo.algoutils.tutor.general.match.Identifiable;
import org.tudalgo.algoutils.tutor.general.match.Match;
import org.tudalgo.algoutils.tutor.general.match.Matcher;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.tudalgo.algoutils.tutor.general.match.Matcher.always;

/**
 * <p>An interface used to simply access classes, interfaces and enumerations.</p>
 *
 * @author Dustin Glaser
 */
public interface TypeLink extends Link, Identifiable {

    /**
     * <p>Returns the field contained in the type linked by this type link and matched by the given matcher with the highest match score.</p>
     *
     * <p>If there is not such a field, null is returned.</p>
     *
     * @param matcher the matcher
     * @return the field or null
     */
    default FieldLink getField(Matcher<? super FieldLink> matcher) {
        return getFields(matcher).stream().findFirst().orElse(null);
    }

    /**
     * <p>Returns a list of all fields contained in the type linked by this type link and
     * matched by the given matcher.</p>
     *
     * <p>The list is sorted by match scores in descending order.</p>
     *
     * @param matcher the matcher
     * @return the sublist of field
     */
    default List<FieldLink> getFields(Matcher<? super FieldLink> matcher) {
        return getFields().stream().map(matcher::match).filter(Match::matched).sorted().map(Match::object).toList();
    }

    /**
     * <p>Returns a collection of all fields contained in the type linked by this type link.</p>
     *
     * @return the collection of fields
     */
    default Collection<FieldLink> getFields() {
        return getFields(always());
    }

    /**
     * <p>Returns a collection of all interfaces extended (interfaces) or implemented (classes and enums) by the type linked by this type link.</p>
     *
     * @return the collection of interfaces
     */
    Collection<TypeLink> getInterfaces();

    /**
     * <p>Returns the method contained in the type linked by this type link and matched by the given matcher with the highest match score.</p>
     *
     * <p>If there is not such a method, null is returned.</p>
     *
     * @param matcher the matcher
     * @return the method or null
     */
    default MethodLink getMethod(Matcher<? super MethodLink> matcher) {
        return getMethods(matcher).stream().findFirst().orElse(null);
    }

    /**
     * <p>Returns a list of all methods contained in the type linked by this type link and
     * matched by the given matcher.</p>
     *
     * <p>The list is sorted by match scores in descending order.</p>
     *
     * @param matcher the matcher
     * @return the sublist of methods
     */
    default Collection<MethodLink> getMethods(Matcher<? super MethodLink> matcher) {
        return getMethods().stream().map(matcher::match).filter(Match::matched).sorted().map(Match::object).toList();
    }

    /**
     * <p>Returns a collection of all methods contained in the type linked by this type link.</p>
     *
     * @return the collection of methods
     */
    Collection<MethodLink> getMethods();

    /**
     * <p>Returns the actual type behind this type link.</p>
     *
     * @return the type
     */
    default Type getType() {
        return Type.of(link());
    }

    /**
     * <p>Returns the actual {@linkplain Class type} behind this type link.</p>
     *
     * @return the type
     */
    Class<?> link();

    /**
     * <p>An enumeration of actual types behind type links.</p>
     *
     * @author Dustin Glaser
     */
    enum Type {
        CLASS, INTERFACE, ENUM, ANNOTATION;

        private static Type of(Class<?> type) {
            if (type.isAnnotation()) return ANNOTATION;
            if (type.isEnum()) return ENUM;
            if (type.isInterface()) return INTERFACE;
            return CLASS;
        }
    }
}
