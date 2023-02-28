package org.tudalgo.algoutils.tutor.general.reflections;

import org.apache.maven.api.annotations.Nullable;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtTypeInformation;
import spoon.reflect.declaration.CtTypedElement;
import spoon.reflect.path.CtRole;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.stream;

public class BasicConstructorLink extends BasicLink implements ConstructorLink, WithCtElement {

    private static final Map<Constructor<?>, BasicConstructorLink> INSTANCES = new HashMap<>();

    private final Constructor<?> constructor;
    private final TypeLink typeLink;
    private final List<BasicTypeLink> parameterTypeLinks;

    /**
     * The spoon element of this link.
     */
    private @Nullable CtConstructor<?> element;

    private BasicConstructorLink(Constructor<?> constructor) {
        this.constructor = constructor;
        this.typeLink = BasicTypeLink.of(constructor.getDeclaringClass());
        this.parameterTypeLinks = stream(constructor.getParameterTypes()).map(BasicTypeLink::of).toList();
    }

    /**
     * <p>Returns the constructor link for the given constructor.</p>
     *
     * @param constructor the constructor
     *
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

    @Override
    public CtConstructor<?> getCtElement() {
        if (element != null) return element;
        Set<CtConstructor<?>> constructors = ((BasicTypeLink) typeLink).getCtElement().getValueByRole(CtRole.CONSTRUCTOR);
        List<String> parameterTypes = stream(constructor.getTypeParameters()).map(TypeVariable::getName).toList();
        element = constructors.stream().filter(c -> c.getParameters().stream()
                .map(CtTypedElement::getType)
                .map(CtTypeInformation::getTypeErasure)
                .map(CtTypeInformation::getQualifiedName)
                .toList()
                .equals(parameterTypes)
            )
            .findFirst()
            .orElseThrow();
        return element;
    }
}
