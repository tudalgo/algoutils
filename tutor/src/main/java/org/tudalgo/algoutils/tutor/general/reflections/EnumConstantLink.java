package org.tudalgo.algoutils.tutor.general.reflections;

public interface EnumConstantLink extends Link, WithName {

    @Override
    Enum<?> reflection();

    default Enum<?> constant() {
        return reflection();
    }

    @Override
    default Kind kind() {
        return Kind.ENUM;
    }

}
