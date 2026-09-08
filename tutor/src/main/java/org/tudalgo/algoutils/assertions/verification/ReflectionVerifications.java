package org.tudalgo.algoutils.assertions.verification;

import org.tudalgo.algoutils.assertions.*;
import org.tudalgo.algoutils.assertions.options.AssertionOption;
import org.tudalgo.algoutils.descriptors.Descriptors;
import org.tudalgo.algoutils.descriptors.WithModifiers;
import org.tudalgo.algoutils.descriptors.members.FieldDescriptor;
import org.tudalgo.algoutils.descriptors.types.ParameterizedTypeDescriptor;
import org.tudalgo.algoutils.descriptors.types.TypeDescriptor;
import org.tudalgo.algoutils.descriptors.types.TypeVariableDescriptor;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.tudalgo.algoutils.assertions.verification.ValueBasedVerification.wrap;

// TODO: verifications for type parameters

/**
 * Provides verifications on reflection objects, such as classes and members, and their modifiers.
 * These verifications can be used with the {@link org.tudalgo.algoutils.assertions.Assertions} class to assert properties of classes and members in tests.
 */
public final class ReflectionVerifications {

    // Do not instantiate
    private ReflectionVerifications() {}

    // Common for classes and members

    /**
     * Verifies that the actual value has the given modifiers.
     * The actual value must be either a class, a class member (i.e., a field, constructor or method) or
     * a descriptor for either (see {@link org.tudalgo.algoutils.descriptors {@code descriptors} package}).
     * This verification only checks if the actual value has the given modifiers set, it may have additional
     * modifiers that are not covered by the ones that were passed to this method.
     * See {@link #hasExactModifiers(int)} for matching modifiers exactly.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
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
     * Verifies that the actual value has exactly the given modifiers.
     * The actual value must be either a class, a class member (i.e., a field, constructor or method) or
     * a descriptor for either (see {@link org.tudalgo.algoutils.descriptors {@code descriptors} package}).
     * See {@link #hasModifiers(int)} for checking if a class or class member has at least the specified modifiers set.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param modifiers the modifiers to check for
     * @return an {@link AssertionVerification} object to verify the assertion
     * @throws IllegalArgumentException if the actual value is not a class or a class member
     * @see Modifier
     */
    public static AssertionVerification<Integer, ?, Integer> hasExactModifiers(int modifiers) {
        return hasModifiers(modifiers, true);
    }

