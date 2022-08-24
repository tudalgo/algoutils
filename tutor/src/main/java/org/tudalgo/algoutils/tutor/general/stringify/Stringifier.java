package org.tudalgo.algoutils.tutor.general.stringify;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A stringifier is a function for transforming objects to a string representations.
 *
 * @author Dustin Glaser
 */
@FunctionalInterface
public interface Stringifier {

    /**
     * Returns a new stringifier using the given stringifier as an alternative to the current stringifier if the current stringifier cannot create a string representation.
     *
     * @param stringifier the stringifier to use as an alternative to the current stringifier
     * @return the new stringifier using the given stringifier as an alternative to the current stringifier
     */
    default Stringifier orElse(Stringifier stringifier) {
        return s -> {
            var string = stringify(s);
            if (string == null) {
                return stringifier.stringify(s);
            }
            return string;
        };
    }

    /**
     * <p>Creates a string representation of the given object or <code>null</code>, if no string representation can be created.</p>
     * <p>This method is a shortcut for {@link #stringifyOrElseNull(Object)}</p>
     *
     * @param object the object to create a string representation for
     * @return the string representation of the given object or <code>null</code>
     */
    default String stringify(Object object) {
        return stringifyOrElseNull(object);
    }

    /**
     * Creates a string representation of the given object or the default string supplied by the given suuplier, if no string representation can be created.
     *
     * @param object                the object to create a string representation for
     * @param defaultStringSupplier the supplier to use if no string representation can be created
     * @return the string representation of the given object
     */
    default String stringifyOrElseDefault(Object object, Supplier<String> defaultStringSupplier) {
        var string = stringify(object);
        if (string == null) {
            return defaultStringSupplier.get();
        }
        return string;
    }

    /**
     * Creates a string representation of the given object or use the given default string, if no string representation can be created.
     *
     * @param object        the object to create a string representation for
     * @param defaultString the default string to use if no string representation can be created
     * @return the string representation of the given object
     */
    default String stringifyOrElseDefault(Object object, String defaultString) {
        var string = stringify(object);
        if (string == null) {
            return defaultString;
        }
        return string;
    }

    /**
     * Creates a string representation of the given object or <code>null</code>, if no string representation can be created.
     *
     * @param object the object to create a string representation for
     * @return the string representation of the given object or <code>null</code>
     */
    String stringifyOrElseNull(Object object);

    /**
     * Creates a string representation of the given object or use {@link #toString()}, if no string representation can be created.
     *
     * @param object the object to create a string representation for
     * @return the string representation of the given object
     */
    default String stringifyOrElseToString(Object object) {
        var string = stringify(object);
        if (string == null) {
            return Objects.toString(object);
        }
        return string;
    }
}
