package org.tudalgo.algoutils.tutor.general.reflections;

import java.util.function.Predicate;

public enum Modifier {

    PUBLIC(java.lang.reflect.Modifier::isPublic),
    PROTECTED(java.lang.reflect.Modifier::isProtected),
    PRIVATE(java.lang.reflect.Modifier::isPrivate),
    DEFAULT_ACCESS(m -> !java.lang.reflect.Modifier.isPublic(m) && !java.lang.reflect.Modifier.isProtected(m) && !java.lang.reflect.Modifier.isPrivate(m)),
    STATIC(java.lang.reflect.Modifier::isStatic),
    NON_STATIC(m -> !java.lang.reflect.Modifier.isStatic(m)),
    FINAL(java.lang.reflect.Modifier::isFinal),
    NON_FINAL(m -> !java.lang.reflect.Modifier.isFinal(m)),
    INTERFACE(java.lang.reflect.Modifier::isInterface),
    ABSTRACT(java.lang.reflect.Modifier::isAbstract),
    NON_ABSTRACT(m -> !java.lang.reflect.Modifier.isAbstract(m)),
    ENUM(t -> (t & 0x00004000) != 0),
    CLASS(t -> !INTERFACE.is(t) && !ENUM.is(t));

    private final Predicate<Integer> check;

    Modifier(Predicate<Integer> check) {
        this.check = check;
    }

    public boolean is(int modifier) {
        return check.test(modifier);
    }

    public boolean isNot(int modifier) {
        return !is(modifier);
    }

    public String keyword() {
        return name().toLowerCase();
    }
}
