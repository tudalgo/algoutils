package org.tudalgo.algoutils.tutor.general.assertions;

import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObjects;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.WithImports;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtForEach;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.text;

@SuppressWarnings("unused")
public class Assertions4 {

    private Assertions4() {
    }

    public static <T extends CtElement> T assertIsCtElementOfType(
        Class<T> expected,
        CtElement actual,
        Context context,
        PreCommentSupplier<? super ResultOfObject<Class<? extends CtElement>>> comment
    ) {
        Assertions2.<Class<? extends CtElement>> testOfObjectBuilder()
            .expected(ExpectedObjects.instanceOf(expected, true)).build()
            .run(actual.getClass())
            .check(context, comment);
        //noinspection unchecked
        return (T) actual;
    }

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

    private static final Filter<CtElement> ITERATIVE_ELEMENTS_FILTER = e ->
        e instanceof CtFor || e instanceof CtForEach || e instanceof WhileStatement || e instanceof DoStatement;

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

    public static void assertIsNotRecursively(
        CtMethod<?> method,
        Context context,
        PreCommentSupplier<? super ResultOfFail> comment
    ) {
        assertDoesNotCall(method, method, new HashSet<>(), context, comment);
    }

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
     * Asserts that the given link with import contains the given imports.
     *
     * @param link    the link to check
     * @param imports the imports to check
     * @param context the context information about the assertion to display if the assertion fails
     * @param comment the comment to display if the assertion fails
     */
    public static void assertContainsClasses(
        WithImports link,
        Set<Class<?>> imports,
        Context context,
        PreCommentSupplier<? super ResultOfFail> comment
    ) {
        assertContainsTypeLinks(link, imports.stream().map(BasicTypeLink::of).collect(Collectors.toSet()), context, comment);
    }

    /**
     * Asserts that the given link with import contains the given imports.
     *
     * @param link    the link to check
     * @param imports the imports to check
     * @param context the context information about the assertion to display if the assertion fails
     * @param comment the comment to display if the assertion fails
     */
    public static void assertContainsTypeLinks(
        WithImports link,
        Set<TypeLink> imports,
        Context context,
        PreCommentSupplier<? super ResultOfFail> comment
    ) {
        if (link.imports().containsAll(imports)) {
            return;
        }
        Assertions2.fail(context, comment);
    }

    /**
     * Asserts that the given link with import does not contain the given imports
     *
     * @param link    the link to check
     * @param imports the imports to check
     * @param context the context information about the assertion to display if the assertion fails
     * @param comment the comment to display if the assertion fails
     */
    public static void assertContainsNotClasses(
        WithImports link,
        Set<Class<?>> imports,
        Context context,
        PreCommentSupplier<? super ResultOfFail> comment
    ) {
        assertContainsTypeLinks(link, imports.stream().map(BasicTypeLink::of).collect(Collectors.toSet()), context, comment);
    }

    /**
     * Asserts that the given link with import does not contain the given imports.
     *
     * @param link    the link to check
     * @param imports the imports to check
     * @param context the context information about the assertion to display if the assertion fails
     * @param comment the comment to display if the assertion fails
     */
    public static void assertContainsNotTypeLinks(
        WithImports link,
        Set<TypeLink> imports,
        Context context,
        PreCommentSupplier<? super ResultOfFail> comment
    ) {
        if (!link.imports().containsAll(imports)) {
            return;
        }
        Assertions2.fail(context, comment);
    }


}