    /**
     * Verifies that the actual value declares the given type parameters.
     * The actual value must implement the {@link GenericDeclaration} interface, so it must be either a class or an executable
     * (i.e., a constructor or method).
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
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
            List<TypeVariableDescriptor> actualTypeVariables;
            String prefix;
            switch (genericDeclaration) {
                case Class<?> clazz -> {
                    actualTypeVariables = List.of(Descriptors.forClass(clazz).getTypeParameters());
                    prefix = "Class %s".formatted(clazz.getName());
                }
                case Constructor<?> constructor -> {
                    actualTypeVariables = List.of(Descriptors.forConstructor(constructor).getTypeParameters());
                    prefix = "Constructor %s".formatted(AssertionUtils.getMethodSignature(constructor));
                }
                case Method method -> {
                    actualTypeVariables = List.of(Descriptors.forMethod(method).getTypeParameters());
                    prefix = "Method %s".formatted(AssertionUtils.getMethodSignature(method));
                }
                default -> throw new IllegalArgumentException("Unsupported actual type");
            }

            return AssertionResult.of(expectedTypeVariables.equals(actualTypeVariables), expectedTypeVariables, actualTypeVariables,
                "%s does not declare the correct type parameters".formatted(prefix));
        }));
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
                case WithModifiers descriptor -> {
                    actualModifiers = descriptor.getModifiers();
                    prefix = "Descriptor " + descriptor;
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

    // Classes

    /**
     * Verifies that the class with the given name exists.
     *
     * <p>
     *     Note that if the class exists and was not previously loaded, it will be loaded when invoking
     *     {@link AssertionVerification#verify(ActualWrapper)} on this object.
     *     This will cause the class to be linked and the class initializer to be executed.
     *     During both operations, errors may occur which will cause this verification to be unsuccessful.
     *     If a {@link LinkageError} or an {@link ExceptionInInitializerError} is thrown during loading,
     *     the error gets caught and stored in the result as the cause for this failure.
     * </p>
     *
     * <p>Use with {@link Assertions#assertThat(AssertionVerification, AssertionOption[])}.</p>
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
     * Verifies that the class for the given descriptor exists.
     *
     * <p>
     *     Note that if the class exists and was not previously loaded, it will be loaded when invoking
     *     {@link AssertionVerification#verify(ActualWrapper)} on this object.
     *     This will cause the class to be linked and the class initializer to be executed.
     *     During both operations, errors may occur which will cause this verification to be unsuccessful.
     *     If a {@link LinkageError} or an {@link ExceptionInInitializerError} is thrown during loading,
     *     the error gets caught and stored in the result as the cause for this failure.
     * </p>
     *
     * <p>Use with {@link Assertions#assertThat(AssertionVerification, AssertionOption[])}.</p>
     *
     * @param descriptor the descriptor for the class
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, ?, Class<?>> classExists(TypeDescriptor descriptor) {
        return classExists(descriptor.getName());
    }

    /**
     * Verifies that the class under test has the given class as its superclass.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
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
     * Verifies that the class under test has the given class as its superclass.
     * If {@code descriptor} is generic, the verification will check against the generic superclass.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param descriptor the descriptor for the expected superclass, may describe a generic type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<TypeDescriptor, Class<?>, Type> hasSuperclass(TypeDescriptor descriptor) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            if (clazz.getSuperclass() == null) {
                return AssertionResult.of(false, "This verification is not applicable to classes without a superclass");
            } else if (descriptor instanceof ParameterizedTypeDescriptor ptDescriptor) {
                return AssertionResult.of(
                    clazz.getGenericSuperclass().getTypeName().equals(ptDescriptor.getName()),
                    ptDescriptor,
                    clazz.getGenericSuperclass(),
                    "Class %s does not have %s as its superclass".formatted(clazz.getName(), ptDescriptor.getName())
                );
            } else {
                return AssertionResult.of(
                    clazz.getSuperclass().getName().equals(descriptor.getName()),
                    descriptor,
                    clazz.getSuperclass(),
                    "Class %s does not have %s as its superclass".formatted(clazz.getName(), descriptor.getName())
                );
            }
        }));
    }

    /**
     * Verifies that the class under test implements the given interfaces.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
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
     * Verifies that the class under test implements the given interfaces.
     * If an element of {@code interface} is generic, the verification will check against the generic interface for that element.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param interfaces zero or more expected type descriptors, may describe a generic type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<Set<TypeDescriptor>, Class<?>, Set<Type>> implementsInterface(TypeDescriptor... interfaces) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            Set<TypeDescriptor> expectedInterfaces = Set.of(interfaces);
            Set<Type> actualInterfaces = Set.of(clazz.getInterfaces());
            Set<Type> actualGenericInterfaces = Set.of(clazz.getGenericInterfaces());
            return AssertionResult.of(
                expectedInterfaces.stream()
                    .allMatch(d -> (d instanceof ParameterizedTypeDescriptor ? actualGenericInterfaces : actualInterfaces).stream()
                        .anyMatch(c -> c.getTypeName().equals(d.getName()))),
                expectedInterfaces,
                actualInterfaces,
                "Class %s does not implement the required interface(s)".formatted(clazz.getName())
            );
        }));
    }

    // Fields

    /**
     * Verifies that the class under test has access to a field with the given name.
     * The pool of possible fields include any fields within the class itself (including private ones) and
     * those the class has access to via inheritance (superclasses and interfaces).
     *
     * <p>
     *     Note that in some cases a class may have access to two or more fields with an identical name but different owners.
     *     This verification makes no guarantees which of these fields would be returned in the result.
     *     To check if a class explicitly declares a field, see {@link #declaresField}.
     * </p>
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
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
     * Verifies that the class under test has access to a field with the given name.
     * The pool of possible fields include any fields within the class itself (including private ones) and
     * those the class has access to via inheritance (superclasses and interfaces).
     *
     * <p>
     *     Note that in some cases a class may have access to two or more fields with an identical name but different owners.
     *     This verification makes no guarantees which of these fields would be returned in the result.
     *     To check if a class explicitly declares a field, see {@link #declaresField}.
     * </p>
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param fieldDescriptor descriptor for the expected field
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Class<?>, Field> hasField(FieldDescriptor fieldDescriptor) {
        return hasField(fieldDescriptor.getName());
    }

    /**
     * Verifies that the class under test declares a field with the given name.
     * To check if a class can access a field with the given name, see {@link #hasField}.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
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
     * Verifies that the class under test declares a field with the given name.
     * To check if a class can access a field with the given name, see {@link #hasField}.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param fieldDescriptor descriptor for the expected field
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Class<?>, Field> declaresField(FieldDescriptor fieldDescriptor) {
        return declaresField(fieldDescriptor.getName());
    }

    /**
     * Verifies that the field under test was declared with the expected type.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
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
     * Verifies that the field under test was declared with the expected type.
     * If {@code typeDescriptor} is generic, the verification will check against the generic type.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param typeDescriptor descriptor for the expected type, may describe a generic type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<TypeDescriptor, Field, Type> hasType(TypeDescriptor typeDescriptor) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Field field = actual.getValue();
            Type fieldType = typeDescriptor.isGeneric() ? field.getGenericType() : field.getType();
            return AssertionResult.of(fieldType.getTypeName().equals(typeDescriptor.getName()),
                typeDescriptor,
                fieldType,
                "Field %s#%s does not have the expected type".formatted(field.getDeclaringClass().getName(), field.getName()));
        }));
    }

    /**
     * Verifies that the class under test is an enum class and has a constant with the given name.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
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

    private static Set<Field> getFieldsRecursively(Class<?> clazz) {
        if (clazz.getSuperclass() == null) {
            return Collections.emptySet();
        } else {
            // TODO: Add caching
            Set<Field> fields = Stream.concat(Arrays.stream(clazz.getFields()), Arrays.stream(clazz.getDeclaredFields()))
                .collect(Collectors.toSet());
            fields.addAll(getFieldsRecursively(clazz.getSuperclass()));
            return Collections.unmodifiableSet(fields);
        }
    }

    // Constructors

    /**
     * Verifies that the class under test declares a constructor with the given parameter types.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param parameterTypes the expected parameter types
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static <A> AssertionVerification<?, Class<A>, Constructor<A>> declaresConstructor(Class<?>... parameterTypes) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<A> clazz = actual.getValue();
            AssertionResult<?, Constructor<A>> result;
            try {
                Constructor<A> constructor = clazz.getDeclaredConstructor(parameterTypes);
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
     * Verifies that the class under test declares a constructor with the given parameter types.
     * If an element of {@code typeDescriptors} is generic, the verification will check against the generic parameter type for that element.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param typeDescriptors descriptors for the expected parameter types, may describe a generic type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static <A> AssertionVerification<?, Class<A>, Constructor<A>> declaresConstructor(TypeDescriptor... typeDescriptors) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<A> clazz = actual.getValue();
            @SuppressWarnings("unchecked") Constructor<A> constructor = (Constructor<A>) Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> {
                    Type[] parameterTypes = c.getParameterTypes();
                    Type[] genericParameterTypes = c.getGenericParameterTypes();
                    if (parameterTypes.length != typeDescriptors.length) {
                        return false;
                    }
                    for (int i = 0; i < typeDescriptors.length; i++) {
                        if (!typeDescriptors[i].getName().equals((typeDescriptors[i].isGeneric() ? genericParameterTypes : parameterTypes)[i].getTypeName())) {
                            return false;
                        }
                    }
                    return true;
                })
                .findAny()
                .orElse(null);
            AssertionResult<?, Constructor<A>> result = AssertionResult.of(constructor != null, null, constructor,
                "Could not find constructor with matching parameter types");
            result.getErrorBuilder()
                .setExpectedStringRepresentation("a constructor with signature " + AssertionUtils.getMethodSignature(clazz.getName(), typeDescriptors))
                .setActualStringRepresentation("no such constructor");
            return result;
        }));
    }

    // Methods

    /**
     * Verifies that the class under test has access to a method with the given name and parameter types.
     * The pool of possible methods include any methods within the class itself (including private ones) and
     * those the class has access to via inheritance (superclasses and interfaces).
     * To check if a class explicitly declares a method, see {@link #declaresMethod}.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param methodName     name of the expected method
     * @param parameterTypes the expected parameter types
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
     * Verifies that the class under test has access to a method with the given name and parameter types.
     * The pool of possible methods include any methods within the class itself (including private ones) and
     * those the class has access to via inheritance (superclasses and interfaces).
     * If an element of {@code typeDescriptors} is generic, the verification will check against the generic parameter type for that element.
     * To check if a class explicitly declares a method, see {@link #declaresMethod}.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param methodName      name of the expected method
     * @param typeDescriptors descriptors for the expected parameter types, may describe a generic type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Class<?>, Method> hasMethod(String methodName, TypeDescriptor... typeDescriptors) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            Optional<Method> method = getMethodsRecursively(clazz)
                .stream()
                // filter out private methods of superclasses
                .filter(m -> m.getDeclaringClass() == clazz || !Modifier.isPrivate(m.getModifiers()))
                // filter out package-private methods of superclasses
                .filter(m -> m.getDeclaringClass().getPackage() == clazz.getPackage() || (m.getModifiers() & 0b111) != 0)
                // look for method with matching name
                .filter(m -> {
                    Type[] parameterTypes = m.getParameterTypes();
                    Type[] genericParameterTypes = m.getGenericParameterTypes();
                    if (!m.getName().equals(methodName) || parameterTypes.length != typeDescriptors.length) {
                        return false;
                    }
                    for (int i = 0; i < typeDescriptors.length; i++) {
                        if (!typeDescriptors[i].getName().equals((typeDescriptors[i].isGeneric() ? genericParameterTypes : parameterTypes)[i].getTypeName())) {
                            return false;
                        }
                    }
                    return true;
                })
                .findFirst();
            AssertionResult<?, Method> result = AssertionResult.of(
                method.isPresent(),
                null,
                method.orElse(null),
                "Method %s does not exist in class %s or any of its superclasses or interfaces".formatted(AssertionUtils.getMethodSignature(methodName, typeDescriptors),
                    clazz.getName())
            );
            result.getErrorBuilder()
                .setExpectedStringRepresentation("a method with signature " + AssertionUtils.getMethodSignature(methodName, typeDescriptors))
                .setActualStringRepresentation("no such method");
            return result;
        }));
    }

    /**
     * Verifies that the class under test declares a method with the given name and parameter types.
     * To check if a class can access a method with the given name, see {@link #hasMethod}.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param methodName     name of the expected method
     * @param parameterTypes the expected parameter types
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

    /**
     * Verifies that the class under test declares a method with the given name and parameter types.
     * If an element of {@code typeDescriptors} is generic, the verification will check against the generic parameter type for that element.
     * To check if a class can access a method with the given name, see {@link #hasMethod}.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param methodName      name of the expected method
     * @param typeDescriptors descriptors for the expected parameter types, may describe a generic type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<?, Class<?>, Method> declaresMethod(String methodName, TypeDescriptor... typeDescriptors) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Class<?> clazz = actual.getValue();
            Optional<Method> method = Set.of(clazz.getDeclaredMethods())
                .stream()
                .filter(m -> {
                    Type[] parameterTypes = m.getParameterTypes();
                    Type[] genericParameterTypes = m.getGenericParameterTypes();
                    if (!m.getName().equals(methodName) || parameterTypes.length != typeDescriptors.length) {
                        return false;
                    }
                    for (int i = 0; i < typeDescriptors.length; i++) {
                        if (!typeDescriptors[i].getName().equals((typeDescriptors[i].isGeneric() ? genericParameterTypes : parameterTypes)[i].getTypeName())) {
                            return false;
                        }
                    }
                    return true;
                })
                .findFirst();
            AssertionResult<?, Method> result = AssertionResult.of(
                method.isPresent(),
                null,
                method.orElse(null),
                "Class %s does not declare method %s".formatted(clazz.getName(), AssertionUtils.getMethodSignature(methodName, typeDescriptors))
            );
            result.getErrorBuilder()
                .setExpectedStringRepresentation("a method with signature " + AssertionUtils.getMethodSignature(methodName, typeDescriptors))
                .setActualStringRepresentation("no such method");
            return result;
        }));
    }

    /**
     * Verifies that the method under test was declared with the expected return type.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
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
     * Verifies that the method under test was declared with the expected return type.
     * If {@code typeDescriptor} is generic, the verification will check against the generic return type.
     *
     * <p>
     *     Usable with these assertions:
     *     <ul>
     *         <li>{@link Assertions#assertThat(Object, AssertionVerification, AssertionOption[])}</li>
     *         <li>{@link Assertions#assertThatExpression(ActualWrapper.Expression, AssertionVerification, AssertionOption[])}</li>
     *     </ul>
     * </p>
     *
     * @param typeDescriptor descriptor for the expected return type, may describe a generic type
     * @return an {@link AssertionVerification} object to verify the assertion
     */
    public static AssertionVerification<TypeDescriptor, Method, Type> hasReturnType(TypeDescriptor typeDescriptor) {
        return ValueBasedVerification.of(actual -> wrap(() -> {
            Method method = actual.getValue();
            Type returnType = typeDescriptor.isGeneric() ? method.getGenericReturnType() : method.getReturnType();
            return AssertionResult.of(returnType.getTypeName().equals(typeDescriptor.getName()), typeDescriptor, returnType,
                "Method %s#%s does not have the expected return type".formatted(method.getDeclaringClass().getName(),
                    AssertionUtils.getMethodSignature(method.getName(), method.getParameterTypes())));
        }));
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
