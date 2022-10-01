package org.tudalgo.algoutils.tutor.general.test.expected;

/**
 * <p>A type representing no expected behavior.</p>
 *
 * @author Dustin Glaser
 */
public final class Nothing implements Expected {

    // no instantiation allowed
    private Nothing() {

    }

    @Override
    public Object behavior() {
        return null;
    }
}
