package org.tudalgo.algoutils.tutor.general.reflections;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.stream;
import static org.tudalgo.algoutils.tutor.general.ResourceUtils.toShortSignature;
import static org.tudalgo.algoutils.tutor.general.SpoonUtils.collectConstructorCallImports;
import static org.tudalgo.algoutils.tutor.general.SpoonUtils.collectFieldImports;
import static org.tudalgo.algoutils.tutor.general.SpoonUtils.collectImports;
import static org.tudalgo.algoutils.tutor.general.SpoonUtils.collectMethodInvocationImports;

/**
 * A basic implementation of a {@link MethodLink method link}.
 */
public class BasicMethodLink extends BasicLink implements MethodLink, WithCtElement {

    private static final Map<Method, BasicMethodLink> INSTANCES = new HashMap<>();

    private final Method method;

    private final BasicTypeLink returnTypeLink;

    private final List<BasicTypeLink> parameterTypeLinks;

    /**
     * The class type link of this method link.
     */
    private final BasicTypeLink parent;

    /**
     * The imports used in this link.
     */
    private Set<TypeLink> imports;

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
    public <T> T invokeStatic(Object... args) throws Exception {
        try {
            //noinspection unchecked
            return (T) method.invoke(null, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // TODO
        } catch (InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Override
    public <T> T invoke(Object instance, Object... args) throws Exception {
        try {
            //noinspection unchecked
            return (T) method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // TODO
        } catch (InvocationTargetException e) {
            throw (Exception) e.getCause();
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

    private CtMethod<?> element;

    @Override
    public CtMethod<?> getCtElement() {
        if (element != null) {
            return element;
        }
        return element = (CtMethod<?>) parent.getCtElement().getElements(f -> f instanceof CtMethod<?> m && m.getSignature().equals(toShortSignature(reflection()))).get(0);
    }

    /**
     * Collects all the imports used in this link.
     *
     * @param element the element to collect the imports from
     *
     * @return the collected imports
     */
    private Set<TypeLink> collectImportsRecursively(CtElement element) {
        // Current imports
        Set<TypeLink> imports = collectImports(element);
        // Do not visit the same element twice.
        Set<Link> visited = new HashSet<>(imports);
        // Imports of the constructor call
        imports.addAll(collectConstructorCallImports(element, visited));
        // Imports of the method invocation
        imports.addAll(collectMethodInvocationImports(element, visited));
        // Imports of fields
        imports.addAll(collectFieldImports(element, visited));
        return imports;
    }

    @Override
    public Set<TypeLink> imports(boolean recursive) {
        if (imports != null) {
            return imports;
        }
        if (!parent.isResourceAvailable()) {
            return imports = Set.of();
        }
        imports = recursive ? collectImportsRecursively(getCtElement()) : collectImports(getCtElement());
        return imports = Collections.unmodifiableSet(imports);
    }

}
