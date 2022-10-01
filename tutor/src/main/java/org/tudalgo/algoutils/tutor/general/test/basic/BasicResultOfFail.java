package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.ResultOfFail;
import org.tudalgo.algoutils.tutor.general.test.actual.NoActual;
import org.tudalgo.algoutils.tutor.general.test.expected.Nothing;

public class BasicResultOfFail extends BasicResult<BasicResultOfFail, NoActual, BasicFail, Nothing> implements ResultOfFail<BasicResultOfFail, BasicFail> {

    public BasicResultOfFail(Environment environment, BasicFail test, Exception exception) {
        super(environment, test, null, exception);
    }
}
