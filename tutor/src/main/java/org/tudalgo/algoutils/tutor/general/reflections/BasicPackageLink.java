package org.tudalgo.algoutils.tutor.general.reflections;

import org.opentest4j.AssertionFailedError;

import java.io.IOException;
import java.util.*;

public class BasicPackageLink extends BasicLink implements PackageLink {

    private static final Map<String, BasicPackageLink> INSTANCES = new HashMap<>();

    private final String name;
    private final Collection<BasicTypeLink> classes;

    private BasicPackageLink(String name, Collection<Class<?>> classes) {
        this.name = name;
        this.classes = classes.stream().map(BasicTypeLink::of).toList();
    }

    public static BasicPackageLink of(String name) {
        List<Class<?>> classes;
        try {
            classes = Arrays.stream(ReflectUtils.getClasses(name)).toList();
        } catch (IOException e) {
            throw new AssertionFailedError("internal error in class BasicPackageLink");
        }
        return INSTANCES.computeIfAbsent(name, n -> new BasicPackageLink(n, classes));
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Object reflection() {
        return null;
    }

    @Override
    public Collection<BasicTypeLink> getTypes() {
        return classes;
    }
}
