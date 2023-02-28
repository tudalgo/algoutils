package org.tudalgo.algoutils.tutor.general.reflections;

import org.apache.maven.api.annotations.Nullable;
import spoon.reflect.declaration.CtField;
import spoon.reflect.path.CtRole;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

public class BasicFieldLink extends BasicLink implements FieldLink, WithCtElement {

    private final Field field;

    /**
     * The parent of this field.
     */
    private final TypeLink parent;

    /**
     * The spoon element of this link.
     */
    private @Nullable CtField<?> element;

    private BasicFieldLink(Field field) {
        field.setAccessible(true);
        this.field = field;
        this.parent = BasicTypeLink.of(field.getDeclaringClass());
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
    public Field reflection() {
        return field;
    }

    public int modifiers() {
        return field.getModifiers();
    }

    @Override
    public TypeLink staticType() {
        return BasicTypeLink.of(field.getType());
    }

    @Override
    public void set(Object object) {
        if (!Modifier.isStatic(modifiers())) {
            throw new RuntimeException(); // TODO
        }
        set(null, object);
    }

    @Override
    public void set(Object instance, Object object) {
        try {
            if (!field.getType().isPrimitive()) {
                field.set(instance, object);
            } else if (field.getType() == boolean.class) {
                field.setBoolean(instance, (boolean) object);
            } else if (field.getType() == byte.class) {
                field.setByte(instance, (byte) object);
            } else if (field.getType() == char.class) {
                field.setChar(instance, (char) object);
            } else if (field.getType() == double.class) {
                field.setDouble(instance, (double) object);
            } else if (field.getType() == float.class) {
                field.setFloat(instance, (float) object);
            } else if (field.getType() == int.class) {
                field.setInt(instance, (int) object);
            } else if (field.getType() == long.class) {
                field.setLong(instance, (long) object);
            } else if (field.getType() == short.class) {
                field.setShort(instance, (short) object);
            } else {
                throw new RuntimeException(); // TODO
            }
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

    @Override
    public String name() {
        return field.getName();
    }

    @Override
    public TypeLink parent() {
        return parent;
    }

    @Override
    public CtField<?> getCtElement() {
        if (element != null) return element;
        Set<CtField<?>> fields = ((BasicTypeLink) parent).getCtElement().getValueByRole(CtRole.FIELD);
        element = fields.stream().filter(f -> f.getSimpleName().equals(field.getName())).findFirst().orElseThrow();
        return element;
    }

}
