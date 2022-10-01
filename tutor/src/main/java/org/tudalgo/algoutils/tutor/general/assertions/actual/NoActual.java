package org.tudalgo.algoutils.tutor.general.assertions.actual;

/**
 * <p>A type representing no actual behavior.</p>
 *
 * @author Dustin Glaser
 */
public final class NoActual implements Actual {

    // no instantiation allowed
    private NoActual() {

    }

    @Override
    public Object behavior() {
        return null;
    }
}
