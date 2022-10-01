package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Fail;
import org.tudalgo.algoutils.tutor.general.test.actual.NoActual;
import org.tudalgo.algoutils.tutor.general.test.expected.Nothing;

public class BasicFail extends BasicTest<BasicFail, Nothing, BasicResultOfFail, NoActual> implements Fail<BasicFail, BasicResultOfFail> {

    public BasicFail(Environment environment) {
        super(environment, null);
    }
}
