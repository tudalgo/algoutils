package org.tudalgo.algoutils.tutor.general.assertions;


import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;
import org.tudalgo.algoutils.tutor.general.basic.BasicEnvironment;
import org.tudalgo.algoutils.tutor.general.match.BasicReflectionMatchers;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.Stringifiable;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.ConstructorLink;
import org.tudalgo.algoutils.tutor.general.reflections.EnumConstantLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.Link;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.WithModifiers;
import org.tudalgo.algoutils.tutor.general.reflections.WithTypeList;
import org.tudalgo.algoutils.tutor.general.stringify.HTML;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.emptyContext;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;
import static org.tudalgo.algoutils.tutor.general.assertions.actual.Actual.unexpected;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Expected.of;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.items;
import static org.tudalgo.algoutils.tutor.general.stringify.HTML.tt;

/**
 * <p>The Assertions3 class is a collection of test utils for verifying the declarations of classes, methods, fields,
 * etc.
 * </p>
 */
@SuppressWarnings("unused")
public class Assertions3 {

    /**
     * The {@link Environment} to use.
     */
    private static final Environment ENVIRONMENT = BasicEnvironment.getInstance();

    /**
     * <p>Asserts that the given {@link TypeLink} exists in the given {@link PackageLink}.</p>
     *
     * @param linkToPackage the {@link PackageLink} to search in
     * @param matcher       the {@link Matcher} to match the {@link TypeLink} against
     * @return the {@link TypeLink} if it exists
     */
    public static TypeLink assertTypeExists(
        PackageLink linkToPackage,
        Matcher<? super TypeLink> matcher
    ) {
        var match = linkToPackage.getType(matcher);
        if (match != null) {
            return match;
        }
        var typeName = ENVIRONMENT.getStringifier().stringify(matcher);
        return fail(
            emptyContext(),
            e -> format("type %s does not exist with expected characteristics", tt(typeName))
        );
    }

    /**
     * <p>Asserts that the given {@link TypeLink} exists in the given {@link PackageLink}.</p>
     *
     * @param linkToPackage the {@link PackageLink} to search in
     * @param name          the name of the {@link TypeLink}
     * @return the {@link TypeLink} if it exists
     */
    public static TypeLink assertTypeExists(PackageLink linkToPackage, String name) {
        return assertTypeExists(linkToPackage, BasicStringMatchers.identical(name));
    }

    /**
     * <p>Asserts that the given {@link org.tudalgo.algoutils.tutor.general.reflections.Link} has the correct modifiers.</p>
     *
     * @param link       the {@link org.tudalgo.algoutils.tutor.general.reflections.Link} to check
     * @param attributes the {@link Modifier}s that should be present
     * @param <T>        the type of the {@link org.tudalgo.algoutils.tutor.general.reflections.Link}
     * @return the {@link org.tudalgo.algoutils.tutor.general.reflections.Link} if it has the correct modifiers
     */
    public static <T extends WithModifiers> T assertCorrectModifiers(
        T link,
        Modifier... attributes
    ) {
        var missingModifiers =
            stream(attributes).filter(e -> e.isNot(link.modifiers())).map(Modifier::keyword).map(HTML::tt).toList();
        if (missingModifiers.isEmpty()) {
            return link;
        }
        var typeName = ENVIRONMENT.getStringifier().stringify(link);
        var expectedModifiers = stream(attributes).map(Modifier::keyword).map(HTML::tt).toList();
        var expected = items(expectedModifiers);
        var actual = items(
            stream(Modifier.fromInt(link.modifiers()))
                .map(Modifier::keyword)
                .map(HTML::tt)
                .toList() + ". Therefor missing:",
            missingModifiers
        );
        return fail(
            expected,
            actual,
            contextBuilder().subject(link).build(),
            e -> "modifiers are not correct"
        );
    }

