package org.tudalgo.algoutils.tutor.general.reflections;

import java.lang.reflect.Constructor;

public interface ConstructorLink extends Link, WithType, WithTypeList, WithModifiers {

    @Override
    default Kind kind() {
        return Kind.CONSTRUCTOR;
    }

    <T> T invoke(Object... arguments) throws Throwable;

    @Override
    Constructor<?> reflection();
}
