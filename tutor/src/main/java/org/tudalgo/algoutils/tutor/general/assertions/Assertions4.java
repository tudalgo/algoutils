package org.tudalgo.algoutils.tutor.general.assertions;

import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObjects;
import org.tudalgo.algoutils.tutor.general.basic.BasicEnvironment;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.stringify.HTML;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtForEach;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtWhile;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.SpoonClassNotFoundException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.text;

/**
 * <p>The Assertions4 class provides methods for analyzing the source code of a student solution with Spoon. This includes
 * utilities for checking for forbidden method calls, recursive method calls, and more.</p>
 */
@SuppressWarnings("unused")
public class Assertions4 {

    /**
     * Private constructor to prevent instantiation.
     */
    private Assertions4() {
    }

    /**
     * Asserts that the given {@link CtElement} is of the given type.
     *
     * @param expected the expected type
     * @param actual   the actual element
     * @param context  the context of this test
     * @param comment  the comment to be displayed in case of a failure
     * @param <T>      the expected type
     * @return the actual element
     */
    public static <T extends CtElement> T assertIsCtElementOfType(
        Class<T> expected,
        CtElement actual,
        Context context,
        PreCommentSupplier<? super ResultOfObject<Class<? extends CtElement>>> comment
    ) {
        Assertions2.<Class<? extends CtElement>>testOfObjectBuilder()
            .expected(ExpectedObjects.instanceOf(expected, true)).build()
            .run(actual.getClass())
            .check(context, comment);
        //noinspection unchecked
        return (T) actual;
    }

    /**
     * Asserts that the solution is one statement (containing only one semicolon).
     *
     * @param statement the statement
     * @param context   the context of this test
     * @param comment   the comment to be displayed in case of a failure
     * @return the statement
     */
    public static CtStatement assertIsOneStatement(
        CtStatement statement,
        Context context,
        PreCommentSupplier<? super ResultOfObject<CtStatement>> comment
    ) {
        List<CtStatement> statements;
        if (statement instanceof CtComment) {
            statements = List.of();
        } else if (statement instanceof CtBlock<?> block) {
            statements = block.getStatements().stream().filter(s -> !(s instanceof CtComment)).toList();
        } else {
            return statement;
        }
        Assertions2.assertEquals(
            1,
            statements.size(),
            context,
            r -> "block contains more than one statement"
        );
        if (statements.get(0) instanceof CtBlock<?>) {
            return assertIsOneStatement(statements.get(0), context, comment);
        }
        return statements.get(0);
    }

    /**
     * Asserts that the given {@link CtIf} statement has an else statement.
     *
     * @param ifStatement the if statement
     * @param context     the context of this test
     * @param comment     the comment to be displayed in case of a failure
     * @return the else statement
     */
    public static CtStatement assertHasElseStatement(
        CtIf ifStatement,
        Context context,
        PreCommentSupplier<? super Result<?, ?, ?, ?>> comment
    ) {
        if (ifStatement.getElseStatement() == null) {
            return Assertions2.fail(
                text("else statement"),
                text("no else statement"),
                context,
                comment
            );
        }
        return ifStatement.getElseStatement();
    }

    /**
     * A filter for {@link CtElement}s that are iterative.
     */
    private static final Filter<CtElement> ITERATIVE_ELEMENTS_FILTER = e ->
        e instanceof CtFor || e instanceof CtForEach || e instanceof WhileStatement || e instanceof DoStatement;

    /**
     * Asserts that the given method does not contain iterative statements.
     *
     * @param method  the method
     * @param context the context of this test
     * @param comment the comment to be displayed in case of a failure
     */
    public static void assertIsNotIteratively(
        CtMethod<?> method,
        Context context,
        PreCommentSupplier<? super ResultOfFail> comment
    ) {
        if (method.getElements(ITERATIVE_ELEMENTS_FILTER).isEmpty()) {
            return;
        }
        Assertions2.fail(
            context,
            comment
        );
    }

    /**
     * Asserts that the given method does not contain recursive calls.
     *
     * @param method  the method
     * @param context the context of this test
     * @param comment the comment to be displayed in case of a failure
     */
    public static void assertIsNotRecursively(
        CtMethod<?> method,
        Context context,
        PreCommentSupplier<? super ResultOfFail> comment
    ) {
        assertDoesNotCall(method, method, new HashSet<>(), context, comment);
    }