    /**
     * <p>Asserts that the given {@link MethodLink} has the correct return type.</p>
     *
     * @param link    the {@link MethodLink} to check
     * @param matcher the {@link Matcher} to match the return type against
     * @return the {@link MethodLink} if it has the correct return type
     */
    public static MethodLink assertCorrectReturnType(MethodLink link, Matcher<TypeLink> matcher) {
        if (matcher.match(link.returnType()).matched()) {
            return link;
        }
        var typeName = link.name();
        return fail(
            Expected.of(matcher),
            unexpected(link.returnType()),
            contextBuilder().subject(link).build(),
            e -> "return type is not correct"
        );
    }

    /**
     * <p>Asserts that the given {@link MethodLink} has the correct return type.</p>
     *
     * @param link the {@link MethodLink} to check
     * @param type the {@link TypeLink} to match the return type against
     * @return the {@link MethodLink} if it has the correct return type
     */
    public static MethodLink assertCorrectReturnType(MethodLink link, TypeLink type) {
        return assertCorrectReturnType(link, BasicReflectionMatchers.sameType(type));
    }

    /**
     * <p>Asserts that the given {@link MethodLink} has the correct return type.</p>
     *
     * @param link the {@link MethodLink} to check
     * @param type the {@link Class} to match the return type against
     * @return the {@link MethodLink} if it has the correct return type
     */
    public static MethodLink assertCorrectReturnType(MethodLink link, Class<?> type) {
        return assertCorrectReturnType(link, BasicTypeLink.of(type));
    }

    /**
     * <p>Asserts that the given {@link EnumConstantLink} exists in the enum of the given {@link TypeLink}.</p>
     *
     * @param link    the {@link TypeLink} of the enum to search in
     * @param matcher the {@link Matcher} to match the {@link EnumConstantLink} against
     * @return the {@link EnumConstantLink} if it exists
     */
    public static EnumConstantLink assertHasEnumConstant(TypeLink link, Matcher<? super Stringifiable> matcher) {
        var constant = link.getEnumConstant(matcher);
        if (constant != null) {
            return constant;
        }
        var constantName = ENVIRONMENT.getStringifier().stringify(matcher);
        return fail(
            contextBuilder().subject(link).build(),
            e -> format("enum constant %s does not exist", tt(constantName))
        );
    }

    /**
     * <p>Asserts that the given {@link EnumConstantLink} exists in the enum of the given {@link TypeLink}.</p>
     *
     * @param link the {@link TypeLink} of the enum to search in
     * @param name the name of the {@link EnumConstantLink}
     * @return the {@link EnumConstantLink} if it exists
     */
    public static EnumConstantLink assertHasEnumConstant(TypeLink link, String name) {
        return assertHasEnumConstant(link, BasicStringMatchers.identical(name));
    }

    /**
     * Assert that the parameters of the given {@link WithTypeList} are correctly declared.
     *
     * @param withParameters the {@link WithTypeList} to check
     * @param matchers       the {@link Matcher}s to match the parameters against
     * @param <T>            the type of the {@link WithTypeList}
     * @return the {@link WithTypeList} if the parameters are correct
     */
    @SafeVarargs
    public static <T extends WithTypeList> T assertCorrectParameters(
        T withParameters,
        Matcher<? super TypeLink>... matchers
    ) {
        var actual = withParameters.typeList().toArray(TypeLink[]::new);
        var name = ENVIRONMENT.getStringifier().stringify(withParameters);
        for (int i = 0; i < matchers.length; i++) {
            if (matchers.length != actual.length || !matchers[i].match(actual[i]).matched()) {
                fail(
                    Expected.of(List.of(matchers)),
                    unexpected(List.of(actual)),
                    contextBuilder().subject(withParameters).build(),
                    e -> "parameters are not correct"
                );
            }
        }
        return withParameters;
    }

