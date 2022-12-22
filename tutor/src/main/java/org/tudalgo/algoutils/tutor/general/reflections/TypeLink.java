package org.tudalgo.algoutils.tutor.general.reflections;

import org.tudalgo.algoutils.tutor.general.match.Matcher;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.tudalgo.algoutils.tutor.general.match.MatchingUtils.firstMatch;
import static org.tudalgo.algoutils.tutor.general.match.MatchingUtils.matches;

/**
 * <p>An interface used to simply access classes, interfaces and enumerations.</p>
 *
 * @author Dustin Glaser
 */
public interface TypeLink extends Link, WithType, WithModifiers, WithName, WithImports {

    /**
     * <p>Returns the field contained in the type linked by this type link and matched by the given matcher with the highest match score.</p>
     *
     * <p>If there is not such a field, null is returned.</p>
     *
     * @param matcher the matcher
     *
     * @return the field or null
     */
    default FieldLink getField(Matcher<? super FieldLink> matcher) {
        return firstMatch(getFields(), matcher);
    }

    /**
     * <p>Returns a list of all fields contained in the type linked by this type link and
     * matched by the given matcher.</p>
     *
     * <p>The list is sorted by match scores in descending order.</p>
     *
     * @param matcher the matcher
     *
     * @return the sublist of field
     */
    default List<? extends FieldLink> getFields(Matcher<? super FieldLink> matcher) {
        return matches(getFields(), matcher);
    }

    /**
     * <p>Returns a collection of all fields contained in the type linked by this type link.</p>
     *
     * @return the collection of fields
     */
    Collection<? extends FieldLink> getFields();

    /**
     * <p>Returns a collection of all interfaces extended (interfaces) or implemented (classes and enums) by the type linked by this type link.</p>
     *
     * @return the collection of interfaces
     */
    Collection<? extends TypeLink> interfaces();

    default List<? extends TypeLink> interfaces(Matcher<? super TypeLink> matcher) {
        return matches(interfaces(), matcher);
    }

    default TypeLink getInterface(Matcher<? super TypeLink> matcher) {
        return firstMatch(interfaces(), matcher);
    }

    /**
     * <p>Returns the type link to the super type of the type linked by this type link.</p>
     *
     * @return the link to the super type
     */
    TypeLink superType();

    /**
     * <p>Returns the method contained in the type linked by this type link and matched by the given matcher with the highest match score.</p>
     *
     * <p>If there is not such a method, null is returned.</p>
     *
     * @param matcher the matcher
     *
     * @return the method or null
     */
    default MethodLink getMethod(Matcher<? super MethodLink> matcher) {
        return firstMatch(getMethods(), matcher);
    }

    /**
     * <p>Returns a list of all methods contained in the type linked by this type link and
     * matched by the given matcher.</p>
     *
     * <p>The list is sorted by match scores in descending order.</p>
     *
     * @param matcher the matcher
     *
     * @return the sublist of methods
     */
    default List<? extends MethodLink> getMethods(Matcher<? super MethodLink> matcher) {
        return matches(getMethods(), matcher);
    }

    /**
     * <p>Returns a collection of all methods contained in the type linked by this type link.</p>
     *
     * @return the collection of methods
     */
    Collection<? extends MethodLink> getMethods();

    /**
     * <p>Returns a collection of constructors declared in the type linked by this type link.</p>
     *
     * @return the collection of constructors
     */
    Collection<? extends ConstructorLink> getConstructors();

    /**
     * <p>Returns the constructor contained in the type linked by this type link and matched by the given matcher with the highest match score.</p>
     *
     * <p>If there is not such a constructor, null is returned.</p>
     *
     * @param matcher the matcher
     *
     * @return the constructor or null
     */
    default ConstructorLink getConstructor(Matcher<? super ConstructorLink> matcher) {
        return firstMatch(getConstructors(), matcher);
    }

    /**
     * <p>Returns a list of all constructors contained in the type linked by this type link and
     * matched by the given matcher.</p>
     *
     * <p>The list is sorted by match scores in descending order.</p>
     *
     * @param matcher the matcher
     *
     * @return the sublist of constructors
     */
    default List<? extends ConstructorLink> getConstructors(Matcher<? super ConstructorLink> matcher) {
        return matches(getConstructors(), matcher);
    }


    /**
     * <p>Returns a list of all enum constants contained in the type linked by this type link and matched by the given matcher.</p>
     *
     * <p>The list is sorted by match scores in descending order.</p>
     *
     * @param matcher the matcher
     *
     * @return the sublist of enum constants
     */
    default List<? extends EnumConstantLink> getEnumConstants(Matcher<? super EnumConstantLink> matcher) {
        return matches(getEnumConstants(), matcher);
    }

    /**
     * <p>Returns the enum constant contained in the type linked by this type link and matched by the given matcher with the highest match score.</p>
     *
     * <p>If there is not such a enum constant, null is returned.</p>
     *
     * @param matcher the matcher
     *
     * @return the enum constant or null
     */
    default EnumConstantLink getEnumConstant(Matcher<? super EnumConstantLink> matcher) {
        return firstMatch(getEnumConstants(), matcher);
    }

    /**
     * <p>Returns a collection of all enum constants contained in the type linked by this type link.</p>
     *
     * @return the collection of enum constants
     */
    Collection<? extends EnumConstantLink> getEnumConstants();

    /**
     * @deprecated use {@linkplain #reflection()} instead
     */
    @Deprecated
    default Class<?> link() {
        return reflection();
    }

    @Override
    default int modifiers() {
        return link().getModifiers();
    }


    /**
     * <p>Returns the actual {@linkplain Class type} behind this type link.</p>
     *
     * @return the type
     */
    Class<?> reflection();


    @Override
    default String name() {
        return link().getSimpleName();
    }

    @Override
    default TypeLink type() {
        return this;
    }

}
