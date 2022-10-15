package org.tudalgo.algoutils.tutor.general.stringify.basic;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        if (object instanceof Class<?> clazz) {
            return clazz.getName();
        } else if (object instanceof Field field) {
            var clazzString = field.getDeclaringClass().getName();
            var typeString = field.getType().getSimpleName();
            var name = field.getName();
            return String.format("%s#%s %s", clazzString, typeString, name);
        } else if (object instanceof Method method) {
            var clazzString = method.getDeclaringClass().getName();
            var name = method.getName();
            var parametersString = Stream.of(method.getParameters())
                .map(parameter -> String.format("%s %s", parameter.getType().getName(), parameter.getName()))
                .collect(Collectors.joining(", "));
            return String.format("%s#%s(%s)", clazzString, name, parametersString);
        } else if (object instanceof Parameter parameter) {
            var typeString = parameter.getType().getSimpleName();
            var name = parameter.getName();
            return String.format("%s %s", typeString, name);
        }
        return null;
    }
}
