package org.tudalgo.algoutils.descriptors.generic;

import org.tudalgo.algoutils.descriptors.TypeDescriptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Descriptor for wildcard types.
 * Wildcard types are defined by their upper or lower bounds.
 * At the moment, wildcard types can have at most one bound, but that may change in the future.
 * <p>
 * In the code snippet {@code public void m(List<?> l1, List<? extends Number> l2, List<? super Number> l3) ...},
 * {@code ?}, {@code ? extends Number} and {@code ? super Number} are wildcard types.
 * {@code ?} is unbounded, matching any type.
 * {@code ? extends Number} means the wildcard's upper bound is {@link Number}.
 * {@code ? super Number} means the wildcard's lower bound is {@link Number}.
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.WildcardType WildcardType}.
 */
public interface WildcardTypeDescriptor extends GenericTypeDescriptor {

    /**
     * The type of bound for a described wildcard type.
     */
    enum BoundsType {
        /** An unbounded wildcard type, i.e., {@code ?}. */
        NONE,
        /** A lower-bounded wildcard type, for example, {@code ? super Number}. */
        LOWER,
        /** An upper-bounded wildcard type, for example, {@code ? extends Number}. */
        UPPER
    }

    /**
     * Returns the lower bounds of the described wildcard type.
     *
     * @return the lower bounds of the described wildcard type
     */
    List<TypeDescriptor> getLowerBounds();

    /**
     * Returns the upper bounds of the described wildcard type.
     *
     * @return the upper bounds of the described wildcard type
     */
    List<TypeDescriptor> getUpperBounds();

    /**
     * Creates a new {@link WildcardTypeDescriptor} of the given type, optionally with bounds.
     *
     * @param boundsType the type of bound for the wildcard type
     * @param bounds     the bounds of the wildcard type
     * @return the new {@link WildcardTypeDescriptor}
     */
    static WildcardTypeDescriptor of(BoundsType boundsType, TypeDescriptor... bounds) {
        return new WildcardTypeDescriptorImpl(boundsType,
            boundsType == BoundsType.LOWER ? Arrays.asList(bounds) : Collections.emptyList(),
            boundsType == BoundsType.UPPER ? Arrays.asList(bounds) : Collections.emptyList());
    }
}
