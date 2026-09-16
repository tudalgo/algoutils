package org.tudalgo.algoutils.descriptors;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

// TODO: Move to / merge with more fitting class
public final class Stringify {

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
