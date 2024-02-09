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

    /**
     * Factory method for creating a package link
     *
     * @param name      package name
     * @param recursive whether to include sub-packages
     * @return package link
     */
    public static BasicPackageLink of(String name, boolean recursive) {
        List<Class<?>> classes;
        try {
            classes = Arrays.stream(ReflectUtils.getClasses(name, recursive)).toList();
        } catch (IOException e) {
            throw new AssertionFailedError("internal error in class BasicPackageLink");
        }
        return INSTANCES.computeIfAbsent(name, n -> new BasicPackageLink(n, classes));
    }

    /**
     * Overloaded method for non-recursive package link
     *
     * @param name package name
     * @return package link
     */
    public static BasicPackageLink of(String name) {
        return of(name, false);
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
