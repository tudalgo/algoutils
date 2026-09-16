package org.tudalgo.algoutils.assertions.verification;

import kotlin.Pair;
import org.tudalgo.algoutils.AlgoUtils;
import org.tudalgo.algoutils.assertions.*;
import org.tudalgo.algoutils.assertions.options.AssertionOption;
import org.tudalgo.algoutils.descriptors.Descriptor;
import org.tudalgo.algoutils.descriptors.Descriptors;
import org.tudalgo.algoutils.descriptors.NotFound;
import org.tudalgo.algoutils.descriptors.non_generic.*;
import org.tudalgo.algoutils.descriptors.TypeDescriptor;
import org.tudalgo.algoutils.descriptors.generic.TypeVariableDescriptor;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.tudalgo.algoutils.assertions.verification.ValueBasedVerification.wrap;

/**
 * Provides verifications on reflection objects, such as classes and members, and {@link org.tudalgo.algoutils.descriptors Descriptors}.
 * These verifications can be used with the {@link org.tudalgo.algoutils.assertions.Assertions} class to assert properties of classes and members in tests.
 */
public final class ReflectionVerifications {

    // Do not instantiate
    private ReflectionVerifications() {}

    // Packages

    /**
     * Verifies that the package with the given name exists.
     * <p>
     * Note that if the package exists and a class within it was not previously loaded, it might be loaded when invoking
     * {@link AssertionVerification#verify(ActualWrapper)} on this object.
     * This will cause the class to be linked and the class initializer to be executed.
     * <p>
     * Use with {@link Assertions#assertThat(AssertionVerification, AssertionOption[])}.
     *
     * @param packageName the fully-qualified name of the class
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, ?, Package> packageExists(String packageName) {
        if (!packageName.startsWith(AlgoUtils.SUBMISSION_ID)) throw new IllegalArgumentException("Package name must start with submission ID");
        return BehaviorBasedVerification.of(ignored -> wrap(() -> {
            Optional<Package> pkg = AlgoUtils.SUBMISSION_CLASSES.stream()
                .filter(s -> s.matches("^%s\\.[^.]+".formatted(packageName)))
                .findAny()
                .map(s -> {
                    try {
                        return Class.forName(s).getPackage();
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                });

            return AssertionResult.of(pkg.isPresent(), null, pkg.orElse(null),
                "Could not find package " + packageName);
        }));
    }

    /**
     * Verifies that the described package exists.
     * <p>
     * Note that if the package exists and a class within it was not previously loaded, it might be loaded when invoking
     * {@link AssertionVerification#verify(ActualWrapper)} on this object.
     * This will cause the class to be linked and the class initializer to be executed.
     * <p>
     * Use with {@link Assertions#assertThat(AssertionVerification, AssertionOption[])}.
     *
     * @param packageDescriptor the descriptor for the package
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, ?, Package> packageExists(PackageDescriptor packageDescriptor) {
        Optional<AssertionResult<Object, Package>> checkResult = checkDescriptors(
            "Cannot verify if package exists because the expected package was not found",
            packageDescriptor
        );
        return checkResult.isPresent() ? actual -> checkResult.get() : packageExists(packageDescriptor.getName());
    }

    // Common for classes and members

    /**
     * Verifies that the entity under test has the given modifiers.
     * The actual value must be either a class or a class member (i.e., a field, constructor or method).
     * This verification only checks if the actual value has the given modifiers set, it may have additional
     * modifiers that are not covered by the ones that were passed to this method.
     * See {@link #hasExactModifiers(int)} for matching modifiers exactly.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param modifiers the modifiers to check for
     * @return an {@link AssertionVerification} object to verify the assertion
     * @throws IllegalArgumentException if the actual value is not a class or a class member
     * @see Modifier
     */
    public static AssertionVerification<Integer, ?, Integer> hasModifiers(int modifiers) {
        return hasModifiers(modifiers, false);
    }

    /**
     * Verifies that the entity under test has exactly the given modifiers.
     * The actual value must be either a class or a class member (i.e., a field, constructor or method).
     * See {@link #hasModifiers(int)} for checking if a class or class member has at least the specified modifiers set.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param modifiers the modifiers to check for
     * @return an {@link AssertionVerification} object to verify the assertion
     * @throws IllegalArgumentException if the actual value is not a class or a class member
     * @see Modifier
     */
    public static AssertionVerification<Integer, ?, Integer> hasExactModifiers(int modifiers) {
        return hasModifiers(modifiers, true);
    }

