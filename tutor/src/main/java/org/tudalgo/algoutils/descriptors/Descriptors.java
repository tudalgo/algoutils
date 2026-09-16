package org.tudalgo.algoutils.descriptors;

import org.tudalgo.algoutils.AlgoUtils;
import org.tudalgo.algoutils.descriptors.non_generic.*;
import org.tudalgo.algoutils.descriptors.generic.*;
import org.tudalgo.algoutils.tutor.general.match.MatchingUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A collection of factory methods for creating Descriptors for Java types and members.
 * This class converts between Java reflection objects such as {@link java.lang.reflect.Type Type}
 * and the Descriptor abstractions used throughout this package.
 * The methods in this class are divided into two groups:
 * <p>
 * <b>for*</b> methods create Descriptors from Java reflection objects.
 * They act as a short one-directional way to convert reflection objects to Descriptors.
 * To get the reflection object from a descriptor, you must verify that it exists via assertion.
 * <p>
 * <b>find*</b> methods attempt to find a matching entity for the given values and return a Descriptor for the closest match.
 * Be aware that they may not yield the expected result in all cases.
 * Due to the nature of similarity matching, the wrong entity may be matched if the expected one was not found.
 * In that case, a Descriptor for that wrong entity is returned.
 * In cases where no match was found, the methods will return {@link NotFound}-Descriptors whose values are taken from the parameter values.
 * This prevents dealing with {@code null} and NullPointerExceptions while still marking the Descriptor as invalid / not found.
 * All methods in this library dealing with Descriptors will check if a Descriptor is an instance of {@link NotFound} and handle it accordingly.
 * <p>
 * This class also maintains an internal cache for packages, classes, fields, constructors and methods,
 * so two calls for the same entity to the methods in this class may yield the same Descriptor result.
 */
public final class Descriptors {

