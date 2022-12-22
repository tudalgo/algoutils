package org.tudalgo.algoutils.tutor.general.reflections;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtTypeReference;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A basic implementation of a link {@link WithImports with imports}
 *
 * @author Nhan Huynh
 */
public abstract class BasicLinkWithImports extends BasicLink implements WithCtElement, WithImports {

    /**
     * The imports used in this link.
     */
    private Set<TypeLink> imports;

    @Override
    @SuppressWarnings("deprecation")
    public Set<TypeLink> imports() {
        if (imports != null) {
            return imports;
        }
        return imports = getCtElement().getReferencedTypes().stream()
            .map(CtElement::getReferencedTypes)
            .flatMap(Collection::stream)
            .distinct()
            // Remove null references
            .filter(Predicate.not(it -> it.getSimpleName().equals("<nulltype>")))
            // Deprecated in Spoon, since we are mapping to TypeLink, we can safely ignore this
            .map(CtTypeReference::getActualClass)
            .map(BasicTypeLink::of)
            .collect(Collectors.toUnmodifiableSet());
    }

}