    private static AssertionVerification<Integer, ?, Integer> hasModifiers(int modifiers, boolean exact) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Object obj = actual.getValue();
            int actualModifiers;
            String prefix;

            switch (obj) {
                case Class<?> clazz -> {
                    actualModifiers = clazz.getModifiers();
                    prefix = "Class " + clazz.getName();
                }
                case Member member -> {
                    actualModifiers = member.getModifiers();
                    prefix = AssertionUtils.getPrettyString(member);
                }
                default -> throw new IllegalArgumentException("Can not get modifiers of non-class or non-member objects");
            }

            AssertionResult<Integer, Integer> result;
            if (exact) {
                result = AssertionResult.of(actualModifiers == modifiers,
                    modifiers,
                    actualModifiers,
                    "Modifiers of %s do not match the expected ones".formatted(prefix));
            } else {
                result = AssertionResult.of((actualModifiers & modifiers) == modifiers,
                    modifiers,
                    actualModifiers,
                    "%s does not have the required modifiers".formatted(prefix));
            }
            result.getErrorBuilder()
                .setExpectedStringRepresentation(Modifier.toString(modifiers))
                .setActualStringRepresentation(Modifier.toString(actualModifiers));
            return result;
        }));
    }

    /**
     * Verifies that the entity under test declares the given type parameters.
     * The actual value must implement the {@link GenericDeclaration} interface, so it must be either a class or an executable
     * (i.e., a constructor or method).
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param typeVariableDescriptors descriptors for the type variables to check for
     * @return an {@link AssertionVerification} object to verify the assertion
     * @throws IllegalArgumentException if the actual value is not a class, constructor or method
     */
    public static AssertionVerification<List<TypeVariableDescriptor>, GenericDeclaration, List<TypeVariableDescriptor>> declaresTypeParameters(
        TypeVariableDescriptor... typeVariableDescriptors
    ) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            GenericDeclaration genericDeclaration = actual.getValue();
            List<TypeVariableDescriptor> expectedTypeVariables = List.of(typeVariableDescriptors);
            List<TypeVariableDescriptor> actualTypeVariables = Arrays.stream(genericDeclaration.getTypeParameters())
                .map(Descriptors::forTypeVariable)
                .toList();
            String prefix = switch (genericDeclaration) {
                case Class<?> clazz -> "Class %s".formatted(clazz.getName());
                case Constructor<?> constructor -> "Constructor %s".formatted(AssertionUtils.getMethodSignature(constructor));
                case Method method -> "Method %s".formatted(AssertionUtils.getMethodSignature(method));
                default -> throw new IllegalArgumentException("Unsupported actual type");
            };

            return AssertionResult.of(expectedTypeVariables.equals(actualTypeVariables), expectedTypeVariables, actualTypeVariables,
                "%s does not declare the correct type parameters".formatted(prefix));
        }));
    }

    /**
     * Verifies that the executable (i.e., constructor or method) under test was declared with the given (generic) parameter types.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param typeDescriptors descriptors for the expected parameter types, may describe generic and non-generic types
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<List<TypeDescriptor>, Executable, List<TypeDescriptor>> declaresGenericParameters(
        TypeDescriptor... typeDescriptors
    ) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Optional<AssertionResult<List<TypeDescriptor>, List<TypeDescriptor>>> checkResult = checkDescriptors(
                "Cannot verify actual parameter types because an expected parameter type was not found",
                typeDescriptors
            );
            if (checkResult.isPresent()) return checkResult.get();

            Executable executable = actual.getValue();
            List<TypeDescriptor> expectedParameterTypes = List.of(typeDescriptors);
            List<TypeDescriptor> actualParameterTypes = Arrays.stream(executable.getGenericParameterTypes())
                .map(Descriptors::forType)
                .toList();
            return AssertionResult.of(
                actualParameterTypes.equals(expectedParameterTypes),
                expectedParameterTypes,
                actualParameterTypes,
                "Generic parameter types of %s do not match the expected ones".formatted(AssertionUtils.getPrettyString(executable))
            );
        }));
    }

    /**
     * Verifies that the executable (i.e., constructor or method) under test was declared with the given exceptions.
     * To check if an executable was declared with the correct generic exceptions, see {@link #declaresExceptions(TypeDescriptor...)}.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param exceptions the expected exception types
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Set<Class<?>>, Executable, Set<Class<?>>> declaresExceptions(Class<?>... exceptions) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Executable executable = actual.getValue();
            Set<Class<?>> expectedExceptions = Set.of(exceptions);
            Set<Class<?>> actualExceptions = Set.of(executable.getExceptionTypes());
            return AssertionResult.of(
                actualExceptions.containsAll(expectedExceptions),
                expectedExceptions,
                actualExceptions,
                "Constructor or method does not declare the required exception(s)"
            );
        }));
    }

    /**
     * Verifies that the executable (i.e., constructor or method) under test was declared with the given (generic) exceptions.
     * To check if an executable was declared with the correct exception types, regardless of genericity, see {@link #declaresExceptions(Class[])}.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param exceptions descriptors for the expected exception types, may describe generic and non-generic types
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Set<TypeDescriptor>, Executable, Set<TypeDescriptor>> declaresExceptions(TypeDescriptor... exceptions) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Optional<AssertionResult<Set<TypeDescriptor>, Set<TypeDescriptor>>> checkResult = checkDescriptors(
                "Cannot verify actual exception types because an expected exception type was not found",
                exceptions
            );
            if (checkResult.isPresent()) return checkResult.get();

            Executable executable = actual.getValue();
            Set<TypeDescriptor> expectedExceptions = Set.of(exceptions);
            Set<TypeDescriptor> actualExceptions = Arrays.stream(executable.getGenericExceptionTypes())
                .map(Descriptors::forType)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
            return AssertionResult.of(
                actualExceptions.containsAll(expectedExceptions),
                expectedExceptions,
                actualExceptions,
                "Constructor or method does not declare the required exception(s)"
            );
        }));
    }

    // Classes

    /**
     * Verifies that the class with the given name exists.
     * <p>
     * Note that if the class exists and was not previously loaded, it will be loaded when invoking
     * {@link AssertionVerification#verify(ActualWrapper)} on this object.
     * This will cause the class to be linked and the class initializer to be executed.
     * During both operations, errors may occur which will cause this verification to be unsuccessful.
     * If a {@link LinkageError} or an {@link ExceptionInInitializerError} is thrown during loading,
     * the error gets caught and stored in the result as the cause for this failure.
     * <p>
     * Use with {@link Assertions#assertThat(AssertionVerification, AssertionOption[])}.
     *
     * @param className the name of the class
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, ?, Class<?>> classExists(String className) {
        return BehaviorBasedVerification.of(ignored -> {
            AssertionResult<?, Class<?>> result;
            try {
                Class<?> clazz = Class.forName(className);
                result = AssertionResult.of(true, null, clazz);
            } catch (ClassNotFoundException e) {
                result = AssertionResult.of(false, "Class %s does not exist".formatted(className));
                result.getErrorBuilder().setCause(e);
            } catch (ExceptionInInitializerError e) {
                result = AssertionResult.of(false, "Class %s exists, but an error occurred during class initialization".formatted(className));
                result.getErrorBuilder().setCause(e);
            } catch (LinkageError e) {
                result = AssertionResult.of(false, "Class %s exists, but it could not be linked".formatted(className));
                result.getErrorBuilder().setCause(e);
            }
            return result;
        });
    }

    /**
     * Verifies that the described class exists.
     * <p>
     * Note that if the class exists and was not previously loaded, it will be loaded when invoking
     * {@link AssertionVerification#verify(ActualWrapper)} on this object.
     * This will cause the class to be linked and the class initializer to be executed.
     * During both operations, errors may occur which will cause this verification to be unsuccessful.
     * If a {@link LinkageError} or an {@link ExceptionInInitializerError} is thrown during loading,
     * the error gets caught and stored in the result as the cause for this failure.
     * <p>
     * Use with {@link Assertions#assertThat(AssertionVerification, AssertionOption[])}.
     *
     * @param descriptor the descriptor for the class
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, ?, Class<?>> classExists(ClassDescriptor descriptor) {
        Optional<AssertionResult<Object, Class<?>>> checkResult = checkDescriptors(
            "Cannot verify if class exists because the expected class was not found",
            descriptor
        );
        return checkResult.isPresent() ? actual -> checkResult.get() : classExists(descriptor.getName());
    }

    /**
     * Verifies that the class under test is a regular class (i.e., not an interface, record or enum).
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Boolean, Class<?>, Boolean> isClass() {
        return ValueBasedVerification.of(actual -> {
            Class<?> clazz = actual.getClass();
            boolean isClass = !clazz.isInterface() && !clazz.isRecord() && !clazz.isEnum();
            return AssertionResult.of(isClass, true, isClass,
                "Class %s is not a regular class".formatted(clazz.getTypeName()));
        });
    }

    /**
     * Verifies that the class under test is an interface.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Boolean, Class<?>, Boolean> isInterface() {
        return ValueBasedVerification.of(actual -> {
            Class<?> clazz = actual.getClass();
            boolean isInterface = clazz.isInterface();
            return AssertionResult.of(isInterface, true, isInterface,
                "Class %s is not an interface".formatted(clazz.getTypeName()));
        });
    }

    /**
     * Verifies that the class under test is a record class.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Boolean, Class<?>, Boolean> isRecord() {
        return ValueBasedVerification.of(actual -> {
            Class<?> clazz = actual.getClass();
            boolean isRecord = clazz.isRecord();
            return AssertionResult.of(isRecord, true, isRecord,
                "Class %s is not a record".formatted(clazz.getTypeName()));
        });
    }

    /**
     * Verifies that the class under test is an enum class.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Boolean, Class<?>, Boolean> isEnum() {
        return ValueBasedVerification.of(actual -> {
            Class<?> clazz = actual.getClass();
            boolean isEnum = clazz.isEnum();
            return AssertionResult.of(isEnum, true, isEnum,
                "Class %s is not an enum".formatted(clazz.getTypeName()));
        });
    }

    /**
     * Verifies that the class under test has the given class as its superclass.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param superclass the expected superclass
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Class<?>, Class<?>, Class<?>> hasSuperclass(Class<?> superclass) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            if (clazz.getSuperclass() == null) {
                return AssertionResult.of(false, "This verification is not applicable to classes without a superclass");
            } else {
                return AssertionResult.of(
                    clazz.getSuperclass() == superclass,
                    superclass,
                    clazz.getSuperclass(),
                    "Class %s does not have %s as its superclass".formatted(clazz.getName(), superclass.getName())
                );
            }
        }));
    }

    /**
     * Verifies that the class under test has the given (generic) class as its superclass.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param typeDescriptor a descriptor for the expected superclass, may describe a generic type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<TypeDescriptor, Class<?>, TypeDescriptor> hasSuperclass(TypeDescriptor typeDescriptor) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Optional<AssertionResult<TypeDescriptor, TypeDescriptor>> checkResult = checkDescriptors(
                "Cannot verify actual superclass type because the expected superclass type was not found",
                typeDescriptor
            );
            if (checkResult.isPresent()) return checkResult.get();

            Class<?> clazz = actual.getValue();
            if (clazz.getSuperclass() == null) {
                return AssertionResult.of(false, "Verification is not applicable to classes without a superclass");
            } else {
                TypeDescriptor superclassType = Descriptors.forType(clazz.getGenericSuperclass());
                return AssertionResult.of(
                    Objects.equals(typeDescriptor, superclassType),
                    typeDescriptor,
                    superclassType,
                    "Class %s does not have %s as its superclass".formatted(clazz.getName(), typeDescriptor.getName())
                );
            }
        }));
    }

    /**
     * Verifies that the class under test implements the given interfaces.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param interfaces zero or more expected interfaces
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Set<Class<?>>, Class<?>, Set<Class<?>>> implementsInterface(Class<?>... interfaces) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            Set<Class<?>> expectedInterfaces = Set.of(interfaces);
            Set<Class<?>> actualInterfaces = Set.of(clazz.getInterfaces());
            return AssertionResult.of(
                actualInterfaces.containsAll(expectedInterfaces),
                expectedInterfaces,
                actualInterfaces,
                "Class %s does not implement the required interface(s)".formatted(clazz.getName())
            );
        }));
    }

    /**
     * Verifies that the class under test implements the given (generic) interfaces.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param interfaces zero or more descriptors for the expected interfaces, may describe generic types
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Set<TypeDescriptor>, Class<?>, Set<TypeDescriptor>> implementsInterface(TypeDescriptor... interfaces) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Optional<AssertionResult<Set<TypeDescriptor>, Set<TypeDescriptor>>> checkResult = checkDescriptors(
                "Cannot verify actual interface types because an expected interface type was not found",
                interfaces
            );
            if (checkResult.isPresent()) return checkResult.get();

            Class<?> clazz = actual.getValue();
            Set<TypeDescriptor> expectedInterfaces = Set.of(interfaces);
            Set<TypeDescriptor> actualInterfaces = Arrays.stream(clazz.getGenericInterfaces())
                .map(Descriptors::forType)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
            return AssertionResult.of(
                actualInterfaces.containsAll(expectedInterfaces),
                expectedInterfaces,
                actualInterfaces,
                "Class %s does not implement the required interface(s)".formatted(clazz.getName())
            );
        }));
    }

    /**
     * Verifies that the class under test has access to a field with the given name.
     * The pool of possible fields include any fields within the class itself (including private ones) and
     * those the class has access to via inheritance (superclasses and interfaces).
     * <p>
     * Note that in some cases a class may have access to two or more fields with an identical name but different owners.
     * This verification makes no guarantees which of these fields would be returned in the result.
     * To check if a class explicitly declares a field, see {@link #declaresField(String)}.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param fieldName name of the expected field
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Class<?>, Field> hasField(String fieldName) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            Optional<Field> field = getFieldsRecursively(clazz)
                .stream()
                // filter out private fields of superclasses
                .filter(f -> f.getDeclaringClass() == clazz || !Modifier.isPrivate(f.getModifiers()))
                // filter out package-private fields of superclasses
                .filter(f -> f.getDeclaringClass().getPackage() == clazz.getPackage() || (f.getModifiers() & 0b111) != 0)
                // look for field with matching name
                .filter(f -> f.getName().equals(fieldName))
                .findFirst();
            AssertionResult<?, Field> result = AssertionResult.of(
                field.isPresent(),
                null,
                field.orElse(null),
                "Field %s does not exist in class %s or any of its superclasses or interfaces".formatted(fieldName, clazz.getName())
            );
            result.getErrorBuilder()
                .setExpectedStringRepresentation("a field with name " + fieldName)
                .setActualStringRepresentation("no such field");
            return result;
        }));
    }

    /**
     * Verifies that the class under test declares a field with the given name.
     * To check if a class can access a field with the given name, see {@link #hasField(String)}.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param fieldName name of the expected field
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Class<?>, Field> declaresField(String fieldName) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            Optional<Field> field = Set.of(clazz.getDeclaredFields())
                .stream()
                .filter(f -> f.getName().equals(fieldName))
                .findFirst();
            AssertionResult<?, Field> result = AssertionResult.of(
                field.isPresent(),
                null,
                field.orElse(null),
                "Class %s does not declare field %s".formatted(clazz.getName(), fieldName)
            );
            result.getErrorBuilder()
                .setExpectedStringRepresentation("a field with name " + fieldName)
                .setActualStringRepresentation("no such field");
            return result;
        }));
    }

    /**
     * Verifies that the class under test declares a constructor with the given parameter types.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param parameterTypes the expected parameter types
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static <T> AssertionVerification<?, Class<T>, Constructor<T>> declaresConstructor(Class<?>... parameterTypes) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<T> clazz = actual.getValue();
            AssertionResult<?, Constructor<T>> result;
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
                result = AssertionResult.of(true, null, constructor);
            } catch (NoSuchMethodException e) {
                result = AssertionResult.of(false, null, null,
                    "Could not find constructor with matching parameter types");
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("a constructor with signature " + AssertionUtils.getMethodSignature(clazz.getName(), parameterTypes))
                    .setActualStringRepresentation("no such constructor");
            }
            return result;
        }));
    }

    /**
     * Verifies that the class under test has access to a method with the given name and parameter types.
     * The pool of possible methods include any methods within the class itself (including private ones) and
     * those the class has access to via inheritance (superclasses and interfaces).
     * To check if a class explicitly declares a method, see {@link #declaresMethod(String, Class[])}.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param methodName     name of the expected method
     * @param parameterTypes parameter types of the expected method
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Class<?>, Method> hasMethod(String methodName, Class<?>... parameterTypes) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            Optional<Method> method = getMethodsRecursively(clazz)
                .stream()
                // filter out private methods of superclasses
                .filter(m -> m.getDeclaringClass() == clazz || !Modifier.isPrivate(m.getModifiers()))
                // filter out package-private methods of superclasses
                .filter(m -> m.getDeclaringClass().getPackage() == clazz.getPackage() || (m.getModifiers() & 0b111) != 0)
                // look for method with matching name
                .filter(m -> m.getName().equals(methodName) && Arrays.equals(m.getParameterTypes(), parameterTypes))
                .findFirst();
            AssertionResult<?, Method> result = AssertionResult.of(
                method.isPresent(),
                null,
                method.orElse(null),
                "Method %s does not exist in class %s or any of its superclasses or interfaces".formatted(AssertionUtils.getMethodSignature(methodName, parameterTypes),
                    clazz.getName())
            );
            result.getErrorBuilder()
                .setExpectedStringRepresentation("a method with signature " + AssertionUtils.getMethodSignature(methodName, parameterTypes))
                .setActualStringRepresentation("no such method");
            return result;
        }));
    }

    /**
     * Verifies that the class under test declares a method with the given name and parameter types.
     * To check if a class can access a method with the given name, see {@link #hasMethod(String, Class[])}.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param methodName     name of the expected method
     * @param parameterTypes parameter types of the expected method
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Class<?>, Method> declaresMethod(String methodName, Class<?>... parameterTypes) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            Optional<Method> method = Set.of(clazz.getDeclaredMethods())
                .stream()
                .filter(m -> m.getName().equals(methodName) && Arrays.equals(m.getParameterTypes(), parameterTypes))
                .findFirst();
            AssertionResult<?, Method> result = AssertionResult.of(
                method.isPresent(),
                null,
                method.orElse(null),
                "Class %s does not declare method %s".formatted(clazz.getName(), AssertionUtils.getMethodSignature(methodName, parameterTypes))
            );
            result.getErrorBuilder()
                .setExpectedStringRepresentation("a method with signature " + AssertionUtils.getMethodSignature(methodName, parameterTypes))
                .setActualStringRepresentation("no such method");
            return result;
        }));
    }

    // Fields

    /**
     * Verifies that the given descriptor can be resolved to an actual field.
     * <p>
     * Use with {@link Assertions#assertThat(AssertionVerification, AssertionOption[])}.
     *
     * @param fieldDescriptor descriptor for the expected field
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, ?, Field> fieldExists(FieldDescriptor fieldDescriptor) {
        return BehaviorBasedVerification.of(ignored -> {
            Optional<AssertionResult<Object, Field>> checkResult = checkDescriptors(
                "Cannot verify if field exists because an expected field was not found",
                fieldDescriptor
            );
            if (checkResult.isPresent()) return checkResult.get();

            Pair<Optional<Class<?>>, String> result = resolveClass(fieldDescriptor.getDeclaringType());
            if (result.getFirst().isEmpty())
                return AssertionResult.of(false, result.getSecond());
            Class<?> declaringClass = result.getFirst().get();

            for (Field field : declaringClass.getDeclaredFields()) {
                if (field.getName().equals(fieldDescriptor.getName())) {
                    return AssertionResult.of(true, null, field);
                }
            }
            return AssertionResult.of(false, "Could not find field %s in class %s"
                .formatted(fieldDescriptor.getName(), fieldDescriptor.getDeclaringType().getName()));
        });
    }

    /**
     * Verifies that the field under test was declared with the expected type.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param type the expected type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Class<?>, Field, Class<?>> hasType(Class<?> type) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Field field = actual.getValue();
            return AssertionResult.of(field.getType().equals(type), type, field.getType(),
                "Field %s#%s does not have the expected type".formatted(field.getDeclaringClass().getName(), field.getName()));
        }));
    }

    /**
     * Verifies that the field under test was declared with the expected type, including any bounds if generic.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param typeDescriptor descriptor for the expected type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<TypeDescriptor, Field, TypeDescriptor> hasType(TypeDescriptor typeDescriptor) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Optional<AssertionResult<TypeDescriptor, TypeDescriptor>> checkResult = checkDescriptors(
                "Cannot verify actual field type because the expected field type was not found",
                typeDescriptor
            );
            if (checkResult.isPresent()) return checkResult.get();

            Field field = actual.getValue();
            TypeDescriptor actualTypeDescriptor = Descriptors.forType(field.getGenericType());
            return AssertionResult.of(Objects.equals(typeDescriptor, actualTypeDescriptor),
                typeDescriptor,
                actualTypeDescriptor,
                "Field %s#%s does not have the expected type".formatted(field.getDeclaringClass().getName(), field.getName()));
        }));
    }

    /**
     * Verifies that the class under test is an enum class and has a constant with the given name.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param name the name of the expected constant
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static <R extends Enum<?>> AssertionVerification<?, Class<?>, R> hasEnumConstant(String name) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            Object[] constants = clazz.getEnumConstants();
            AssertionResult<?, R> result;

            if (constants == null) {
                result = AssertionResult.of(false, "Class %s is not an enum class".formatted(clazz.getName()));
            } else {
                @SuppressWarnings("unchecked") Optional<R> constant = Arrays.stream(constants)
                    .map(c -> (R) c)
                    .filter(e -> e.name().equals(name))
                    .findFirst();
                result = AssertionResult.of(constant.isPresent(), null, constant.orElse(null),
                    "Class %s does not have the required enum constant".formatted(clazz.getName()));
                result.getErrorBuilder()
                    .setExpectedStringRepresentation("an enum constant with name '%s'".formatted(name))
                    .setActualStringRepresentation("no such constant");
            }

            return result;
        }));
    }

    // Constructors

    /**
     * Verifies that the given descriptor can be resolved to an actual constructor.
     * <p>
     * Use with {@link Assertions#assertThat(AssertionVerification, AssertionOption[])}.
     *
     * @param constructorDescriptor the descriptor for the expected constructor
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, ?, Constructor<?>> constructorExists(ConstructorDescriptor constructorDescriptor) {
        return BehaviorBasedVerification.of(ignored -> {
            Optional<AssertionResult<Object, Constructor<?>>> checkResult = checkDescriptors(
                "Cannot verify if constructor exists because the expected constructor was not found",
                constructorDescriptor
            );
            if (checkResult.isPresent()) return checkResult.get();

            Pair<Optional<Class<?>>, String> resolvedClass = resolveClass(constructorDescriptor.getDeclaringType());
            if (resolvedClass.getFirst().isEmpty())
                return AssertionResult.of(false, resolvedClass.getSecond());
            Class<?> declaringClass = resolvedClass.getFirst().get();
            ClassDescriptor[] parameterTypeDescriptors = constructorDescriptor.getParameterTypes().toArray(ClassDescriptor[]::new);
            checkResult = checkDescriptors(
                "Cannot verify actual parameter types because an expected parameter type was not found",
                parameterTypeDescriptors
            );
            if (checkResult.isPresent()) return checkResult.get();

            Constructor<?> constructor = Arrays.stream(declaringClass.getDeclaredConstructors())
                .filter(c -> c.getParameterTypes().length == parameterTypeDescriptors.length)
                .filter(c -> {
                    Class<?>[] parameterTypes = c.getParameterTypes();
                    for (int i = 0; i < parameterTypeDescriptors.length; i++) {
                        if (!parameterTypeDescriptors[i].getName().equals(parameterTypes[i].getTypeName())) return false;
                    }
                    return true;
                })
                .findAny()
                .orElse(null);
            AssertionResult<Object, Constructor<?>> result = AssertionResult.of(constructor != null, null, constructor,
                "Could not find constructor with matching parameter types");
            result.getErrorBuilder()
                .setExpectedStringRepresentation("a constructor with signature " + AssertionUtils.getMethodSignature(declaringClass.getName(), parameterTypeDescriptors))
                .setActualStringRepresentation("no such constructor");
            return result;
        });
    }

    // Methods

    /**
     * Verifies that the given descriptor can be resolved to an actual method.
     * <p>
     * Use with {@link Assertions#assertThat(AssertionVerification, AssertionOption[])}.
     *
     * @param methodDescriptor descriptor for the expected method
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, ?, Method> methodExists(MethodDescriptor methodDescriptor) {
        return BehaviorBasedVerification.of(ignored -> {
            Optional<AssertionResult<Object, Method>> checkResult = checkDescriptors(
                "Cannot verify if method exists because the expected method was not found",
                methodDescriptor
            );
            if (checkResult.isPresent()) return checkResult.get();

            Pair<Optional<Class<?>>, String> resolvedClass = resolveClass(methodDescriptor.getDeclaringType());
            if (resolvedClass.getFirst().isEmpty())
                return AssertionResult.of(false, resolvedClass.getSecond());
            Class<?> declaringClass = resolvedClass.getFirst().get();
            ClassDescriptor[] parameterTypeDescriptors = methodDescriptor.getParameterTypes().toArray(ClassDescriptor[]::new);
            checkResult = checkDescriptors(
                "Cannot verify actual parameter types because an expected parameter type was not found",
                parameterTypeDescriptors
            );
            if (checkResult.isPresent()) return checkResult.get();

            Method method = Arrays.stream(declaringClass.getDeclaredMethods())
                .filter(m -> m.getParameterTypes().length == parameterTypeDescriptors.length)
                .filter(m -> {
                    Class<?>[] parameterTypes = m.getParameterTypes();
                    for (int i = 0; i < parameterTypeDescriptors.length; i++) {
                        if (!parameterTypeDescriptors[i].getName().equals(parameterTypes[i].getTypeName())) return false;
                    }
                    return true;
                })
                .findAny()
                .orElse(null);
            AssertionResult<Object, Method> result = AssertionResult.of(method != null, null, method,
                "Could not find method with matching parameter types");
            result.getErrorBuilder()
                .setExpectedStringRepresentation("a method with signature " + AssertionUtils.getMethodSignature(declaringClass.getName(), parameterTypeDescriptors))
                .setActualStringRepresentation("no such method");
            return result;
        });
    }

    /**
     * Verifies that the method under test was declared with the expected return type.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param type the expected return type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Class<?>, Method, Class<?>> hasReturnType(Class<?> type) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Method method = actual.getValue();
            return AssertionResult.of(method.getReturnType().equals(type), type, method.getReturnType(),
                "Method %s#%s does not have the expected return type".formatted(method.getDeclaringClass().getName(),
                    AssertionUtils.getMethodSignature(method.getName(), method.getParameterTypes())));
        }));
    }

    /**
     * Verifies that the method under test was declared with the expected (generic) return type.
     * <p>
     * Usable with these assertions:
     * <ul>
     *     <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *     <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     * </ul>
     *
     * @param typeDescriptor descriptor for the expected return type, may describe a generic type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<TypeDescriptor, Method, TypeDescriptor> hasReturnType(TypeDescriptor typeDescriptor) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Optional<AssertionResult<TypeDescriptor, TypeDescriptor>> checkResult = checkDescriptors(
                "Cannot verify actual return type because the expected return type was not found",
                typeDescriptor
            );
            if (checkResult.isPresent()) return checkResult.get();

            Method method = actual.getValue();
            TypeDescriptor returnType = Descriptors.forType(method.getGenericReturnType());
            return AssertionResult.of(Objects.equals(typeDescriptor, returnType), typeDescriptor, returnType,
                "Method %s#%s does not have the expected return type".formatted(method.getDeclaringClass().getName(),
                    AssertionUtils.getMethodSignature(method.getName(), method.getParameterTypes())));
        }));
    }

    private static Pair<Optional<Class<?>>, String> resolveClass(ClassDescriptor classDescriptor) {
        AssertionResult<?, Class<?>> classExistsResult = classExists(classDescriptor).verify(ActualWrapper.ofObject(null));
        if (classExistsResult.successful()) {
            return new Pair<>(classExistsResult.getActual(), null);
        } else {
            return new Pair<>(Optional.empty(), classExistsResult.getErrorBuilder().getMessage());
        }
    }

    private static <E, R> Optional<AssertionResult<E, R>> checkDescriptors(String message, Descriptor... descriptors) {
        Optional<AssertionResult<E, R>> result = Optional.empty();
        for (Descriptor descriptor : descriptors) {
            if (descriptor instanceof NotFound) {
                result = Optional.of(AssertionResult.of(false, message));
                break;
            }
        }
        return result;
    }

    private static Set<Field> getFieldsRecursively(Class<?> clazz) {
        if (clazz == null) {
            return Collections.emptySet();
        } else {
            // TODO: Add caching
            Set<Field> fields = Stream.concat(Arrays.stream(clazz.getFields()), Arrays.stream(clazz.getDeclaredFields()))
                .collect(Collectors.toSet());
            fields.addAll(getFieldsRecursively(clazz.getSuperclass()));
            for (Class<?> iface : clazz.getInterfaces()) {
                fields.addAll(getFieldsRecursively(iface));
            }
            return Collections.unmodifiableSet(fields);
        }
    }

    private static Set<Method> getMethodsRecursively(Class<?> clazz) {
        if (clazz.getSuperclass() == null) {
            return Collections.emptySet();
        } else {
            // TODO: Add caching
            Set<Method> methods = Stream.concat(Arrays.stream(clazz.getMethods()), Arrays.stream(clazz.getDeclaredMethods()))
                .collect(Collectors.toSet());
            methods.addAll(getMethodsRecursively(clazz.getSuperclass()));
            return Collections.unmodifiableSet(methods);
        }
    }
}