    /**
     * Asserts that the given method does not contain calls to the given method.
     *
     * @param executable   the method
     * @param calledMethod the method that must not be called
     * @param visited      the visited methods
     * @param context      the context of this test
     * @param comment      the comment to be displayed in case of a failure
     */
    private static void assertDoesNotCall(
        CtExecutable<?> executable,
        CtMethod<?> calledMethod,
        Set<CtExecutable<?>> visited,
        Context context,
        PreCommentSupplier<? super ResultOfFail> comment
    ) {
        if (executable == null) {
            return;
        }
        executable.getElements(new TypeFilter<>(CtInvocation.class)).forEach(i -> {
            var e = i.getExecutable().getDeclaration();
            if (visited.contains(e)) {
                return;
            }
            if (e instanceof CtMethod<?> m && m.equals(calledMethod)) {
                Assertions2.fail(
                    context,
                    comment
                );
            }
            visited.add(e);
            assertDoesNotCall(e, calledMethod, visited, context, comment);
        });
        visited.add(executable);
    }

    /**
     * A filter for {@link CtElement}s that are conditional.
     */
    private static final Filter<CtElement> CONDITIONAL_ELEMENT_FILTER = e ->
        e instanceof CtIf || e instanceof CtConditional<?> || e instanceof CtFor || e instanceof CtWhile;


    /**
     * Asserts that the given method does not contain conditional statements.
     *
     * @param method  the method
     * @param context the context of this test
     */
    public static void assertNotConditional(
        CtMethod<?> method,
        Context context
    ) {
        if (method.getElements(CONDITIONAL_ELEMENT_FILTER).isEmpty()) {
            return;
        }
        Assertions2.fail(
            contextBuilder()
                .subject(method)
                .add(context)
                .build(),
            r -> "method contains conditional elements"
        );
    }

    /**
     * Asserts that the given method does not use any of the given elements.
     *
     * @param method   the method
     * @param context  the context of this test
     * @param elements the elements that must not be used
     */
    @SafeVarargs
    public static void assertElementsNotUsed(
        CtMethod<?> method,
        Context context,
        Matcher<CtElement>... elements
    ) {
        for (var e : elements) {
            if (method.filterChildren(c -> Assertions2.callObject(() -> e.match(c).matched())).list().isEmpty()) {
                continue;
            }
            Assertions2.fail(
                context,
                r -> "forbidden statement with characteristic " + HTML.tt(e.characteristic()) + " was used."
            );
        }
    }


    /**
     * Builds a list of {@link Matcher}s for the given {@link BasicMethodLink}s.
     *
     * @param links the links
     * @return the matchers
     */
    @SuppressWarnings("unchecked")
    public static Matcher<CtElement>[] buildCtElementBlacklist(BasicMethodLink... links) {
        return Arrays.stream(links).map(m -> Matcher.of(
            ct -> {
                try {
                    return ct instanceof CtInvocation<?> i
                        && i.getExecutable() != null
                        && i.getExecutable().getActualMethod() != null
                        && i.getExecutable().getActualMethod().equals(m.reflection());
                } catch (SpoonClassNotFoundException e) {
                    // for some reason Library sources are not available during Jagr runs if using * imports. So we
                    // just ignore this error and give the student the benefit of the doubt.
                    return false;
                }
            },
            "method " + BasicEnvironment
                .getInstance()
                .getStringifier()
                .stringify(m.reflection())
        )).toArray(Matcher[]::new);
    }

    /**
     * Builds a list of {@link Matcher}s for the given {@link Method}s.
     *
     * @param links the links
     * @return the matchers
     */
    public static Matcher<CtElement>[] buildCtElementBlacklist(Method... links) {
        return buildCtElementBlacklist(
            Arrays.stream(links)
                .map(BasicMethodLink::of)
                .toArray(BasicMethodLink[]::new)
        );
    }

    /**
     * Builds a list of {@link Matcher}s for the given {@link Method}s.
     *
     * @param links Supplier for the links, exceptions are properly handled
     * @return the matchers
     */
    @SafeVarargs
    public static Matcher<CtElement>[] buildCtElementBlacklist(ObjectCallable<Method>... links) {
        return buildCtElementBlacklist(
            Arrays.stream(links)
                .map(l -> Assertions2.callObject(
                    l,
                    Assertions2.emptyContext(),
                    r -> "The desired Method could not be retrieved. This is likely an Error in the tests."
                ))
                .toArray(Method[]::new)
        );
    }
}

