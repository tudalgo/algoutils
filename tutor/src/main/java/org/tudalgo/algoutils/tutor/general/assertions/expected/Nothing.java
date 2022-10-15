package org.tudalgo.algoutils.tutor.general.assertions.expected;

import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

/**
 * <p>A type representing no expected behavior.</p>
 *
 * @author Dustin Glaser
 */
public final class Nothing implements Actual, Expected {

    private static final Nothing INSTANCE = new Nothing();

    // no instantiation allowed
    private Nothing() {

    }

    @Override
    public Object behavior() {
        return null;
    }

    @Override
    public boolean successful() {
        return false;
    }

    @Override
    public boolean display() {
        return false;
    }

    @Override
    public String string(Stringifier stringifier) {
        return Actual.super.string(stringifier);
    }

    public static Nothing nothing() {
        return INSTANCE;
    }
}
