package org.tudalgo.algoutils.tutor.general;

public class Utils {

    private static final Object NONE = new Object();

    public static <T> T none() {
        //noinspection unchecked
        return (T) NONE;
    }
}
