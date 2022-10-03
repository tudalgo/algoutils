package org.tudalgo.algoutils.tutor.general.match;

/**
 * <p>A type that can be identified by an identifier.</p>
 *
 * @author Dustin Glaser
 */
public interface Identifiable extends Stringifiable {

    /**
     * <p>Returns the identifier of this identifiable object.</p>
     *
     * @return the identifier
     */
    String identifier();

    @Override
    default String string() {
        return identifier();
    }
}