    /**
     * Asserts that the given {@link Link} has the correct type parameters.
     *
     * @param link     the {@link Link} to check
     * @param matchers the {@link Matcher}s to match the type parameters against
     */
    @SafeVarargs
    public static <T extends Link> void assertCorrectTypeParameters(
        T link,
        Matcher<? super TypeVariable<?>>... matchers
    ) {
        TypeVariable<?>[] actual;
        if (link instanceof MethodLink) {
            actual = ((MethodLink) link).reflection().getTypeParameters();
        } else if (link instanceof ConstructorLink) {
            actual = ((ConstructorLink) link).reflection().getTypeParameters();
        } else if (link instanceof TypeLink) {
            actual = ((TypeLink) link).reflection().getTypeParameters();
        } else if (link instanceof FieldLink) {
            actual = ((FieldLink) link).reflection().getType().getTypeParameters();
        } else {
            throw new IllegalArgumentException("link is not a MethodLink, ConstructorLink or TypeLink");
        }
        var name = ENVIRONMENT.getStringifier().stringify(link);
        // length
        if (matchers.length != actual.length) {
            fail(
                Expected.of(List.of(matchers)),
                unexpected(List.of(actual)),
                contextBuilder().subject(link).build(),
                e -> "type parameters are not correct."
            );
        }
        for (int i = 0; i < matchers.length; i++) {
            if (matchers.length != actual.length || !matchers[i].match(actual[i]).matched()) {
                fail(
                    Expected.of(List.of(matchers)),
                    unexpected(List.of(actual)),
                    contextBuilder().subject(link).build(),
                    e -> "type parameters are not correct"
                );
            }
        }
    }

    /**
     * Assert that a method with the given characteristics exists in the given {@link TypeLink}.
     *
     * @param link    the {@link TypeLink} to search in
     * @param matcher the {@link Matcher} to match the {@link MethodLink} against
     * @return the {@link MethodLink} if it exists
     */
    public static MethodLink assertMethodExists(TypeLink link, Matcher<? super MethodLink> matcher) {
        var method = link.getMethod(matcher);
        if (method != null) {
            return method;
        }
        var matcherStr = ENVIRONMENT.getStringifier().stringify(matcher);
        return fail(
            contextBuilder().subject(link).build(),
            e -> format("there is no method with expected characteristics %s", tt(matcherStr))
        );
    }

    /**
     * Assert that a constructor with the given characteristics exists in the given {@link TypeLink}.
     *
     * @param link    the {@link TypeLink} to search in
     * @param matcher the {@link Matcher} to match the {@link ConstructorLink} against
     * @return the {@link ConstructorLink} if it exists
     */
    public static ConstructorLink assertConstructorExists(TypeLink link, Matcher<? super ConstructorLink> matcher) {
        var constructor = link.getConstructor(matcher);
        if (constructor != null) {
            return constructor;
        }
        var nameOfType = ENVIRONMENT.getStringifier().stringify(link);
        return fail(
            contextBuilder().subject(link).build(),
            e -> "there is no constructor of class %s with expected characteristics".formatted(tt(nameOfType))
        );
    }

    /**
     * Assert that a field with the given characteristics exists in the given {@link TypeLink}.
     *
     * @param link    the {@link TypeLink} to search in
     * @param matcher the {@link Matcher} to match the {@link FieldLink} against
     * @return the {@link FieldLink} if it exists
     */
    public static FieldLink assertFieldExists(TypeLink link, Matcher<? super FieldLink> matcher) {
        var field = link.getField(matcher);
        if (field != null) {
            return field;
        }
        var fieldName = ENVIRONMENT.getStringifier().stringify(matcher);
        return fail(
            contextBuilder().subject(link).build(),
            e -> format("there is no field %s with expected characteristics", tt(fieldName))
        );
    }

