package org.tudalgo.algoutils.tutor.general.reflections;

import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

public class BasicConstructorLink extends BasicLink implements ConstructorLink, WithCtElement {

    private static final Map<Constructor<?>, BasicConstructorLink> INSTANCES = new HashMap<>();

    private final Constructor<?> constructor;
    private final TypeLink typeLink;
    private final List<BasicTypeLink> parameterTypeLinks;

    /**
     * The class type link of this constructor link.
     */
    private final BasicTypeLink parent;

    /**
     * The CtElement of this link in the Spoon world.
     */
    private CtConstructor<?> element;

    /**
     * The imports used in this link.
     */
    private Set<TypeLink> imports;

    private BasicConstructorLink(Constructor<?> constructor) {
        this.constructor = constructor;
        this.typeLink = BasicTypeLink.of(constructor.getDeclaringClass());
        this.parameterTypeLinks = stream(constructor.getParameterTypes()).map(BasicTypeLink::of).toList();
        this.parent = BasicTypeLink.of(constructor.getDeclaringClass());
    }

    public BasicConstructorLink(Constructor<?> constructor, BasicMethodLink element) {
        this(constructor);
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
    public CtElement getCtElement() {
        if (element != null) {
            return element;
        }
        return element = (CtConstructor<?>) parent.getCtElement()
            .getElements(e -> e instanceof CtConstructor<?> c && c.getSignature().equals(toShortSignature(reflection())))
            .get(0);
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
