package org.tudalgo.algoutils.tutor.general.stringify.basic;

import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.Link;
import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * A stringifier for transforming reflection object to string objects.
 */
public final class ReflectionStringifier implements Stringifier {

    private static final ReflectionStringifier instance = new ReflectionStringifier();

    private ReflectionStringifier() {

    }

    /**
     * Returns an instance of this class
     *
     * @return the instance of this class
     */
    public static ReflectionStringifier getInstance() {
        return instance;
    }

    @Override
    public String stringifyOrElseNull(Object object) {
        if (object == null)
            return null;
        if (object instanceof String s) {
            return s;
        } else if (object instanceof List<?> l) {
            return l.stream().map(this::stringifyOrElseNull).collect(Collectors.joining(", ", "[", "]"));
        } else if (object instanceof Matcher<?> m) {
            return stringifyOrElseNull(m.object());
        } else if (object instanceof Link l) {
            return stringifyOrElseNull(l.reflection());
        } else if (object instanceof Class<?> clazz) {
            return clazz.getSimpleName();
        } else if (object instanceof Field field) {
            var clazzString = field.getDeclaringClass().getSimpleName();
            var typeString = field.getType().getSimpleName();
            var name = field.getName();
            return format("%s#%s %s", clazzString, typeString, name);
        } else if (object instanceof Method method) {
            var clazzString = method.getDeclaringClass().getSimpleName();
            var name = method.getName();
            var parametersString = Stream.of(method.getParameters())
                .map(parameter -> format("%s", parameter.getType().getSimpleName()))
                .collect(Collectors.joining(", "));
            return format("%s#%s(%s)", clazzString, name, parametersString);
        } else if (object instanceof Parameter parameter) {
            var typeString = parameter.getType().getSimpleName();
            return format("%s", typeString);
        } else if (object instanceof Constructor<?> c) {
            return format("constructor of %s", c.getDeclaringClass().getSimpleName());
        }
        return Objects.toString(object);
    }
}