    private static final Map<String, PackageDescriptor> PACKAGE_CACHE = new HashMap<>();
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
        return PACKAGE_CACHE.computeIfAbsent(pkg.getName(), ignored -> PackageDescriptor.of(pkg.getName()));
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
        return CLASS_CACHE.computeIfAbsent(clazz.getTypeName(), ignored -> ClassDescriptor.of(clazz.getName()));
    }

    /**
     * Creates a descriptor for a generic array type.
     *
     * @param genericArrayType the Java reflection generic array type to describe
     * @return the descriptor for the generic array type
     */
    public static GenericArrayTypeDescriptor forGenericArrayType(GenericArrayType genericArrayType) {
        return GenericArrayTypeDescriptor.of(forType(genericArrayType.getGenericComponentType()));
    }

    /**
     * Creates a descriptor for a parameterized type.
     *
     * @param parameterizedType the Java reflection parameterized type to describe
     * @return the descriptor for the parameterized type
     */
    public static ParameterizedTypeDescriptor forParameterizedType(ParameterizedType parameterizedType) {
        if (parameterizedType.getRawType() instanceof Class<?> clazz) {
            return ParameterizedTypeDescriptor.of(
                forClass(clazz),
                Arrays.stream(parameterizedType.getActualTypeArguments()).map(Descriptors::forType).toArray(TypeDescriptor[]::new)
            );
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Creates a descriptor for a type variable.
     * <p>
     * Note: Self-referential type variables (e.g., {@code T extends Supplier<T>}) are not currently supported
     * and will cause a stack overflow.
     *
     * @param typeVariable the Java reflection type variable to describe
     * @return the descriptor for the type variable
     */
    public static TypeVariableDescriptor forTypeVariable(TypeVariable<?> typeVariable) {
        return TypeVariableDescriptor.of(
            typeVariable.getName(),
            Arrays.stream(typeVariable.getBounds()).map(Descriptors::forType).toArray(TypeDescriptor[]::new)
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
            switch (boundsType) {
                case NONE -> new TypeDescriptor[0];
                case LOWER -> Arrays.stream(wildcardType.getLowerBounds()).map(Descriptors::forType).toArray(TypeDescriptor[]::new);
                case UPPER -> Arrays.stream(wildcardType.getUpperBounds()).map(Descriptors::forType).toArray(TypeDescriptor[]::new);
            }
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
            forClass(field.getDeclaringClass()),
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
            forClass(constructor.getDeclaringClass()),
            Arrays.stream(constructor.getParameterTypes()).map(Descriptors::forClass).toArray(ClassDescriptor[]::new)
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
            forClass(method.getDeclaringClass()),
            method.getName(),
            Arrays.stream(method.getParameterTypes()).map(Descriptors::forClass).toArray(ClassDescriptor[]::new)
        ));
    }

    // Finder's keepers

    /**
     * Attempts to find a matching package and returns a descriptor for it.
     * If no match is found, this method returns {@link PackageDescriptor.NotFound}.
     * <p>
     * This method must only be used for submission packages and can handle typos and similar mistakes
     * up to certain degree.
     * However, this also puts it at risk of returning a descriptor for the wrong package if its name
     * is too similar.
     * See {@link AlgoUtils#SIMILARITY_THRESHOLD} for information on similarity matching.
     *
     * @param name the fully-qualified name of the package to find
     * @return a matching descriptor, or {@link PackageDescriptor.NotFound}
     * @throws IllegalArgumentException if the given package name is not part of the submission
     */
    public static PackageDescriptor findPackage(String name) {
        if (!name.startsWith(AlgoUtils.SUBMISSION_ID)) throw new IllegalArgumentException("Package name must start with submission ID");

        // Simple lookup
        PackageDescriptor cacheLookup = PACKAGE_CACHE.get(name);
        if (cacheLookup != null) return cacheLookup;
        Set<String> submissionPackages = AlgoUtils.SUBMISSION_CLASSES.stream()
            .map(s -> s.contains(".") ? s.substring(0, s.lastIndexOf(".")) : null)
            .filter(Objects::nonNull)
            .collect(Collectors.toUnmodifiableSet());
        PackageDescriptor submissionLookup = submissionPackages.stream()
            .filter(name::equals)
            .findFirst()
            .map(PackageDescriptor::of)
            .orElse(null);
        if (submissionLookup != null) return submissionLookup;

        // Search for closest match
        PackageDescriptor cacheSearch = PACKAGE_CACHE.entrySet()
            .stream()
            .map(entry -> Map.entry(MatchingUtils.similarity(name, entry.getKey()), entry.getValue()))
            .filter(entry -> entry.getKey() >= AlgoUtils.SIMILARITY_THRESHOLD)
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .orElse(null);
        if (cacheSearch != null) return cacheSearch;
        return submissionPackages.stream()
            .map(s -> Map.entry(MatchingUtils.similarity(name, s), s))
            .filter(entry -> entry.getKey() >= AlgoUtils.SIMILARITY_THRESHOLD)
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .map(PackageDescriptor::of)
            .orElse(new PackageDescriptor.NotFound(name));
    }

    /**
     * Attempts to find a matching class and returns a descriptor for it.
     * If no match is found, this method returns {@link ClassDescriptor.NotFound}.
     * <p>
     * This method can handle typos and similar mistakes up to certain degree for submission classes.
     * However, this also puts it at risk of returning a descriptor for the wrong class if its name is too similar.
     * See {@link AlgoUtils#SIMILARITY_THRESHOLD} for information on similarity matching.
     * <p>
     * This method performs the following steps to find a matching class:
     * <ol>
     *     <li>Search the internal {@link ClassDescriptor} cache for an exact match.</li>
     *     <li>Search {@link AlgoUtils#SUBMISSION_CLASSES} for an exact match.</li>
     *     <li>Compare all classes in the cache with the given name and return the best match above the similarity threshold.</li>
     *     <li>Compare all classes in {@link AlgoUtils#SUBMISSION_CLASSES} with the given name and return the best match above the similarity threshold.</li>
     * </ol>
     *
     * @param name the fully-qualified name of the class to find
     * @return a matching descriptor, or {@link ClassDescriptor.NotFound}
     */
    public static ClassDescriptor findClass(String name) {
        Function<String, ClassDescriptor> getClass = s -> {
            try {
                return forClass(Class.forName(s));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
        if (!name.startsWith(AlgoUtils.SUBMISSION_ID)) return getClass.apply(name);

        // Simple lookup
        ClassDescriptor cacheLookup = CLASS_CACHE.get(name);
        if (cacheLookup != null) return cacheLookup;
        if (AlgoUtils.SUBMISSION_CLASSES.contains(name)) return getClass.apply(name);

        // Search for closest match
        ClassDescriptor cacheSearch = CLASS_CACHE.entrySet()
            .stream()
            .map(entry -> Map.entry(MatchingUtils.similarity(name, entry.getKey()), entry.getValue()))
            .filter(entry -> entry.getKey() >= AlgoUtils.SIMILARITY_THRESHOLD)
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .orElse(null);
        if (cacheSearch != null) return cacheSearch;
        return AlgoUtils.SUBMISSION_CLASSES.stream()
            .map(s -> Map.entry(MatchingUtils.similarity(name, s), s))
            .filter(entry -> entry.getKey() >= AlgoUtils.SIMILARITY_THRESHOLD)
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .map(getClass)
            .orElseGet(() -> new ClassDescriptor.NotFound(name));
    }

    /**
     * Attempts to find a matching field and returns a descriptor for it.
     * If no match is found or the declaring class could be resolved,
     * this method returns {@link FieldDescriptor.NotFound}.
     * <p>
     * This method can handle typos and similar mistakes up to certain degree for fields of submission classes.
     * However, this also puts it at risk of returning a descriptor for the wrong field if its signature
     * is too similar.
     * See {@link AlgoUtils#SIMILARITY_THRESHOLD} for information on similarity matching.
     *
     * @param declaringClass the class that declares the field
     * @param name           the name of the field to find
     * @return a matching descriptor, or {@link FieldDescriptor.NotFound}
     */
    // TODO: create variant for accessible fields
    public static FieldDescriptor findField(ClassDescriptor declaringClass, String name) {
        if (declaringClass instanceof ClassDescriptor.NotFound) return new FieldDescriptor.NotFound(declaringClass, name);

        // Simple lookup
        FieldDescriptor cacheLookup = FIELD_CACHE.get("%s#%s".formatted(declaringClass.getName(), name));
        if (cacheLookup != null) return cacheLookup;

        // Search for closest match
        Class<?> clazz = resolveClass(declaringClass);
        if (clazz == null) return new FieldDescriptor.NotFound(declaringClass, name);
        Field field = Arrays.stream(clazz.getDeclaredFields())
            .map(f -> Map.entry(MatchingUtils.similarity(name, f.getName()), f))
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .orElse(null);
        return field != null ? forField(field) : new FieldDescriptor.NotFound(declaringClass, name);
    }

    /**
     * Attempts to find a matching constructor and returns a descriptor for it.
     * If no match is found or the declaring class or any parameter types could not be resolved,
     * this method returns {@link ConstructorDescriptor.NotFound}.
     * <p>
     * This method can handle typos and similar mistakes up to certain degree for constructors of submission classes.
     * However, this also puts it at risk of returning a descriptor for the wrong constructor if its signature
     * is too similar.
     * See {@link AlgoUtils#SIMILARITY_THRESHOLD} for information on similarity matching.
     *
     * @param declaringClass the class that declares the constructor
     * @param parameterTypes the parameter types of the constructor to find
     * @return a matching descriptor, or {@link ConstructorDescriptor.NotFound}
     */
    public static ConstructorDescriptor findConstructor(ClassDescriptor declaringClass, ClassDescriptor... parameterTypes) {
        if (declaringClass instanceof ClassDescriptor.NotFound) return new ConstructorDescriptor.NotFound(declaringClass, parameterTypes);
        for (ClassDescriptor parameterType : parameterTypes) {
            if (parameterType instanceof ClassDescriptor.NotFound) return new ConstructorDescriptor.NotFound(declaringClass, parameterTypes);
        }
        String signature = "%s#<init>(%s)".formatted(declaringClass.getName(),
            Arrays.stream(parameterTypes)
                .map(ClassDescriptor::getName)
                .collect(Collectors.joining(", ")));

        // Simple lookup
        ConstructorDescriptor cacheLookup = CONSTRUCTOR_CACHE.get(signature);
        if (cacheLookup != null) return cacheLookup;

        // Search for closest match
        Class<?> clazz = resolveClass(declaringClass);
        if (clazz == null) return new ConstructorDescriptor.NotFound(declaringClass, parameterTypes);
        Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors())
            .filter(c -> c.getParameterTypes().length == parameterTypes.length)
            .map(c -> Map.entry(MatchingUtils.similarity(signature, Stringify.getSignature(c)), c))
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .orElse(null);
        return constructor != null ? forConstructor(constructor) : new ConstructorDescriptor.NotFound(declaringClass, parameterTypes);
    }

    /**
     * Attempts to find a matching method and returns a descriptor for it.
     * If no match is found or the declaring class or any parameter types could not be resolved,
     * this method returns {@link MethodDescriptor.NotFound}.
     * <p>
     * This method can handle typos and similar mistakes up to certain degree for methods of submission classes.
     * However, this also puts it at risk of returning a descriptor for the wrong method if its signature
     * is too similar.
     * See {@link AlgoUtils#SIMILARITY_THRESHOLD} for information on similarity matching.
     *
     * @param declaringClass the class that declares the method
     * @param name           the name of the method to find
     * @param parameterTypes the parameter types of the method to find
     * @return a matching descriptor, or {@link MethodDescriptor.NotFound}
     */
    // TODO: create variant for accessible methods
    public static MethodDescriptor findMethod(ClassDescriptor declaringClass, String name, ClassDescriptor... parameterTypes) {
        if (declaringClass instanceof ClassDescriptor.NotFound) return new MethodDescriptor.NotFound(declaringClass, name, parameterTypes);
        for (ClassDescriptor parameterType : parameterTypes) {
            if (parameterType instanceof ClassDescriptor.NotFound) return new MethodDescriptor.NotFound(declaringClass, name, parameterTypes);
        }
        String signature = "%s#%s(%s)".formatted(declaringClass.getName(), name,
            Arrays.stream(parameterTypes)
                .map(ClassDescriptor::getName)
                .collect(Collectors.joining(", ")));

        // Simple lookup
        MethodDescriptor cacheLookup = METHOD_CACHE.get(signature);
        if (cacheLookup != null) return cacheLookup;

        // Search for closest match
        Class<?> clazz = resolveClass(declaringClass);
        if (clazz == null) return new MethodDescriptor.NotFound(declaringClass, name, parameterTypes);
        Method method = Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> m.getParameterTypes().length == parameterTypes.length)
            .map(m -> Map.entry(MatchingUtils.similarity(signature, Stringify.getSignature(m)), m))
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .orElse(null);
        return method != null ? forMethod(method) : new MethodDescriptor.NotFound(declaringClass, name, parameterTypes);
    }

    private static Class<?> resolveClass(ClassDescriptor descriptor) {
        try {
            return Class.forName(descriptor.getName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
