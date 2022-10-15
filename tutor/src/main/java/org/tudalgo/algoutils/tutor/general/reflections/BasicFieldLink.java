package org.tudalgo.algoutils.tutor.general.reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class BasicFieldLink implements FieldLink {

    private final Field field;

    private BasicFieldLink(Field field) {
        field.setAccessible(true);
        this.field = field;
    }

    public static BasicFieldLink of(Field field) {
        return new BasicFieldLink(field);
    }

    @Override
    public <T> T get() {
        if (!Modifier.isStatic(modifiers())) {
            throw new RuntimeException("unexpectedly non-static"); // TODO
        }
        try {
            //noinspection unchecked
            return (T) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @Override
    public <T> T get(Object instance) {
        if (Modifier.isStatic(modifiers())) {
            throw new RuntimeException("unexpectedly static"); // TODO
        }
        try {
            //noinspection unchecked
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @Override
    public String identifier() {
        return field.getName();
    }

    @Override
    public Field link() {
        return field;
    }

    @Override
    public int modifiers() {
        return field.getModifiers();
    }

    @Override
    public void set(Object object) {
        if (!Modifier.isStatic(modifiers())) {
            throw new RuntimeException(); // TODO
        }
        try {
            field.set(null, object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @Override
    public void set(Object instance, Object object) {
        if (Modifier.isStatic(modifiers())) {
            throw new RuntimeException(); // TODO
        }
        try {
            field.set(instance, object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @Override
    public String toString() {
        return "BasicFieldLink{" +
            "field=" + field +
            '}';
    }
}
