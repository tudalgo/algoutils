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
    }    private final List<FieldLink> fields = new LinkedList<>(), unmodifiableFields = unmodifiableList(fields);

    @Override
    public List<FieldLink> getFields() {
        if (fields.isEmpty()) {
            stream(type.getDeclaredFields()).map(BasicFieldLink::of).forEach(fields::add);
        }
        return unmodifiableFields;
    }

    @Override
    public Collection<TypeLink> getInterfaces() {
        return null;
    }    private final List<MethodLink> methods = new LinkedList<>(), unmodifiableMethods = unmodifiableList(methods);

    @Override
    public Collection<MethodLink> getMethods() {
        if (methods.isEmpty()) {
            stream(type.getDeclaredMethods()).map(BasicMethodLink::of).forEach(methods::add);
        }
        return unmodifiableMethods;
    }

    @Override
    public String identifier() {
        return type.getName();
    }

    @Override
    public Class<?> link() {
        return type;
    }

    @Override
    public int modifiers() {
        return type.getModifiers();
    }






}
