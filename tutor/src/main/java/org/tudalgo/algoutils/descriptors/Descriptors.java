package org.tudalgo.algoutils.descriptors;

import com.google.common.base.Suppliers;
import com.google.common.reflect.ClassPath;
import org.tudalgo.algoutils.AlgoUtils;
import org.tudalgo.algoutils.descriptors.members.*;
import org.tudalgo.algoutils.descriptors.types.*;
import org.tudalgo.algoutils.tutor.general.match.MatchingUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Utility factory for creating descriptors for Java types and members.
 * <p>
 * This class converts between Java reflection objects such as {@link java.lang.reflect.Type Type},
 * {@link java.lang.reflect.Field Field}, {@link java.lang.reflect.Constructor Constructor} and
 * {@link java.lang.reflect.Method Method} and the descriptor abstractions used throughout this package.
 * <p>
 * The methods in this class are divided into four groups:
 * <ol>
 *     <li><b>for*</b> methods - create descriptor instances from Java reflection objects</li>
 *     <li><b>find*</b> methods - attempt to find a match for the given values and return a descriptor for the closest match</li>
 *     <li><b>describe*</b> methods - create descriptors from as little information as necessary</li>
 *     <li><b>*Builder</b> methods - returns builders for more complex descriptor types (i.e., classes and members)</li>
 * </ol>
 */
public final class Descriptors {

    private static final Map<String, ClassDescriptor> CLASS_CACHE = new HashMap<>();
    private static final Map<String, FieldDescriptor> FIELD_CACHE = new HashMap<>();
    private static final Map<String, ConstructorDescriptor> CONSTRUCTOR_CACHE = new HashMap<>();
    private static final Map<String, MethodDescriptor> METHOD_CACHE = new HashMap<>();

    // Do not instantiate
    private Descriptors() {}

    // Packages

    /**
     * Creates a descriptor for the given package.
     *
     * @param pkg the package to describe
     * @return the descriptor for the given package
     */
    public static PackageDescriptor forPackage(Package pkg) {
        return PackageDescriptor.of(pkg.getName());
    }

    // Types

