package org.tudalgo.algoutils.assertions.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;
import org.tudalgo.algoutils.assertions.*;
import org.tudalgo.algoutils.assertions.options.AssertionOption;
import org.tudalgo.algoutils.tutor.general.SpoonUtils;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.lang.reflect.Method;
import java.util.*;

import static org.tudalgo.algoutils.assertions.verification.ValueBasedVerification.wrap;

/**
 * Provides miscellaneous verifications by integrating with other libraries like Mockito and Spoon.
 */
public final class MiscVerifications {

    private static Factory SPOON_FACTORY;
    private static final Map<Class<?>, CtTypeReference<?>> SPOON_TYPE_CACHE = new HashMap<>();

    // Do not instantiate
    private MiscVerifications() {
    }

    /**
     * Verifies that the Mockito verification in the given expression or statement succeeds.
     * Using this verification is not too different from using {@link org.mockito.Mockito#verify(Object, VerificationMode)} directly,
     * but this verification in conjunction with the AlgoUtils' Assertion Framework enables test-writers to modify the result using
     * {@link AssertionOption AssertionOptions}.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatStatement(ActualWrapper.Statement, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, ?, ?> passesMockitoVerification() {
        return BehaviorBasedVerification.of(actual -> {
            AssertionResult<?, ?> result;
            try {
                actual.call();
                result = AssertionResult.of(true);
            } catch (MockitoAssertionError e) {
                result = AssertionResult.of(false, "Invocation of Mockito.verify did not succeed");
                result.getErrorBuilder().setCause(e);
            } catch (Throwable t) {
                result = AssertionResult.of(false, "An unexpected exception occurred");
                result.getErrorBuilder().setCause(t);
            }
            return result;
        });
    }

    /**
     * Verifies that the method under test is recursive (calls itself at least once).
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Method, ?> isRecursive() {
        return verifyRecursive(true);
    }

    /**
     * Verifies that the method under test is not recursive (does not call itself, even indirectly).
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Method, ?> isNotRecursive() {
        return verifyRecursive(false);
    }

    private static AssertionVerification<?, Method, ?> verifyRecursive(boolean expectedRecursion) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Method method = actual.getValue();
            CtMethod<?> ctMethod = getCtMethod(method);
            Set<CtExecutable<?>> executables = new HashSet<>();
            getExecutablesRecursively(executables, ctMethod, ctMethod);

            if (expectedRecursion) {
                return AssertionResult.of(executables.contains(ctMethod),
                    "Method %s does not call itself".formatted(AssertionUtils.getMethodSignature(method)));
            } else {
                return AssertionResult.of(!executables.contains(ctMethod),
                    "Method %s called itself".formatted(AssertionUtils.getMethodSignature(method)));
            }
        }));
    }

    /**
     * Verifies that the method under test is iterative (uses at least one loop of any kind).
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Method, ?> isIterative() {
        return verifyIterative(true);
    }

    /**
     * Verifies that the method under test is not iterative (does not have any loops).
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Method, ?> isNotIterative() {
        return verifyIterative(false);
    }

    private static AssertionVerification<?, Method, ?> verifyIterative(boolean expectedIteration) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Method method = actual.getValue();
            CtMethod<?> ctMethod = getCtMethod(method);
            List<CtLoop> loops = ctMethod.getElements(new TypeFilter<>(CtLoop.class));

            if (expectedIteration) {
                return AssertionResult.of(!loops.isEmpty(),
                    "Method %s does not use any loops".formatted(AssertionUtils.getMethodSignature(method)));
            } else {
                return AssertionResult.of(loops.isEmpty(),
                    "Method %s used loops".formatted(AssertionUtils.getMethodSignature(method)));
            }
        }));
    }

    /**
     * Verifies that the method under test uses conditional statements (if-else).
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Method, ?> usesConditionalStatements() {
        return verifyConditionalStatement(true);
    }

    /**
     * Verifies that the method under test does not use any conditional statements (if-else).
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Method, ?> doesNotUseConditionalStatements() {
        return verifyConditionalStatement(false);
    }

    private static AssertionVerification<?, Method, ?> verifyConditionalStatement(boolean expectedConditional) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Method method = actual.getValue();
            CtMethod<?> ctMethod = getCtMethod(method);
            List<CtIf> conditionals = ctMethod.getElements(new TypeFilter<>(CtIf.class));

            if (expectedConditional) {
                return AssertionResult.of(!conditionals.isEmpty(),
                    "Method %s does not use any conditional statements".formatted(AssertionUtils.getMethodSignature(method)));
            } else {
                return AssertionResult.of(conditionals.isEmpty(),
                    "Method %s used conditional statements".formatted(AssertionUtils.getMethodSignature(method)));
            }
        }));
    }

    /**
     * Verifies that the method under test uses conditional statements (if-else).
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Method, ?> usesTernaryOperator() {
        return verifyTernaryOperator(true);
    }

    /**
     * Verifies that the method under test does not use any conditional statements (if-else).
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Method, ?> doesNotUseTernaryOperator() {
        return verifyTernaryOperator(false);
    }

    private static AssertionVerification<?, Method, ?> verifyTernaryOperator(boolean expectedConditional) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Method method = actual.getValue();
            CtMethod<?> ctMethod = getCtMethod(method);
            List<CtConditional<?>> ternaries = ctMethod.getElements(new TypeFilter<>(CtConditional.class));

            if (expectedConditional) {
                return AssertionResult.of(!ternaries.isEmpty(),
                    "Method %s does not use the ternary operator".formatted(AssertionUtils.getMethodSignature(method)));
            } else {
                return AssertionResult.of(ternaries.isEmpty(),
                    "Method %s used a ternary operator".formatted(AssertionUtils.getMethodSignature(method)));
            }
        }));
    }

    /**
     * Verifies that the method under test consists of exactly one statement.
     *
     * <p>
     * Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Method, ?> hasOneStatement() {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Method method = actual.getValue();
            CtMethod<?> ctMethod = getCtMethod(method);

            return AssertionResult.of(
                ctMethod.getBody()
                    .getStatements()
                    .stream()
                    .filter(statement -> !(statement instanceof CtComment))
                    .count() == 1,
                "Method %s does not consist of exactly one statement".formatted(AssertionUtils.getMethodSignature(method)));
        }));
    }

    // Utils

    private static CtTypeReference<?> getSpoonTypeRef(Class<?> clazz) {
        return SPOON_TYPE_CACHE.computeIfAbsent(clazz, cls -> {
            if (SPOON_FACTORY == null) {
                SPOON_FACTORY = SpoonUtils.getType(clazz.getName()).getFactory();
            }
            return SPOON_FACTORY.createCtTypeReference(clazz);
        });
    }

    private static CtMethod<?> getCtMethod(Method method) {
        return getSpoonTypeRef(method.getDeclaringClass())
            .getTypeDeclaration()
            .getMethod(method.getName(), Arrays.stream(method.getParameterTypes())
                .map(MiscVerifications::getSpoonTypeRef)
                .toArray(CtTypeReference[]::new));
    }

    private static void getExecutablesRecursively(Set<CtExecutable<?>> visited, CtExecutable<?> initial, CtExecutable<?> executable) {
        if (executable == null) {
            return;
        }

        for (CtInvocation<?> invocation : executable.getElements(new TypeFilter<>(CtInvocation.class))) {
            CtExecutable<?> exec = invocation.getExecutable().getDeclaration();
            if (visited.contains(exec)) {
                continue;
            }

            visited.add(exec);
            getExecutablesRecursively(visited, initial, exec);
        }
        if (!executable.equals(initial)) {
            visited.add(executable);
        }
    }
}
