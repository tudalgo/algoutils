package org.tudalgo.algoutils.tutor.general.reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;

public class BasicConstructorLink extends BasicLink implements ConstructorLink {

    private static final Map<Constructor<?>, BasicConstructorLink> INSTANCES = new HashMap<>();

    private final Constructor<?> constructor;
    private final TypeLink typeLink;
    private final List<BasicTypeLink> parameterTypeLinks;

    private BasicConstructorLink(Constructor<?> constructor) {
        this.constructor = constructor;
        this.typeLink = BasicTypeLink.of(constructor.getDeclaringClass());
        this.parameterTypeLinks = stream(constructor.getParameterTypes()).map(BasicTypeLink::of).toList();
    }

    /**
     * <p>Returns the constructor link for the given constructor.</p>
     *
     * @param constructor the constructor
     * @return the constructor link
     */
    public static ConstructorLink of(Constructor<?> constructor) {
        return INSTANCES.computeIfAbsent(constructor, BasicConstructorLink::new);
    }

    @Override
    public Constructor<?> reflection() {
        return constructor;
    }

    @Override
    public List<BasicTypeLink> typeList() {
        return parameterTypeLinks;
    }

    @Override
    public <T> T invoke(Object... arguments) throws Exception {
        try {
            //noinspection unchecked
            return (T) reflection().newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            //noinspection unchecked
            throw (Exception) e.getCause();
        }
    }

    @Override
    public int modifiers() {
        return constructor.getModifiers();
    }

    @Override
    public TypeLink type() {
        return typeLink;
    }
}
