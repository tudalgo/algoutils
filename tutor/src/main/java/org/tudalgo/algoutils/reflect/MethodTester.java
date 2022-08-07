package org.tudalgo.algoutils.reflect;

import org.junit.jupiter.api.Assertions;
import org.mockito.invocation.Invocation;
import org.sourcegrade.docwatcher.api.MethodDocumentation;
import org.sourcegrade.docwatcher.api.SourceDocumentation;
import spoon.Launcher;
import spoon.reflect.code.CtCodeElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;

import javax.management.RuntimeErrorException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A method tester which tests properties of a class.
 *
 * @author Ruben Deisenroth
 */
public class MethodTester {
    /**
     * Indicator if super class check are allowed.
     */
    public boolean allowSuperClass;
    /**
     * The method identifier containing the name of the method and the similarity to accept
     * alternative identifiers.
     */
    IdentifierMatcher methodIdentifier;
    /**
     * The resolved method that will be tested.
     */
    Method theMethod;
    /**
     * The expected access modifier count.
     */
    int accessModifier;
    /**
     * The expected return type of the method.
     */
    private Class<?> returnType;
    /**
     * The expected parameters of the method.
     */
    private List<ParameterMatcher> parameters;
    /**
     * A class test (used for invoking).
     */
    private ClassTester<?> classTester;
    /**
     * Indicator whether to allow derived return types.
     */
    private boolean looseReturnTypeChecking;

    /**
     * Constructs and initializes a method tester.
     *
     * @param classTester     the class tester used to invoke the method
     * @param methodName      the expected method name to test
     * @param similarity      the minimum matching similarity
     * @param accessModifier  the expected access modifier count
     * @param returnType      the expected return type of the method
     * @param parameters      the expected parameters of the method
     * @param allowSuperClass the indicator @param allowSuperClass the indicator if super class
     *                        check are allowed
     */
    public MethodTester(final ClassTester<?> classTester, final String methodName, final double similarity, final int accessModifier,
                        final Class<?> returnType, final List<ParameterMatcher> parameters, final boolean allowSuperClass) {
        this.classTester = classTester;
        this.methodIdentifier = new IdentifierMatcher(methodName, similarity);
        this.accessModifier = accessModifier;
        this.returnType = returnType;
        this.parameters = parameters;
        this.allowSuperClass = allowSuperClass;
    }

    /**
     * Constructs and initializes a method tester.
     *
     * @param classTester    the class tester used to invoke the method
     * @param methodName     the expected method name to test
     * @param similarity     the minimum matching similarity
     * @param accessModifier the expected access modifier count
     * @param returnType     the expected return type of the method
     * @param parameters     the expected parameters of the method
     */
    public MethodTester(final ClassTester<?> classTester, final String methodName, final double similarity, final int accessModifier,
                        final Class<?> returnType, final List<ParameterMatcher> parameters) {
        this(classTester, methodName, similarity, accessModifier, returnType, parameters, false);
    }

    /**
     * Constructs and initializes a method tester.
     *
     * @param classTester     the class tester used to invoke the method
     * @param methodName      the expected method name to test
     * @param similarity      the minimum matching similarity
     * @param accessModifier  the expected access modifier count
     * @param returnType      the expected return type of the method
     * @param parameters      the expected parameters of the method
     * @param allowSuperClass the indicator if super class check are allowed
     */
    public MethodTester(final ClassTester<?> classTester, final String methodName, final double similarity, final int accessModifier,
                        final Class<?> returnType, final ArrayList<ParameterMatcher> parameters, final boolean allowSuperClass,
                        final boolean looseReturnTypeChecking) {
        this(classTester, methodName, similarity, accessModifier, returnType, parameters, allowSuperClass);
        this.looseReturnTypeChecking = looseReturnTypeChecking;
    }

    /**
     * Constructs and initializes a method tester.
     *
     * @param classTester    the class tester used to invoke the method
     * @param methodName     the expected method name to test
     * @param similarity     the minimum matching similarity
     * @param accessModifier the expected access modifier count
     * @param returnType     the expected return type of the method
     */
    public MethodTester(final ClassTester<?> classTester, final String methodName, final double similarity, final int accessModifier,
                        final Class<?> returnType) {
        this(classTester, methodName, similarity, accessModifier, returnType, null);
    }

