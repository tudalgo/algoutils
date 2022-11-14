package org.tudalgo.algoutils.tutor.general.reflections;

import java.util.*;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;

/**
 * A basic implementation of a {@link TypeLink type link}.
 */
public class BasicTypeLink implements TypeLink {

    private static final Map<Class<?>, BasicTypeLink> INSTANCES = new HashMap<>();

    private final Class<?> type;


    private BasicTypeLink(Class<?> type) {
        this.type = type;
    }

    public static BasicTypeLink of(Class<?> type) {
        return INSTANCES.computeIfAbsent(type, BasicTypeLink::new);
    }

    @Override
    public Collection<TypeLink> interfaces() {
        if (interfaces.isEmpty()) {
            stream(type.getInterfaces()).map(BasicTypeLink::of).forEach(interfaces::add);
        }
        return unmodifiableInterfaces;
    }

    @Override
    public TypeLink superType() {
        return of(type.getSuperclass());
    }    private final List<FieldLink> fields = new LinkedList<>(), unmodifiableFields = unmodifiableList(fields);

    @Override
    public List<FieldLink> getFields() {
        if (fields.isEmpty()) {
            stream(type.getDeclaredFields()).map(BasicFieldLink::of).forEach(fields::add);
        }
        return unmodifiableFields;
    }

    @Override
    public Collection<ConstructorLink> getConstructors() {
        if (constructors.isEmpty()) {
            stream(type.getDeclaredConstructors()).map(BasicConstructorLink::of).forEach(constructors::add);
        }
        return unmodifiableConstructors;
    }

    @Override
    public Collection<EnumConstantLink> getEnumConstants() {
        if (!type.isEnum()) {
            return Collections.emptyList();
        }
        @SuppressWarnings("unchecked")
        var enumType = (Class<Enum<?>>) type;
        if (enums.isEmpty()) {
            stream(enumType.getEnumConstants()).map(BasicEnumConstantLink::of).forEach(enums::add);
        }
        return unmodifiableEnums;
    }    private final List<TypeLink> interfaces = new ArrayList<>(), unmodifiableInterfaces = unmodifiableList(interfaces);

    @Override
    public String identifier() {
        return type.getSimpleName();
    }

    @Override
    public Class<?> reflection() {
        return type;
    }

    @Override
    public Kind kind() {
        if (type.isEnum()) {
            return Kind.ENUM;
        } else if (type.isInterface()) {
            return Kind.INTERFACE;
        } else if (type.isPrimitive()) {
            return Kind.PRIMITIVE;
        } else if (type.isArray()) {
            return Kind.ARRAY;
        } else if (type.isAnnotation()) {
            return Kind.ANNOTATION;
        } else if (type.isRecord()) {
            return Kind.RECORD;
        } else {
            return Kind.CLASS;
        }
    }

    private final List<MethodLink> methods = new LinkedList<>(), unmodifiableMethods = unmodifiableList(methods);

    @Override
    public Collection<MethodLink> getMethods() {
        if (methods.isEmpty()) {
            stream(type.getDeclaredMethods()).map(BasicMethodLink::of).forEach(methods::add);
        }
        return unmodifiableMethods;
    }



    private final List<ConstructorLink> constructors = new LinkedList<>(), unmodifiableConstructors = unmodifiableList(constructors);



    private final List<EnumConstantLink> enums = new LinkedList<>(), unmodifiableEnums = unmodifiableList(enums);
}
