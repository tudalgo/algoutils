package org.tudalgo.algoutils.tutor.general.reflections;

public interface ConstructorLink extends Link, WithType, WithTypeList, WithModifiers, WithImports {

    @Override
    default Kind kind() {
        return Kind.CONSTRUCTOR;
    }

    <T> T invoke(Object... arguments) throws Exception;

}