    /**
     * Creates a descriptor for the given Java reflection type.
     *
     * @param type the Java reflection type to describe
     * @return the corresponding descriptor, or {@code null} if the input is {@code null}
     */
    public static TypeDescriptor forType(Type type) {
        return switch (type) {
            case null -> null;
            case Class<?> clazz -> forClass(clazz);
            case GenericArrayType genericArrayType -> forGenericArrayType(genericArrayType);
            case ParameterizedType parameterizedType -> forParameterizedType(parameterizedType);
            case TypeVariable<?> typeVariable -> forTypeVariable(typeVariable);
            case WildcardType wildcardType -> forWildcardType(wildcardType);
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    /**
     * Creates a descriptor for a class type.
     *
     * @param clazz the Java class to describe
     * @return the descriptor for the given class
     */
    public static ClassDescriptor forClass(Class<?> clazz) {
        return CLASS_CACHE.computeIfAbsent(clazz.getTypeName(), ignored -> ClassDescriptor.of(
            clazz.getModifiers(),
            clazz.getName(),
            Suppliers.memoize(() -> Arrays.stream(clazz.getTypeParameters()).map(Descriptors::forTypeVariable).toArray(TypeVariableDescriptor[]::new)),
            Suppliers.memoize(() -> forType(clazz.getGenericSuperclass())),
            Suppliers.memoize(() -> Arrays.stream(clazz.getGenericInterfaces()).map(Descriptors::forType).toArray(TypeDescriptor[]::new))
        ));
    }

    /**
     * Creates a descriptor for a generic array type.
     *
     * @param genericArrayType the Java reflection generic array type to describe
     * @return the descriptor for the generic array type
     */
    public static GenericArrayTypeDescriptor forGenericArrayType(GenericArrayType genericArrayType) {
        return GenericArrayTypeDescriptor.of(Suppliers.memoize(() -> forType(genericArrayType.getGenericComponentType())));
    }

    /**
     * Creates a descriptor for a parameterized type.
     *
     * @param parameterizedType the Java reflection parameterized type to describe
     * @return the descriptor for the parameterized type
     */
    public static ParameterizedTypeDescriptor forParameterizedType(ParameterizedType parameterizedType) {
        return ParameterizedTypeDescriptor.of(
            Suppliers.memoize(() -> forType(parameterizedType.getRawType())),
            Suppliers.memoize(() -> Arrays.stream(parameterizedType.getActualTypeArguments()).map(Descriptors::forType).toArray(TypeDescriptor[]::new))
        );
    }

    /**
     * Creates a descriptor for a type variable.
     *
     * @param typeVariable the Java reflection type variable to describe
     * @return the descriptor for the type variable
     */
    public static TypeVariableDescriptor forTypeVariable(TypeVariable<?> typeVariable) {
        return TypeVariableDescriptor.of(
            typeVariable.getName(),
            Suppliers.memoize(() -> Arrays.stream(typeVariable.getBounds()).map(Descriptors::forType).toArray(TypeDescriptor[]::new))
        );
    }

    /**
     * Creates a descriptor for a wildcard type.
     *
     * @param wildcardType the Java reflection wildcard type to describe
     * @return the descriptor for the wildcard type
     */
    public static WildcardTypeDescriptor forWildcardType(WildcardType wildcardType) {
        WildcardTypeDescriptor.BoundsType boundsType = wildcardType.getTypeName().equals("?") ?
            WildcardTypeDescriptor.BoundsType.NONE :
            wildcardType.getLowerBounds().length > 0 ?
                WildcardTypeDescriptor.BoundsType.LOWER :
                WildcardTypeDescriptor.BoundsType.UPPER;
        return WildcardTypeDescriptor.of(
            boundsType,
            Suppliers.memoize(() -> switch (boundsType) {
                case NONE -> new TypeDescriptor[0];
                case LOWER -> Arrays.stream(wildcardType.getLowerBounds()).map(Descriptors::forType).toArray(TypeDescriptor[]::new);
                case UPPER -> Arrays.stream(wildcardType.getUpperBounds()).map(Descriptors::forType).toArray(TypeDescriptor[]::new);
            })
        );
    }

    // Class members

    /**
     * Creates a descriptor for a field.
     *
     * @param field the Java reflection field to describe
     * @return the descriptor for the field
     */
    public static FieldDescriptor forField(Field field) {
        return FIELD_CACHE.computeIfAbsent(Stringify.getSignature(field), ignored -> FieldDescriptor.of(
            Suppliers.memoize(() -> forClass(field.getDeclaringClass())),
            field.getModifiers(),
            Suppliers.memoize(() -> forType(field.getGenericType())),
            field.getName()
        ));
    }

    /**
     * Creates a descriptor for a constructor.
     *
     * @param constructor the Java reflection constructor to describe
     * @return the descriptor for the constructor
     */
    public static ConstructorDescriptor forConstructor(Constructor<?> constructor) {
        return CONSTRUCTOR_CACHE.computeIfAbsent(Stringify.getSignature(constructor), ignored -> ConstructorDescriptor.of(
            Suppliers.memoize(() -> forClass(constructor.getDeclaringClass())),
            constructor.getModifiers(),
            Suppliers.memoize(() -> Arrays.stream(constructor.getTypeParameters()).map(Descriptors::forTypeVariable).toArray(TypeVariableDescriptor[]::new)),
            Suppliers.memoize(() -> Arrays.stream(constructor.getGenericParameterTypes()).map(Descriptors::forType).toArray(TypeDescriptor[]::new)),
            Suppliers.memoize(() -> Arrays.stream(constructor.getGenericExceptionTypes()).map(Descriptors::forType).toArray(TypeDescriptor[]::new))
        ));
    }

    /**
     * Creates a descriptor for a method.
     *
     * @param method the Java reflection method to describe
     * @return the descriptor for the method
     */
    public static MethodDescriptor forMethod(Method method) {
        return METHOD_CACHE.computeIfAbsent(Stringify.getSignature(method), ignored -> MethodDescriptor.of(
            Suppliers.memoize(() -> forClass(method.getDeclaringClass())),
            method.getModifiers(),
            Suppliers.memoize(() -> Arrays.stream(method.getTypeParameters()).map(Descriptors::forTypeVariable).toArray(TypeVariableDescriptor[]::new)),
            Suppliers.memoize(() -> forType(method.getGenericReturnType())),
            method.getName(),
            Suppliers.memoize(() -> Arrays.stream(method.getGenericParameterTypes()).map(Descriptors::forType).toArray(TypeDescriptor[]::new)),
            Suppliers.memoize(() -> Arrays.stream(method.getGenericExceptionTypes()).map(Descriptors::forType).toArray(TypeDescriptor[]::new))
        ));
    }

    // Finder's keepers

    /**
     * Attempts to find a matching package and returns a descriptor for it.
     * If no match is found, this method returns {@code null}.
     * <p>
     * This method must only be used for submission packages and can handle typos and similar mistakes
     * up to certain degree.
     * However, this also puts it at risk of returning a descriptor for the wrong package if its name
     * is too similar.
     * See {@link org.tudalgo.algoutils.AlgoUtils AlgoUtils} for information on similarity matching.
     *
     * @param name the fully-qualified name of the package to find
     * @return a matching descriptor, or {@code null}
     */
    public static PackageDescriptor findPackage(String name) {
        if (!name.startsWith(AlgoUtils.SUBMISSION_ID)) throw new IllegalArgumentException("Package name doesn't start with submission ID");
        return null;
    }

    /**
     * Attempts to find a matching class and returns a descriptor for it.
     * If no match is found, this method returns {@code null}.
     * <p>
     * This method can handle typos and similar mistakes up to certain degree for submission classes.
     * However, this also puts it at risk of returning a descriptor for the wrong class if its name
     * is too similar.
     * See {@link AlgoUtils} for information on similarity matching.
     *
     * @param name the fully-qualified name of the class to find
     * @return a matching descriptor, or {@code null}
     */
    public static ClassDescriptor findClass(String name) {
        try {
            if (!name.startsWith(AlgoUtils.SUBMISSION_ID)) return forClass(Class.forName(name));

            // Simple lookup
            ClassDescriptor cacheLookup = CLASS_CACHE.get(name);
            if (cacheLookup != null) return cacheLookup;
            if (submissionClasses.contains(name)) return forClass(Class.forName(name));

            // Search for closest match
            ClassDescriptor cacheSearch = findMostSimilar(CLASS_CACHE, name);
            if (cacheSearch != null) return cacheSearch;
            Callable<ClassDescriptor> submissionSearch = findMostSimilar(submissionClasses.stream()
                .map(s -> Map.entry(s, (Callable<ClassDescriptor>) () -> forClass(Class.forName(s))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), name);
            if (submissionSearch != null) return submissionSearch.call();

            // no match found
            return null;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Attempts to find a matching field and returns a descriptor for it.
     * If no match is found, this method returns {@code null}.
     * <p>
     * This method can handle typos and similar mistakes up to certain degree for fields of submission classes.
     * However, this also puts it at risk of returning a descriptor for the wrong field if its signature
     * is too similar.
     * See {@link AlgoUtils} for information on similarity matching.
     *
     * @param declaringClass the class that declares the field
     * @param name           the name of the field to find
     * @return a matching descriptor, or {@code null}
     */
    // TODO: create variant for accessible fields
    public static FieldDescriptor findField(ClassDescriptor declaringClass, String name) {
        // Simple lookup
        FieldDescriptor cacheLookup = FIELD_CACHE.get("%s#%s".formatted(declaringClass.getName(), name));
        if (cacheLookup != null) return cacheLookup;

        // Search for closest match
        Class<?> clazz = declaringClass.reflect();
        Field field = Arrays.stream(clazz.getDeclaredFields())
            .map(f -> Map.entry(MatchingUtils.similarity(name, f.getName()), f))
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .orElse(null);
        return field != null ? forField(field) : null;
    }

    /**
     * Attempts to find a matching constructor and returns a descriptor for it.
     * If no match is found, this method returns {@code null}.
     * <p>
     * This method can handle typos and similar mistakes up to certain degree for constructors of submission classes.
     * However, this also puts it at risk of returning a descriptor for the wrong constructor if its signature
     * is too similar.
     * See {@link AlgoUtils} for information on similarity matching.
     *
     * @param declaringClass the class that declares the constructor
     * @param parameterTypes the parameter types of the constructor to find
     * @return a matching descriptor, or {@code null}
     */
    public static ConstructorDescriptor findConstructor(ClassDescriptor declaringClass, ClassDescriptor... parameterTypes) {
        String signature = "%s#<init>(%s)".formatted(declaringClass.getName(),
            Arrays.stream(parameterTypes)
                .map(ClassDescriptor::getName)
                .collect(Collectors.joining(", ")));

        // Simple lookup
        ConstructorDescriptor cacheLookup = CONSTRUCTOR_CACHE.get(signature);
        if (cacheLookup != null) return cacheLookup;

        // Search for closest match
        Class<?> clazz = declaringClass.reflect();
        Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors())
            .filter(c -> c.getParameterTypes().length == parameterTypes.length)
            .map(c -> Map.entry(MatchingUtils.similarity(signature, Stringify.getSignature(c)), c))
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .orElse(null);
        return constructor != null ? forConstructor(constructor) : null;
    }

    /**
     * Attempts to find a matching method and returns a descriptor for it.
     * If no match is found, this method returns {@code null}.
     * <p>
     * This method can handle typos and similar mistakes up to certain degree for methods of submission classes.
     * However, this also puts it at risk of returning a descriptor for the wrong method if its signature
     * is too similar.
     * See {@link AlgoUtils} for information on similarity matching.
     *
     * @param declaringClass the class that declares the method
     * @param name           the name of the method to find
     * @param parameterTypes the parameter types of the method to find
     * @return a matching descriptor, or {@code null}
     */
    // TODO: create variant for accessible methods
    public static MethodDescriptor findMethod(ClassDescriptor declaringClass, String name, ClassDescriptor... parameterTypes) {
        String signature = "%s#%s(%s)".formatted(declaringClass.getName(), name,
            Arrays.stream(parameterTypes)
                .map(ClassDescriptor::getName)
                .collect(Collectors.joining(", ")));

        // Simple lookup
        MethodDescriptor cacheLookup = METHOD_CACHE.get(signature);
        if (cacheLookup != null) return cacheLookup;

        // Search for closest match
        Class<?> clazz = declaringClass.reflect();
        Method method = Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> m.getParameterTypes().length == parameterTypes.length)
            .map(m -> Map.entry(MatchingUtils.similarity(signature, Stringify.getSignature(m)), m))
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .orElse(null);
        return method != null ? forMethod(method) : null;
    }

    // TODO: Move to / merge with more fitting class
    private static <T> T findMostSimilar(Map<String, ? extends T> map, String target) {
        return map.entrySet()
            .stream()
            .map(entry -> Map.entry(MatchingUtils.similarity(target, entry.getKey()), entry.getValue()))
            .filter(entry -> entry.getKey() >= AlgoUtils.SIMILARITY_THRESHOLD)
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .orElse(null);
    }

    // TODO: Move to / merge with more fitting class
    @SuppressWarnings("UnstableApiUsage")
    private static void setup() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            ClassPath classPath = ClassPath.from(classLoader);
            for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive(AlgoUtils.SUBMISSION_ID)) {
                packages.add(classInfo.getPackageName());
                submissionClasses.add(classInfo.getName());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // TODO: Move to / merge with more fitting class
    private static Set<String> packages = new HashSet<>();
    private static Set<String> submissionClasses = new HashSet<>();

    // Factory methods

    /**
     * Describes a package by name.
     *
     * @param name the fully-qualified name of the package
     * @return the descriptor for the described package
     */
    public static PackageDescriptor describePackage(String name) {
        return PackageDescriptor.of(name);
    }

    /**
     * Describes a class type by name.
     *
     * @param name the name of the class
     * @return the descriptor for the described class
     */
    public static ClassDescriptor describeClass(String name) {
        return ClassDescriptor.of(0, name, new TypeVariableDescriptor[0], null, new TypeDescriptor[0]);
    }

    /**
     * Describes a generic array type.
     *
     * @param componentType the component type of the generic array
     * @return the descriptor for the described generic array type
     */
    public static GenericArrayTypeDescriptor describeGenericArrayType(TypeDescriptor componentType) {
        return GenericArrayTypeDescriptor.of(componentType);
    }

    /**
     * Describes a parameterized type.
     *
     * @param rawType       the raw type of the parameterized type
     * @param typeArguments the type arguments of the parameterized type
     * @return the descriptor for the described parameterized type
     */
    public static ParameterizedTypeDescriptor describeParameterizedType(TypeDescriptor rawType, TypeDescriptor... typeArguments) {
        return ParameterizedTypeDescriptor.of(rawType, typeArguments);
    }

    /**
     * Describes a type variable.
     *
     * @param name   the name of the type variable
     * @param bounds the bounds of the type variable
     * @return the descriptor for the described type variable
     */
    public static TypeVariableDescriptor describeTypeVariable(String name, TypeDescriptor... bounds) {
        return TypeVariableDescriptor.of(name, bounds);
    }

    /**
     * Describes a wildcard type.
     *
     * @param boundsType the kind of wildcard bounds
     * @param bounds     the bounds of the wildcard type
     * @return the descriptor for the described wildcard type
     */
    public static WildcardTypeDescriptor describeWildcardType(WildcardTypeDescriptor.BoundsType boundsType, TypeDescriptor... bounds) {
        return WildcardTypeDescriptor.of(boundsType, bounds);
    }

    /**
     * Describes a field by declaring type and name.
     *
     * @param declaringType the declaring type of the field
     * @param name          the name of the field
     * @return the descriptor for the described field
     */
    public static FieldDescriptor describeField(TypeDescriptor declaringType, String name) {
        return FieldDescriptor.of(declaringType, 0, null, name);
    }

    /**
     * Describes a constructor by declaring type and parameter types.
     *
     * @param declaringType  the declaring type of the constructor
     * @param parameterTypes the parameter types of the constructor
     * @return the descriptor for the described constructor
     */
    public static ConstructorDescriptor describeConstructor(TypeDescriptor declaringType, TypeDescriptor... parameterTypes) {
        return ConstructorDescriptor.of(declaringType, 0, new TypeVariableDescriptor[0], parameterTypes, new TypeDescriptor[0]);
    }

    /**
     * Describes a method by declaring type, name and parameter types.
     *
     * @param declaringType  the declaring type of the method
     * @param name           the name of the method
     * @param parameterTypes the parameter types of the method
     * @return the descriptor for the described method
     */
    public static MethodDescriptor describeMethod(TypeDescriptor declaringType, String name, TypeDescriptor... parameterTypes) {
        return MethodDescriptor.of(declaringType, 0, new TypeVariableDescriptor[0], null, name, parameterTypes, new TypeDescriptor[0]);
    }

    // Builders

    /**
     * Creates a new builder for a class descriptor.
     *
     * @param name the name of the class
     * @return a builder for the class descriptor
     */
    public static ClassDescriptor.Builder classDescriptorBuilder(String name) {
        return ClassDescriptor.builder(name);
    }

    /**
     * Creates a new builder for a field descriptor.
     *
     * @param declaringType the declaring type of the field
     * @param name          the name of the field
     * @return a builder for the field descriptor
     */
    public static FieldDescriptor.Builder fieldDescriptorBuilder(TypeDescriptor declaringType, String name) {
        return FieldDescriptor.builder(declaringType, name);
    }

    /**
     * Creates a new builder for a constructor descriptor.
     *
     * @param declaringType  the declaring type of the constructor
     * @param parameterTypes the parameter types of the constructor
     * @return a builder for the constructor descriptor
     */
    public static ConstructorDescriptor.Builder constructorDescriptorBuilder(TypeDescriptor declaringType, TypeDescriptor... parameterTypes) {
        return ConstructorDescriptor.builder(declaringType, parameterTypes);
    }

    /**
     * Creates a new builder for a method descriptor.
     *
     * @param declaringType  the declaring type of the method
     * @param name           the name of the method
     * @param parameterTypes the parameter types of the method
     * @return a builder for the method descriptor
     */
    public static MethodDescriptor.Builder methodDescriptorBuilder(TypeDescriptor declaringType, String name, TypeDescriptor... parameterTypes) {
        return MethodDescriptor.builder(declaringType, name, parameterTypes);
    }

    // TODO: Move to / merge with more fitting class
    public static final class Stringify {

        public static String getSignature(Class<?> clazz) {
            return clazz.getTypeName();
        }

        public static String getSignature(Field field) {
            return "%s#%s".formatted(field.getDeclaringClass().getTypeName(), field.getName());
        }

        public static String getSignature(Constructor<?> constructor) {
            return "%s#<init>(%s)".formatted(
                constructor.getDeclaringClass().getTypeName(),
                Arrays.stream(constructor.getParameterTypes()).map(Type::getTypeName).collect(Collectors.joining(", "))
            );
        }

        public static String getSignature(Method method) {
            return "%s#%s(%s)".formatted(
                method.getDeclaringClass().getTypeName(),
                method.getName(),
                Arrays.stream(method.getParameterTypes()).map(Type::getTypeName).collect(Collectors.joining(", "))
            );
        }
    }
}
