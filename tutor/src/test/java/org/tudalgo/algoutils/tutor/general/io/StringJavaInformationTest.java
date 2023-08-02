package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.tudalgo.algoutils.tutor.general.TestUtils;

/**
 * Defines unit tests for {@link StringJavaInformation}.
 *
 * @author Nhan Huynh
 */
@DisplayName("StringJavaInformation")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringJavaInformationTest extends JavaInformationTest {

    @BeforeAll
    @Override
    public void setUp() {
        information = new StringJavaInformation(TestUtils.getContent(TestUtils.ROOT.resolve(TEST_FILE)));
    }
}