    /**
     * Asserts that the given {@link FieldLink} has the correct static type.
     *
     * @param link    the {@link FieldLink} to check
     * @param matcher the {@link Matcher} to match the static type against
     * @return the {@link FieldLink} if it has the correct static type
     */
    public static FieldLink assertCorrectStaticType(FieldLink link, Matcher<? super FieldLink> matcher) {
        if (matcher.match(link).matched()) {
            return link;
        }
        var typeName = link.name();
        return fail(
            of(matcher),
            unexpected(link),
            contextBuilder().subject(link).build(),
            e -> format("static type of %s is not correct", tt(typeName))
        );
    }

    /**
     * Asserts that the given {@link FieldLink} has the correct super type.
     *
     * @param link    the {@link FieldLink} to check
     * @param matcher the {@link Matcher} to match the super type against
     * @return the {@link FieldLink} if it has the correct super type
     */
    public static TypeLink assertCorrectSuperType(TypeLink link, Matcher<? super TypeLink> matcher) {
        if (matcher.match(link.superType()).matched()) {
            return link;
        }
        return fail(
            of(matcher),
            unexpected(link.superType()),
            contextBuilder().subject(link).build(),
            e -> "super type is not correct"
        );
    }

    /**
     * Asserts that the given {@link FieldLink} has the correct super type.
     *
     * @param link the {@link FieldLink} to check
     * @param type the {@link TypeLink} to match the super type against
     * @return the {@link FieldLink} if it has the correct super type
     */
    public static TypeLink assertCorrectSuperType(TypeLink link, TypeLink type) {
        return assertCorrectSuperType(link, BasicReflectionMatchers.sameType(type));
    }

    /**
     * Asserts that the given {@link FieldLink} has the correct super type.
     *
     * @param link the {@link FieldLink} to check
     * @param type the {@link Class} to match the super type against
     * @return the {@link FieldLink} if it has the correct super type
     */
    public static TypeLink assertCorrectSuperType(TypeLink link, Class<?> type) {
        return assertCorrectSuperType(link, BasicTypeLink.of(type));
    }

    /**
     * Asserts that the given {@link FieldLink} implements the given interfaces.
     *
     * @param link     the {@link FieldLink} to check
     * @param matchers the {@link Matcher}s to match the interfaces against
     * @return the {@link FieldLink} if it implements the given interfaces
     */
    @SafeVarargs
    public static TypeLink assertCorrectInterfaces(TypeLink link, Matcher<? super TypeLink>... matchers) {
        for (var matcher : matchers) {
            if (link.getInterface(matcher) != null) {
                continue;
            }
            return fail(
                of(matcher),
                unexpected(link.interfaces()),
                contextBuilder().subject(link).build(),
                e -> "interfaces are not correct"
            );
        }
        return link;
    }

    /**
     * Asserts that the given {@link FieldLink} implements the given interfaces.
     *
     * @param link  the {@link FieldLink} to check
     * @param types the {@link TypeLink}s to match the interfaces against
     * @return the {@link FieldLink} if it implements the given interfaces
     */
    @SuppressWarnings("unchecked")
    public static TypeLink assertCorrectInterfaces(TypeLink link, TypeLink... types) {
        return assertCorrectInterfaces(link, stream(types)
            .map(BasicReflectionMatchers::sameType)
            .toArray(Matcher[]::new)
        );
    }

    /**
     * Asserts that the given {@link MethodLink} has the correct throws.
     *
     * @param link     the {@link MethodLink} to check
     * @param matchers the {@link Matcher}s to match the throws against
     * @return the {@link MethodLink} if it has the correct throws
     */
    public static MethodLink assertCorrectThrows(MethodLink link, Matcher<? super TypeLink>... matchers) {
        final var exceptionTypes = stream(link.reflection().getExceptionTypes())
            .map(BasicTypeLink::of)
            .toList();
        for (var matcher : matchers) {
            if (exceptionTypes.stream().anyMatch(matcher.predicate())) {
                continue;
            }
            return fail(
                of(stream(matchers).map(Matcher::object).toList()),
                   unexpected(exceptionTypes),
                   contextBuilder().subject(link).build(),
                e -> "throws is not correct"
            );
        }
        return link;
    }