    /**
     * Constructs and initializes a method tester.
     *
     * @param classTester    the class tester used to invoke the method
     * @param methodName     the expected method name to test
     * @param similarity     the minimum matching similarity
     * @param accessModifier the expected access modifier count
     */
    public MethodTester(final ClassTester<?> classTester, final String methodName, final double similarity, final int accessModifier) {
        this(classTester, methodName, similarity, accessModifier, null, null);
    }

    /**
     * Constructs and initializes a method tester.
     *
     * @param classTester the class tester used to invoke the method
     * @param methodName  the expected method name to test
     * @param similarity  the minimum matching similarity
     * @param returnType  the expected return type of the method
     * @param parameters  the expected parameters of the method
     */
    public MethodTester(final ClassTester<?> classTester, final String methodName, final double similarity, final Class<?> returnType,
                        final ArrayList<ParameterMatcher> parameters) {
        this(classTester, methodName, similarity, -1, returnType, parameters);
    }

    /**
     * Constructs and initializes a method tester.
     *
     * @param classTester the class tester used to invoke the method
     * @param methodName  the expected method name to test
     * @param similarity  the minimum matching similarity
     */
    public MethodTester(final ClassTester<?> classTester, final String methodName, final double similarity) {
        this(classTester, methodName, similarity, -1, null);
    }

    /**
     * Constructs and initializes a method tester.
     *
     * @param classTester the class tester used to invoke the method
     * @param methodName  the expected method name to test
     */
    public MethodTester(final ClassTester<?> classTester, final String methodName) {
        this(classTester, methodName, 1, -1, null);
    }

    /**
     * Generates a predefined message for an invalid return type.
     *
     * @param methodName the method name used in the message
     * @return a predefined message for an invalid return type.
     */
    public static String getInvalidReturnTypeMessage(final String methodName) {
        return String.format("falscher Rückgabetyp für Methode %s", methodName);
    }

    /**
     * Generates a predefined should not have parameter message.
     *
     * @param methodName the method name used in the message
     * @return a predefined should not have parameter message
     */
    public static String getShouldNotHaveParameterMessage(final String methodName) {
        return String.format("Methode %s sollte keine Parameter haben.", methodName);
    }

    /**
     * Counts the matching parameters.
     *
     * @param expectedParameters the expected parameters
     * @param actualParameters   the actual Parameters
     * @param ignoreNames        the indicator  whether to ignore parameter names
     * @return the number of matched parameters
     */
    public static int countMatchingParameters(
        final List<ParameterMatcher> expectedParameters,
        final List<Parameter> actualParameters,
        final boolean ignoreNames) {
        int count = 0;
        for (int i = 0; i < expectedParameters.size(); i++) {
            final var matcher = expectedParameters.get(i);
            final var param = actualParameters.get(i);
            if (param.getType() != matcher.parameterType) {
                continue;
            }
            if (!ignoreNames && matcher.identifierName != null && matcher.similarity > 0) {
                if (TestUtils.similarity(matcher.identifierName, param.getName()) < matcher.similarity) {
                    continue;
                }
            }
            count++;
        }
        return count;
    }

    /**
     * Counts the matching parameters.
     *
     * @param m           the actual method with the parameters
     * @param methodName  the name of the method
     * @param parameters  the expected parameters
     * @param ignoreNames the indicator  whether to ignore parameter names
     * @return the number of matched parameters
     */
    public static int countMatchingParameters(final Method m, final String methodName, final List<ParameterMatcher> parameters,
                                              final boolean ignoreNames) {
        assertMethodNotNull(m, methodName);
        if (parameters == null || parameters.isEmpty()) {
            return 0;
        }
        return countMatchingParameters(parameters, new ArrayList<>(List.of(m.getParameters())), ignoreNames);
    }

