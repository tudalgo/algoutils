package org.tudalgo.algoutils.tutor.general;

import org.tudalgo.algoutils.tutor.general.basic.BasicEnvironment;

public final class AlgoUtils {

    private final Environment environment = BasicEnvironment.getInstance();

    public static AlgoUtils getInstance() {
        return new AlgoUtils();
    }

    public Environment getEnvironment() {
        return environment;
    }
}
