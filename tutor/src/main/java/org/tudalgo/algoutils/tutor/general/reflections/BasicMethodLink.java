package org.tudalgo.algoutils.tutor.general.reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Arrays.stream;
import static org.tudalgo.algoutils.tutor.general.Utils.listOfCtParametersToTypeNames;
import static org.tudalgo.algoutils.tutor.general.Utils.listOfTypeLinksToTypeNames;

import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;
import spoon.reflect.declaration.CtMethod;

/**
 * A basic implementation of a {@link MethodLink method link}.
 */
public class BasicMethodLink extends BasicLink implements MethodLink, WithCtElement {

    private static final Map<Method, BasicMethodLink> INSTANCES = new HashMap<>();

    private final Method method;

    private final BasicTypeLink returnTypeLink;

    private final List<BasicTypeLink> parameterTypeLinks;

    private final BasicTypeLink parent;
    private CtMethod<?> element;

    private BasicMethodLink(Method method) {
        method.setAccessible(true);
        this.method = method;
        this.returnTypeLink = BasicTypeLink.of(method.getReturnType());
        this.parameterTypeLinks = stream(method.getParameterTypes()).map(BasicTypeLink::of).toList();
        this.parent = BasicTypeLink.of(method.getDeclaringClass());
    }

    public static BasicMethodLink of(Method method) {
        return INSTANCES.computeIfAbsent(method, BasicMethodLink::new);
    }

    /**
     * Returns a type link for the method retrieved by the given callable.
     *
     * @param callable the method to retrieve the method form
     * @return the type link
     */
    public static BasicMethodLink of(ObjectCallable<Method> callable) {
        try {
            return of(callable.call());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public List<BasicTypeLink> typeList() {
        return parameterTypeLinks;
    }

    @Override
    public BasicTypeLink returnType() {
        return returnTypeLink;
    }

    @Override
    public String identifier() {
        return method.getName();
    }

    @Override
    public <T> T invokeStatic(Object... args) throws Throwable {
        try {
            //noinspection unchecked
            return (T) method.invoke(null, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // TODO
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Override
    public <T> T invoke(Object instance, Object... args) throws Throwable {
        try {
            //noinspection unchecked
            return (T) method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // TODO
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Override
    public Method reflection() {
        return method;
    }

    @Override
    public BasicTypeLink type() {
        return returnTypeLink;
    }

    @Override
    public CtMethod<?> getCtElement() {
        if (element != null) {
            return element;
        }
        var parentElement = parent.getCtElement();
        if (parentElement == null) {
            return null;
        }
        var elements = parentElement.getDirectChildren().stream()
            // filter methods
            .filter(e -> e instanceof CtMethod<?>)
            // map to methods
            .map(e -> (CtMethod<?>) e)
            // filter fitting methods by name
            .filter(m -> reflection().getName().equals(m.getSimpleName()))
            // filter fitting methods by count of parameters
            .filter(c -> Objects.equals(
                c.getParameters().size(),
                parameterTypeLinks.size()
            ))
            // get fitting methods
            .toList();
        if (elements.size() > 1) {
            // Due to a bug in spoon, this filter does not work in all cases so there are two upstream filters!
            element = elements.stream()
                // filter fitting method
                .filter(m -> Objects.equals(
                    listOfCtParametersToTypeNames(m.getParameters()),
                    listOfTypeLinksToTypeNames(parameterTypeLinks)
                ))
                // get fitting method
                .findFirst().orElseThrow();
        } else {
            element = elements.get(0);
        }
        return element;
    }
}
