package org.tudalgo.algoutils.tutor.general.reflections;

import spoon.reflect.declaration.CtType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static org.tudalgo.algoutils.tutor.general.SpoonUtils.getType;

/**
 * A basic implementation of a {@link TypeLink type link}.
 */
public class BasicTypeLink implements TypeLink, WithCtElement {

    private static final Map<Class<?>, BasicTypeLink> INSTANCES = new HashMap<>();

    private final Class<?> type;
    private CtType<?> element;

    private BasicTypeLink(Class<?> type) {
        this.type = type;
    }

    public static BasicTypeLink of(Class<?> type) {
        return INSTANCES.computeIfAbsent(type, BasicTypeLink::new);
    }

    @Override
    public Collection<BasicTypeLink> interfaces() {
        if (interfaces.isEmpty()) {
            stream(type.getInterfaces()).map(BasicTypeLink::of).forEach(interfaces::add);
        }
        return unmodifiableInterfaces;
    }

    @Override
    public TypeLink superType() {
        return of(type.getSuperclass());
    }

    private final List<BasicFieldLink> fields = new LinkedList<>(), unmodifiableFields = unmodifiableList(fields);

    @Override
    public List<BasicFieldLink> getFields() {
        if (fields.isEmpty()) {
            stream(type.getDeclaredFields()).map(BasicFieldLink::of).forEach(fields::add);
        }
        return unmodifiableFields;
    }

    @Override
    public Collection<BasicConstructorLink> getConstructors() {
        if (constructors.isEmpty()) {
            stream(type.getDeclaredConstructors()).map(BasicConstructorLink::of).forEach(constructors::add);
        }
        return unmodifiableConstructors;
    }

    @Override
    public Collection<BasicEnumConstantLink> getEnumConstants() {
        if (!type.isEnum()) {
            return Collections.emptyList();
        }
        @SuppressWarnings("unchecked")
        var enumType = (Class<Enum<?>>) type;
        if (enums.isEmpty()) {
            stream(enumType.getEnumConstants()).map(BasicEnumConstantLink::of).forEach(enums::add);
        }
        return unmodifiableEnums;
    }

    @Override
    public String identifier() {
        return type.getSimpleName();
    }

    private final List<BasicTypeLink> interfaces = new ArrayList<>(), unmodifiableInterfaces = unmodifiableList(interfaces);

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

    @Override
    public Collection<BasicMethodLink> getMethods() {
        if (methods.isEmpty()) {
            stream(type.getDeclaredMethods()).map(BasicMethodLink::of).forEach(methods::add);
        }
        return unmodifiableMethods;
    }

    @Override
    public CtType<?> getCtElement() {
        if (element != null) {
            return element;
        }
        String className = reflection().getName();
        return element = getType(className);
    }

    private final List<BasicMethodLink> methods = new LinkedList<>(), unmodifiableMethods = unmodifiableList(methods);


    private final List<BasicConstructorLink> constructors = new LinkedList<>(), unmodifiableConstructors = unmodifiableList(constructors);


    private final List<BasicEnumConstantLink> enums = new LinkedList<>(), unmodifiableEnums = unmodifiableList(enums);


}
