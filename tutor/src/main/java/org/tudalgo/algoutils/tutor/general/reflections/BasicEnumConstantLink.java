package org.tudalgo.algoutils.tutor.general.reflections;

import java.util.HashMap;
import java.util.Map;

public class BasicEnumConstantLink extends BasicLink implements EnumConstantLink {

    private static final Map<Enum<?>, BasicEnumConstantLink> INSTANCES = new HashMap<>();

    private final Enum<?> linkToEnum;

    private BasicEnumConstantLink(Enum<?> linkToEnum) {
        this.linkToEnum = linkToEnum;
    }

    public static BasicEnumConstantLink of(Enum<?> linkToEnum) {
        return INSTANCES.computeIfAbsent(linkToEnum, BasicEnumConstantLink::new);
    }

    @Override
    public String name() {
        return linkToEnum.name();
    }

    @Override
    public Enum<?> reflection() {
        return linkToEnum;
    }
}
