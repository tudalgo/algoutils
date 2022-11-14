package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;
import org.tudalgo.algoutils.tutor.general.basic.BasicEnvironment;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.Stringifiable;
import org.tudalgo.algoutils.tutor.general.reflections.*;
import org.tudalgo.algoutils.tutor.general.stringify.HTML;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.actual.Actual.unexpected;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Expected.of;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.items;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.text;

@SuppressWarnings("unused")
public class Assertions3 {

    private static final Environment ENVIRONMENT = BasicEnvironment.getInstance();

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
            text("exists"),
            text("does not exist"),
            emptyContext(),
            e -> format("type %s does not exist with expected characteristics", typeName)
        );
    }

    public static <T extends WithModifiers> T assertCorrectModifiers(
        T link,
        Modifier... attributes
    ) {
        var missingModifiers = stream(attributes).filter(e -> e.isNot(link.modifiers())).map(Modifier::name).map(HTML::tt).toList();
        if (missingModifiers.isEmpty()) {
            return link;
        }
        var typeName = ENVIRONMENT.getStringifier().stringify(link);
        var expectedModifiers = stream(attributes).map(Modifier::name).map(HTML::tt).toList();
        var expected = items(expectedModifiers);
        var actual = items("not", missingModifiers);
        return fail(
            expected,
            actual,
            contextBuilder().subject(link).build(),
            e -> format("modifiers of %s are not correct", typeName)
        );
    }

    public static MethodLink assertCorrectReturnType(MethodLink link, Matcher<TypeLink> matcher) {
        if (matcher.match(link.returnType()).matched()) {
            return link;
        }
        var typeName = link.name();
        return fail(
            Expected.of(matcher),
            unexpected(link.returnType()),
            contextBuilder().subject(link).build(),
            e -> format("return type of method %s is not correct", typeName)
        );
    }

    public static EnumConstantLink assertHasEnumConstant(TypeLink link, Matcher<? super Stringifiable> matcher) {
        var constant = link.getEnumConstant(matcher);
        if (constant != null) {
            return constant;
        }
        var constantName = ENVIRONMENT.getStringifier().stringify(matcher);
        return fail(
            text("exists"),
            text("does not exist"),
            contextBuilder().subject(link).build(),
            e -> format("enum constant %s does not exist", constantName)
        );
    }

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
                    e -> "%s does not have correct parameters".formatted(name)
                );
            }
        }
        return withParameters;
    }

    public static MethodLink assertMethodExists(TypeLink link, Matcher<? super MethodLink> matcher) {
        var method = link.getMethod(matcher);
        if (method != null) {
            return method;
        }
        var methodName = ENVIRONMENT.getStringifier().stringify(matcher);
        return fail(
            text("exists"),
            text("does not exist"),
            contextBuilder().subject(link).build(),
            e -> format("there is no method %s with expected characteristics", methodName)
        );
    }

    public static ConstructorLink assertConstructorExists(TypeLink link, Matcher<? super ConstructorLink> matcher) {
        var constructor = link.getConstructor(matcher);
        if (constructor != null) {
            return constructor;
        }
        var nameOfType = ENVIRONMENT.getStringifier().stringify(link);
        return fail(
            text("exists"),
            text("does not exist"),
            contextBuilder().subject(link).build(),
            e -> "there is no constructor of %s with expected characteristics".formatted(nameOfType)
        );
    }

    public static FieldLink assertFieldExists(TypeLink link, Matcher<? super FieldLink> matcher) {
        var field = link.getField(matcher);
        if (field != null) {
            return field;
        }
        var fieldName = ENVIRONMENT.getStringifier().stringify(matcher);
        return fail(
            text("exists"),
            text("does not exist"),
            contextBuilder().subject(link).build(),
            e -> format("there is no field %s with expected characteristics", fieldName)
        );
    }

    public static FieldLink assertCorrectStaticType(FieldLink link, Matcher<? super FieldLink> matcher) {
        if (matcher.match(link).matched()) {
            return link;
        }
        var typeName = link.name();
        return fail(
            of(matcher),
            unexpected(link),
            contextBuilder().subject(link).build(),
            e -> format("static type of %s is not correct", typeName)
        );
    }

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
}