    /**
     * Tests whether the method parameters match.
     *
     * @param expectedParameters the expected parameters
     * @param actualParameters   the actual parameters
     * @param ignoreNames        the indicator whether to ignore parameter names
     */
    public static void assertParametersMatch(final List<ParameterMatcher> expectedParameters,
                                             final List<Parameter> actualParameters,
                                             final boolean ignoreNames) {

        if (expectedParameters == null || expectedParameters.isEmpty()) {
            assertTrue(actualParameters == null || actualParameters.isEmpty(),
                "Es sollen keine Parameter vorhanden sein.");
        } else {
            for (int i = 0; i < expectedParameters.size(); i++) {
                final var matcher = expectedParameters.get(i);
                assertTrue(i < actualParameters.size(), "Zu wenige Parameter.");
                final var param = actualParameters.get(i);
                if (matcher.allowSubTypes) {
                    assertInstanceOf(matcher.parameterType, param.getType(),
                        "Falscher Parametertyp an Index " + "i. (Subtypen erlaubt)");
                } else {
                    assertSame(matcher.parameterType, param.getType(), "Falscher Parametertyp an Index " + "i.");
                }
                if (!ignoreNames && param.isNamePresent() && matcher.identifierName != null && matcher.similarity > 0) {
                    assertTrue(TestUtils.similarity(matcher.identifierName, param.getName()) >= matcher.similarity,
                        "Falscher Parametername. Erwartet: " + matcher.identifierName + ", Erhalten: "
                            + param.getName());
                }
            }
            assertEquals(actualParameters.size(), expectedParameters.size(),
                "Die folgenden Parameter waren nicht gefordert:"
                    + actualParameters.subList(expectedParameters.size(), actualParameters.size()));
        }
    }

    /**
     * Tests whether the method parameters match.
     *
     * @param m           the actual method to verify
     * @param methodName  the expected name of the method
     * @param parameters  the expected parameter
     * @param ignoreNames the indicator whether to ignore parameter names
     */
    public static void assertParametersMatch(final Method m, final String methodName, final List<ParameterMatcher> parameters,
                                             final boolean ignoreNames) {
        assertMethodNotNull(m, methodName);
        assertParametersMatch(parameters, List.of(m.getParameters()), ignoreNames);
    }

    /**
     * Tests whether the method parameters matches with the expected parameters.
     */
    public void assertParametersMatch() {
        assertParametersMatch(this.theMethod, this.methodIdentifier.identifierName, this.parameters, false);
    }

    /**
     * Tests whether the specified method is not {@code null}.
     *
     * @param m    the method to check
     * @param name the expected method name of the method to check
     */
    public static void assertMethodNotNull(final Method m, final String name) {
        assertNotNull(m, getMethodNotFoundMessage(name));
    }

    /**
     * Generates a predefined class tester {@code null} message.
     *
     * @param methodName the expected method name
     * @return a predefined class tester {@code null} message
     */
    public static String getClassTesterNullMessage(final String methodName) {
        return String.format("Fehlerhafter Test für Methode %s: Kein Klassentester gegeben.", methodName);
    }

    /**
     * Represents a safe string as an array.
     *
     * @param array the array to convert
     * @return the string representation of the array
     */
    public static String safeArrayToString(final Object... array) {
        var paramsString = "[]";
        if (array != null) {
            try {
                paramsString = Arrays.toString(array);
            } catch (final Exception e) {
                paramsString = Arrays.stream(
                        array).map(x -> x.getClass().getName() + "@" + Integer.toHexString(x.hashCode()))
                    .collect(Collectors.joining(", ", "[", "]"));
            }
        }
        return paramsString;
    }