    /**
     * Asserts that the given {@link FieldLink} implements the given interfaces.
     *
     * @param link  the {@link FieldLink} to check
     * @param types the {@link Class}es to match the interfaces against
     * @return the {@link FieldLink} if it implements the given interfaces
     */
    @SuppressWarnings("unchecked")
    public static TypeLink assertCorrectInterfaces(TypeLink link, Class<?>... types) {
        return assertCorrectInterfaces(link, stream(types)
            .map(BasicTypeLink::of)
            .map(BasicReflectionMatchers::sameType)
            .toArray(Matcher[]::new)
        );
    }

    /**
     * Asserts that the given {@link MethodLink} has the same signature as the given {@link MethodLink}.
     *
     * @param expected             a {@link MethodLink} to compare against
     * @param actual               a {@link MethodLink} to check
     * @param verifyTypeParameters whether to verify the type parameters
     * @param ignoredModifiers     modifiers to ignore
     * @param <T>                  the type of the {@link MethodLink}
     */
    @SuppressWarnings("unchecked")
    public static <T extends MethodLink> void assertSameSignature(
        T expected,
        T actual,
        boolean verifyTypeParameters,
        Modifier... ignoredModifiers
    ) {
        final String baseErrorMessage = "Method Signature is not correct.";
        // name
        if (!expected.name().equals(actual.name())) {
            fail(
                of(expected.name()),
                unexpected(actual.name()),
                contextBuilder().subject(expected).build(),
                e -> baseErrorMessage + " Incorrect name."
            );
        }

        // modifiers
        assertCorrectModifiers(
            actual,
            stream(Modifier.fromIntPositive(expected.modifiers()))
                .filter(Predicate.not(List.of(ignoredModifiers)::contains))
                .toArray(Modifier[]::new)
        );

        // return type
        if (!expected.returnType().equals(actual.returnType())) {
            fail(
                of(expected.returnType()),
                unexpected(actual.returnType()),
                contextBuilder().subject(expected).build(),
                e -> baseErrorMessage + " Incorrect return type."
            );
        }
        // parameters
        assertCorrectParameters(expected, actual.typeList().stream().map(BasicReflectionMatchers::sameType)
            .toArray(Matcher[]::new));

        // type parameters
        if (verifyTypeParameters) {
            assertCorrectTypeParameters(actual, stream(expected.reflection().getTypeParameters())
                .map(BasicReflectionMatchers::sameTypeVariable)
                .toArray(Matcher[]::new));
        }

        // throws
        assertCorrectThrows(actual, stream(expected.reflection().getExceptionTypes())
            .map(BasicReflectionMatchers::sameType)
            .toArray(Matcher[]::new));
    }

    /**
     * Asserts that the given {@link MethodLink} has the same signature as the given {@link MethodLink}.
     *
     * @param expected the {@link MethodLink} to compare against
     * @param actual   the {@link MethodLink} to check
     */
    public void assertSameSignature(MethodLink expected, MethodLink actual) {
        assertSameSignature(expected, actual, true);
    }

    /**
     * Asserts that an interface method is implemented in the given {@link TypeLink}. This method checks that the
     * method has the correct signature and that it is annotated with {@link Override}.
     *
     * @param link            the {@link TypeLink} to check for the implementation
     * @param interfaceMethod the {@link MethodLink} of the interface method
     */
    public static MethodLink assertImplementsInterfaceMethod(TypeLink link, MethodLink interfaceMethod) {
        var method = (BasicMethodLink) assertMethodExists(
            link,
            BasicReflectionMatchers.sameTypes(interfaceMethod.typeList().toArray(TypeLink[]::new))
        );
        assertSameSignature(interfaceMethod, method, false, Modifier.ABSTRACT);
        if (!method.getCtElement().hasAnnotation(Override.class)) {
            fail(
                emptyContext(),
                e -> "Method does not have an @Override annotation."
            );
        }
        return method;
    }
}
