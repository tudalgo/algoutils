package org.tudalgo.algoutils.descriptors.types;

import java.util.function.Supplier;

/**
 * Descriptor for wildcard types.
 * Wildcard types are defined by their upper or lower bounds.
 * At the moment, wildcard types can have at most one bound, but that may change in the future.
 *
 * <p>
 * In the code snippet {@code public void m(List<?> l1, List<? extends Number> l2, List<? super Number> l3) ...},
 * {@code ?}, {@code ? extends Number} and {@code ? super Number} are wildcard types.
 * {@code ?} is unbounded, matching any type.
 * {@code ? extends Number} means the wildcard's upper bound is {@link Number}.
 * {@code ? super Number} means the wildcard's lower bound is {@link Number}.
 * </p>
 *
 * <p>
 * This interface acts as a descriptor for the Java reflection class {@link java.lang.reflect.WildcardType WildcardType}.
 * </p>
 */
public interface WildcardTypeDescriptor extends TypeDescriptor {

    /**
     * The type of bound for a described wildcard type.
     */
    enum BoundsType {
        /** An unbounded wildcard type, i.e., {@code ?}. */
        NONE("?"),
        /** A lower-bounded wildcard type, for example, {@code ? super Number}. */
        LOWER("? super "),
        /** An upper-bounded wildcard type, for example, {@code ? extends Number}. */
        UPPER("? extends ");

        final String namePrefix;

        BoundsType(String namePrefix) {
            this.namePrefix = namePrefix;
        }
    }

    /**
     * Returns the lower bounds of the described wildcard type.
     *
     * @return the lower bounds of the described wildcard type
     */
    TypeDescriptor[] getLowerBounds();

    /**
     * Returns the upper bounds of the described wildcard type.
     *
     * @return the upper bounds of the described wildcard type
     */
    TypeDescriptor[] getUpperBounds();

    @Override
    default boolean isGeneric() {
        return true;
    }

    /**
     * Creates a new {@link WildcardTypeDescriptor} of the given type, optionally with bounds.
     *
     * @param boundsType the type of bound for the wildcard type
     * @param bounds     the bounds of the wildcard type
     * @return the new {@link TypeVariableDescriptor}
     */
    static WildcardTypeDescriptor of(BoundsType boundsType, TypeDescriptor... bounds) {
        return of(boundsType, () -> bounds);
    }

    /**
     * Creates a new {@link WildcardTypeDescriptor} of the given type, optionally with bounds.
     *
     * @param boundsType the type of bound for the wildcard type
     * @param bounds     a supplier for the bounds of the wildcard type
     * @return the new {@link WildcardTypeDescriptor}
     */
    static WildcardTypeDescriptor of(BoundsType boundsType, Supplier<TypeDescriptor[]> bounds) {
        return new WildcardTypeDescriptorImpl(boundsType,
            boundsType == BoundsType.LOWER ? bounds : () -> new TypeDescriptor[0],
            boundsType == BoundsType.UPPER ? bounds : () -> new TypeDescriptor[0]);
    }
}