    /**
     * Returns all fields from a class and its super classes recursively.
     *
     * @param methods the found fields so far (initial value is an empty list)
     * @param clazz   the class to search
     * @return all fields from a class and its super classes
     */
    private static List<Method> getAllMethods(final List<Method> methods, final Class<?> clazz) {
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));

        if (clazz.getSuperclass() != null) {
            getAllMethods(methods, clazz.getSuperclass());
        }

        return methods;
    }

    /**
     * Returns all fields from a class and its super classes recursively.
     *
     * @param clazz the class to search
     * @return all fields from a class and its super classes
     */
    public static List<Method> getAllMethods(final Class<?> clazz) {
        return getAllMethods(new ArrayList<>(), clazz);
    }

    /**
     * Returns the class tester used to invoke the method.
     *
     * @return the class tester used to invoke the method
     */
    public ClassTester<?> getClassTester() {
        return this.classTester;
    }

    /**
     * Sets the class tester used to invoke the method to the specified value.
     *
     * @param classTester the new class tester value
     */
    public void setClassTester(final ClassTester<?> classTester) {
        this.classTester = classTester;
    }

    /**
     * Returns the method identifier containing the name of the method and the similarity to accept
     * alternative identifiers.
     *
     * @return method identifier containing the name of the method and the similarity to accept alternative identifiers
     */
    public IdentifierMatcher getMethodIdentifier() {
        return this.methodIdentifier;
    }

    /**
     * Set the method identifier containing the name of the method and the similarity to accept
     * alternative identifiers to the specified value.
     *
     * @param methodIdentifier the new method identifier
     */
    public void setMethodIdentifier(final IdentifierMatcher methodIdentifier) {
        this.methodIdentifier = methodIdentifier;
    }

    /**
     * Returns the expected class type of the test method.
     *
     * @return the expected class type of the test method
     */
    public Class<?> getReturnType() {
        return this.returnType;
    }

    /**
     * Sets the expected class type of the test method to the specified value.
     *
     * @param returnType the new return type
     */
    public void setReturnType(final Class<?> returnType) {
        this.returnType = returnType;
    }

    /**
     * Returns {@code true} if subtype are allowed for the return type.
     *
     * @return {@code true} if subtype are allowed for the return type
     */
    public boolean isLooseReturnTypeChecking() {
        return this.looseReturnTypeChecking;
    }

    /**
     * Sets the boolean value whether to allow subtypes for the return type.
     *
     * @param looseReturnTypeChecking the indicator whether to allow it or not
     */
    public void setLooseReturnTypeChecking(final boolean looseReturnTypeChecking) {
        this.looseReturnTypeChecking = looseReturnTypeChecking;
    }

    /**
     * Tests whether the actual return type matches the expected return type.
     */
    public void assertReturnType() {
        if (this.returnType == null) {
            throw new RuntimeErrorException(new Error(), "Faulty Test: Cannot assert return type null");
        }
        assertMethodResolved();
        if (this.looseReturnTypeChecking) {
            assertInstanceOf(this.returnType, this.theMethod.getReturnType(),
                getInvalidReturnTypeMessage(this.methodIdentifier.identifierName));
        } else {
            assertSame(this.returnType, this.theMethod.getReturnType(),
                getInvalidReturnTypeMessage(this.methodIdentifier.identifierName));
        }
    }

    /**
     * Verifies that the method was declared correctly.
     *
     * @return this method tester
     */
    public MethodTester verify() {
        if (!methodResolved()) {
            resolveMethod();
        }
        if (this.accessModifier >= 0) {
            assertAccessModifier();
        }
        assertParametersMatch();
        assertReturnType();
        return this;
    }

    /**
     * Returns the expected parameters.
     *
     * @return the expected parameters
     */
    public List<ParameterMatcher> getParameters() {
        return this.parameters;
    }

    /**
     * Sets the expected parameters to the specified value.
     *
     * @param parameters the new expected parameters
     */
    public void setParameters(final List<ParameterMatcher> parameters) {
        this.parameters = parameters;
    }

    /**
     * Returns the method that should be tested.
     *
     * @return the method that should be tested
     */
    public Method getTheMethod() {
        return this.theMethod;
    }

    /**
     * Sets the method that should be tested to the specified value.
     *
     * @param theMethod the new method to be tested
     */
    public void setTheMethod(final Method theMethod) {
        this.theMethod = theMethod;
    }

    /**
     * Adds an expected parameters' matcher to this tester.
     *
     * @param parameterMatchers the parameters' matcher to add
     */
    public void addParameter(final ParameterMatcher... parameterMatchers) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<>();
        }
        this.parameters.addAll(Arrays.asList(parameterMatchers));
    }

    /**
     * Adds an expected parameters' matcher to this tester.
     *
     * @param type       the type of the expected parameter type
     * @param name       the name of the parameter to match
     * @param similarity the minimum required similarity
     */
    public void addParameter(final Class<?> type, final String name, final double similarity) {
        addParameter(new ParameterMatcher(name, similarity, type));
    }

    /**
     * Adds an expected parameter matchers to this tester.
     *
     * @param type the type of the expected parameter type
     */
    public void addParameter(final Class<?> type) {
        addParameter(new ParameterMatcher(null, 1, type));
    }

    /**
     * Generates a predefined method not found message.
     *
     * @param methodName the name of the method used for the message
     * @return a predefined method not found message
     */
    public static String getMethodNotFoundMessage(final String methodName) {
        return String.format("Methode %s existiert nicht.", methodName);
    }

    /**
     * Generates a predefined method not found message.
     *
     * @return a predefined method not found message
     */
    public String getMethodNotFoundMessage() {
        return getMethodNotFoundMessage(this.methodIdentifier.identifierName);
    }

    /**
     * Returns {@code true} if test method is not {@code null}.
     *
     * @return {@code true} if test method is not {@code null}
     */
    public boolean methodResolved() {
        return this.theMethod != null;
    }

    /**
     * Tests whether the test method could be resolved.
     */
    public void assertMethodResolved() {
        assertTrue(methodResolved(), getMethodNotFoundMessage());
    }

    /**
     * Tests whether the class tester used to invoke the method is not {@code null}.
     */
    public void assertClassTesterNotNull() {
        assertNotNull(this.classTester, getClassTesterNullMessage(this.methodIdentifier.identifierName));
    }

    /**
     * Returns {@code true} if the class tester used to invoke the method is not {@code null} and
     * can be resolved.
     *
     * @return {@code true} if the class tester used to invoke the method is not {@code null} and can be resolved
     */
    public boolean classResolved() {
        return this.classTester != null && this.classTester.class_resolved();
    }

    /**
     * Tests whether the class tester used to invoke the method can be resolved.
     */
    public void assertClassResolved() {
        assertClassTesterNotNull();
        this.classTester.assertClassResolved();
    }

    /**
     * Returns {@code true} if the method can be invoked, more formally returns {@code true} if the
     * class tester used to invoke the method, the method itself can be resolved.
     *
     * @return {@code true} if the method can be invoked.
     */
    public boolean invokeable() {
        return classResolved() && this.classTester.classInstanceResolved() && methodResolved()
            && this.classTester.classInstanceResolved();
    }

    /**
     * Tests whether the method can be invoked.
     *
     * @see #invokeable()
     */
    public void assertInvokeable() {
        assertClassResolved();
        this.classTester.assertclassInstanceResolved();
        assertMethodResolved();
    }

    /**
     * Invokes test method using the class tester.
     *
     * @param params the parameters of the method that should be invoked
     * @return the return value of the method after its invocation
     */
    public Object invoke(final Object... params) {
        assertInvokeable();
        assertDoesNotThrow(() -> this.theMethod.setAccessible(true), "Konnte Methode nicht ausführen.");
        Object returnValue = null;
        try {
            returnValue = this.theMethod.invoke(this.classTester.getClassInstance(), params);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            fail("Method Could not be invoked.", e);
        }
        return returnValue;
    }

    /**
     * Gets the invocations of the test method.
     *
     * @return the invocations of the test method
     */
    public List<Invocation> getInvocations() {
        assertMethodResolved();
        this.classTester.assertSpied();
        return this.classTester.getMockingDetails().getInvocations().stream()
            .filter(x -> x.getMethod().getName().equals(getTheMethod().getName())).collect(Collectors.toList());
    }

    /**
     * Gets the number of invocations. (How often Has the test method been invoked?)
     *
     * @return the number of invocations
     */
    public int getInvocationCount() {
        return getInvocations().size();
    }

    /**
     * Returns the  random valid parameter values.
     *
     * @return the random valid parameter values
     */
    public Object[] getRandomParams() {
        return Arrays.stream(getTheMethod().getParameters()).map(x -> ClassTester.getRandomValue(x.getType())).toArray();
    }

    /**
     * Invokes the test method with random parameters.
     *
     * @return the return value of the method after its invocation
     */
    public Object invokeWithRandomParams() {
        assertMethodResolved();
        return invoke(getRandomParams());
    }

    /**
     * Tests whether none of the blacklisted constructs where used in the constructor.
     *
     * @param disallowedConstructs the not allowed constructs in the constructor
     */
    public void assertConstructsNotUsed(final List<Class<? extends CtCodeElement>> disallowedConstructs) {
        assureMethodResolved();
        final Launcher spoon = Assertions.assertDoesNotThrow(() -> getClassTester().assureSpoonLauncherModelsBuild().getSpoon(),
            "Could not Create Spoon Launcher");
        final CtType<?> type = assertDoesNotThrow(
            () -> spoon.getModel().getAllTypes().stream().filter(CtType::isTopLevel).findFirst().orElseThrow(),
            "Could not resolve Class Source for Class " + this.classTester.getClassIdentifier().identifierName + "."
                + "available Class Sources:" + spoon
                .getModel().getAllTypes().toString());
        final CtMethod<?> method = assertDoesNotThrow(
            () -> type.getMethodsByName(getMethodIdentifier().identifierName).stream().findFirst()
                .orElseThrow(),
            "Could not resolve Method Source for Method " + getTheMethod().getName());

        for (final var construct : disallowedConstructs) {
            assertTrue(method.getElements(new TypeFilter<>(construct)).isEmpty(),
                String.format("Ein verbotener Sprachkonstrukt wurde in Methode %s verwendet: %s.",
                    getMethodIdentifier().identifierName, construct.getSimpleName()));
        }
    }

    /**
     * Tests whether the return value after the invocation with the specified parameters are
     * correct.
     *
     * @param expected the expected return value
     * @param params   the parameters used for the invocation
     */
    public void assertReturnValueEquals(final Object expected, final Object... params) {
        assertReturnValueEquals(expected, "", params);
    }

    /**
     * Tests whether the return value after the invocation with the specified parameters are
     * correct.
     *
     * @param expected          the expected return value
     * @param additionalMessage the additional message if the test fails
     * @param params            the parameters used for the invocation
     */
    public void assertReturnValueEquals(final Object expected, final String additionalMessage, final Object... params) {
        assertEquals(expected, invoke(params), "Falsche Rückgabe bei Methode" + getMethodIdentifier().identifierName
            + (params.length > 0 ? "mit Parameter(n):" + safeArrayToString(params) : "") + additionalMessage);
    }

    /**
     * Returns the expected access modifier count.
     *
     * @return the expected access modifier count
     */
    public int getAccessModifier() {
        return this.accessModifier;
    }

    /**
     * Sets  the expected access modifier count to the new value.
     *
     * @param accessModifier the new  expected access modifier count
     */
    public void setAccessModifier(final int accessModifier) {
        this.accessModifier = accessModifier;
    }

    /**
     * Tests whether the actual access modifier count matches with the expected one.
     */
    public void assertAccessModifier() {
        if (this.accessModifier >= 0) {
            TestUtils.assertModifier(this.accessModifier, this.theMethod);
        }
    }

    /**
     * Resolves the method with the tolerances, more formally the method is searched first using its
     * name. If multiple overloads are found then the method with the most matching parameters
     * according to {@link  #countMatchingParameters(Method, String, List, boolean)} is chosen.
     *
     * @param theClass        the class to search
     * @param methodName      the expected method name
     * @param similarity      the minimum required similarity
     * @param parameters      the expected parameters
     * @param allowSuperClass the indicator whether to search in super classes as well
     * @return the resolved method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Method, String, List, boolean)
     */
    public Method resolveMethod(final Class<?> theClass, final String methodName, double similarity,
                                final List<ParameterMatcher> parameters, final boolean allowSuperClass) {
        similarity = Math.max(0, Math.min(similarity, 1));
        ClassTester.assertClassNotNull(theClass, "zu Methode " + methodName);
        final List<Method> methods = allowSuperClass ? getAllMethods(theClass) : Arrays.asList(theClass.getDeclaredMethods());
        var bestMatch = methods.stream().min((x, y) -> Double.compare(TestUtils.similarity(methodName, y.getName()),
            TestUtils.similarity(methodName, x.getName()))).orElse(null);
        assertMethodNotNull(bestMatch, methodName);
        final var sim = TestUtils.similarity(bestMatch.getName(), methodName);
        assertTrue(sim >= similarity, getMethodNotFoundMessage() + "Ähnlichster Methodenname:" + bestMatch.getName()
            + " with " + sim + " similarity.");
        if (parameters != null) {
            // Account for overloads
            final var matches = methods.stream().filter(x -> TestUtils.similarity(methodName, x.getName()) == sim)
                .collect(Collectors.toCollection(ArrayList::new));
            if (matches.size() > 1) {
                // Find best match according to parameter options
                bestMatch = matches.stream()
                    .min((x, y) -> Integer.compare(countMatchingParameters(y, methodName, parameters, true),
                        countMatchingParameters(x, methodName, parameters, true))).orElse(null);
            }
        }

        return this.theMethod = bestMatch;
    }

    /**
     * Resolves the method with the tolerances, more formally the method is searched first using its
     * name. If multiple overloads are found then the method with the most matching parameters
     * according to {@link  #countMatchingParameters(Method, String, List, boolean)} is chosen.
     *
     * @param theClass   the class to search
     * @param methodName the expected method name
     * @param similarity the minimum required similarity
     * @param parameters the expected parameters
     * @return the resolved method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Method, String, List, boolean)
     */
    public Method resolveMethod(final Class<?> theClass, final String methodName, final double similarity,
                                final List<ParameterMatcher> parameters) {
        return resolveMethod(theClass, methodName, similarity, parameters, false);
    }

    /**
     * Resolves the method with the tolerances, more formally the method is searched first using its
     * name. If multiple overloads are found then the method with the most matching parameters
     * according to {@link  #countMatchingParameters(Method, String, List, boolean)} is chosen.
     *
     * @param similarity the minimum required similarity
     * @return the resolved method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Method, String, List, boolean)
     */
    public Method resolveMethod(final double similarity) {
        return resolveMethod(this.classTester.theClass, this.methodIdentifier.identifierName, similarity, this.parameters);
    }

    /**
     * Resolves the method with the tolerances, more formally the method is searched first using its
     * name. If multiple overloads are found then the method with the most matching parameters
     * according to {@link  #countMatchingParameters(Method, String, List, boolean)} is chosen.
     *
     * @return the resolved method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Method, String, List, boolean)
     */
    public Method resolveMethod() {
        assertClassTesterNotNull();
        if (!classResolved()) {
            this.classTester.resolveClass();
        }
        return resolveMethod(this.classTester.theClass, this.methodIdentifier.identifierName, this.methodIdentifier.similarity,
            this.parameters, this.allowSuperClass);
    }

    /**
     * Assures that the method has been resolved.
     *
     * @return this method tester
     */
    public MethodTester assureMethodResolved() {
        if (!methodResolved()) {
            resolveMethod();
        }
        return this;
    }

    /**
     * Gets method documentation for JavaDoc.
     *
     * @param d the source documentation
     * @return the method documentation
     */
    public MethodDocumentation getMethodDocumentation(final SourceDocumentation d) {
        try {
            this.classTester.assureClassResolved();
            final var resolvedMethod = assureMethodResolved().getTheMethod();
            return d.forTopLevelType(this.classTester.getTheClass().getName()).forMethod(
                resolvedMethod.getName(), resolvedMethod.getParameterTypes());
        } catch (final Throwable e) {
            return d.forTopLevelType("").forMethod("");
        }
    }
}
