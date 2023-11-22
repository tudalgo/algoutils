package org.tudalgo.algoutils.tutor.general.reflections;

import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;

public class BasicConstructorLink extends BasicLink implements ConstructorLink, WithCtElement {

    private static final Map<Constructor<?>, BasicConstructorLink> INSTANCES = new HashMap<>();

    private final Constructor<?> constructor;
    private final TypeLink typeLink;
    private final List<BasicTypeLink> parameterTypeLinks;

    private final BasicTypeLink parent;

    private CtConstructor<?> element;

    private BasicConstructorLink(Constructor<?> constructor) {
        constructor.setAccessible(true);
        this.constructor = constructor;
        this.typeLink = BasicTypeLink.of(constructor.getDeclaringClass());
        this.parameterTypeLinks = stream(constructor.getParameterTypes()).map(BasicTypeLink::of).toList();
        this.parent = BasicTypeLink.of(constructor.getDeclaringClass());
    }

    /**
     * <p>Returns the constructor link for the given constructor.</p>
     *
     * @param constructor the constructor
     * @return the constructor link
     */
    public static BasicConstructorLink of(Constructor<?> constructor) {
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
    public <T> T invoke(Object... arguments) throws Throwable {
        try {
            //noinspection unchecked
            return (T) reflection().newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            //noinspection unchecked
            throw e.getCause();
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

    @Override
    public CtElement getCtElement() {
        if (element != null) {
            return element;
        }
        var parentElement = parent.getCtElement();
        if (parentElement == null) {
            return null;
        }
        element = (CtConstructor<?>) parentElement.getDirectChildren().stream()
            .filter(e ->
                e instanceof CtConstructor<?>
                    && stream(reflection().getParameterTypes()).map(BasicTypeLink::of).toList().equals(parameterTypeLinks)
            ).findFirst().orElse(null);
        return element;
    }
}
