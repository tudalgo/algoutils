package org.tudalgo.algoutils.assertions;

import org.tudalgo.algoutils.descriptors.types.TypeDescriptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class AssertionUtils {

    // Do not instantiate
    private AssertionUtils() {}

    public static String getMethodSignature(Constructor<?> constructor) {
        return getMethodSignature(constructor.getDeclaringClass().getName(), constructor.getParameterTypes());
    }

    public static String getMethodSignature(Method method) {
        return getMethodSignature(method.getName(), method.getParameterTypes());
    }

    public static String getMethodSignature(String methodName, Class<?>... parameterTypes) {
        return "%s(%s)".formatted(methodName, Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.joining(", ")));
    }

    public static String getMethodSignature(String methodName, TypeDescriptor... typeDescriptors) {
        return "%s(%s)".formatted(methodName, Arrays.stream(typeDescriptors).map(TypeDescriptor::getName).collect(Collectors.joining(", ")));
    }

    public static String getPrettyString(Member member) {
        if (member instanceof Field field) {
            return "Field %s#%s".formatted(field.getDeclaringClass().getName(), field.getName());
        } else if (member instanceof Constructor<?> constructor) {
            return "Constructor %s#%s(%s)".formatted(constructor.getDeclaringClass().getName(),
                constructor.getName(),
                Arrays.stream(constructor.getParameterTypes()).map(Class::getName).collect(Collectors.joining(", ")));
        } else if (member instanceof Method method) {
            return "Method %s#%s(%s)".formatted(method.getDeclaringClass().getName(),
                method.getName(),
                Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.joining(", ")));
        } else {
            throw new IllegalArgumentException("Unknown member type");
        }
    }
}
