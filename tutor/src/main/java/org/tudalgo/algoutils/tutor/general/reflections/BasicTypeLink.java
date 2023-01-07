package org.tudalgo.algoutils.tutor.general.reflections;

import org.tudalgo.algoutils.tutor.general.ResourceUtils;
import org.tudalgo.algoutils.tutor.general.SpoonUtils;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static org.tudalgo.algoutils.tutor.general.SpoonUtils.collectConstructorCallImports;
import static org.tudalgo.algoutils.tutor.general.SpoonUtils.collectFieldImports;
import static org.tudalgo.algoutils.tutor.general.SpoonUtils.collectImports;
import static org.tudalgo.algoutils.tutor.general.SpoonUtils.collectMethodInvocationImports;
import static org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers.identical;

/**
 * A basic implementation of a {@link TypeLink type link}.
 */
public class BasicTypeLink extends BasicLink implements TypeLink, WithCtElement, WithResource {

    private static final Map<Class<?>, BasicTypeLink> INSTANCES = new HashMap<>();

    private final Class<?> type;

    /**
     * The imports used in this link.
     */
    private Set<TypeLink> imports;

    /**
     * The indicator if the source code is available for this type link.
     */
    private final boolean isResourceAvailable;

    private BasicTypeLink(Class<?> type) {
        this.type = type;
        this.isResourceAvailable = ResourceUtils.isResourceAvailable(type);
    }

    public static BasicTypeLink of(Class<?> type) {
        return INSTANCES.computeIfAbsent(type, BasicTypeLink::new);
    }

    @Override
    public Collection<BasicTypeLink> interfaces() {
        if (interfaces.isEmpty()) {
            stream(type.getInterfaces()).map(BasicTypeLink::of).forEach(interfaces::add);
        }
        return unmodifiableInterfaces;
    }

    @Override
    public TypeLink superType() {
        return of(type.getSuperclass());
    }

    private final List<BasicFieldLink> fields = new LinkedList<>(), unmodifiableFields = unmodifiableList(fields);

    @Override
    public List<BasicFieldLink> getFields() {
        if (fields.isEmpty()) {
            stream(type.getDeclaredFields()).map(BasicFieldLink::of).forEach(fields::add);
        }
        return unmodifiableFields;
    }

    @Override
    public Collection<BasicConstructorLink> getConstructors() {
        if (constructors.isEmpty()) {
            stream(type.getDeclaredConstructors()).map(BasicConstructorLink::of).forEach(constructors::add);
        }
        return unmodifiableConstructors;
    }

    @Override
    public Collection<BasicEnumConstantLink> getEnumConstants() {
        if (!type.isEnum()) {
            return Collections.emptyList();
        }
        @SuppressWarnings("unchecked")
        var enumType = (Class<Enum<?>>) type;
        if (enums.isEmpty()) {
            stream(enumType.getEnumConstants()).map(BasicEnumConstantLink::of).forEach(enums::add);
        }
        return unmodifiableEnums;
    }

    private final List<BasicTypeLink> interfaces = new ArrayList<>(), unmodifiableInterfaces = unmodifiableList(interfaces);

    @Override
    public String identifier() {
        return type.getSimpleName();
    }

    @Override
    public Class<?> reflection() {
        return type;
    }

    @Override
    public Kind kind() {
        if (type.isEnum()) {
            return Kind.ENUM;
        } else if (type.isInterface()) {
            return Kind.INTERFACE;
        } else if (type.isPrimitive()) {
            return Kind.PRIMITIVE;
        } else if (type.isArray()) {
            return Kind.ARRAY;
        } else if (type.isAnnotation()) {
            return Kind.ANNOTATION;
        } else if (type.isRecord()) {
            return Kind.RECORD;
        } else {
            return Kind.CLASS;
        }
    }

    private final List<BasicMethodLink> methods = new LinkedList<>(), unmodifiableMethods = unmodifiableList(methods);

    @Override
    public Collection<BasicMethodLink> getMethods() {
        if (methods.isEmpty()) {
            stream(type.getDeclaredMethods()).map(BasicMethodLink::of).forEach(methods::add);
        }
        return unmodifiableMethods;
    }


    private final List<BasicConstructorLink> constructors = new LinkedList<>(), unmodifiableConstructors = unmodifiableList(constructors);


    private final List<BasicEnumConstantLink> enums = new LinkedList<>(), unmodifiableEnums = unmodifiableList(enums);

    private CtType<?> element;

    @Override
    public boolean isResourceAvailable() {
        return isResourceAvailable;
    }

    @Override
    public CtElement getCtElement() {
        if (element != null) {
            return element;
        }
        if (!isResourceAvailable()) {
            return null;
        }
        var source = ResourceUtils.getTypeContent(type);
        return element = SpoonUtils.getCtElementForSourceCode(source, CtType.class, identical(type.getSimpleName()));
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
        // Super classes
        return imports;
    }

    @Override
    public Set<TypeLink> imports(boolean recursive) {
        if (imports != null) {
            return imports;
        }
        if (!isResourceAvailable()) {
            return imports = Set.of();
        }
        imports = recursive ? collectImportsRecursively(getCtElement()) : collectImports(getCtElement());
        return imports = Collections.unmodifiableSet(imports);
    }

}
