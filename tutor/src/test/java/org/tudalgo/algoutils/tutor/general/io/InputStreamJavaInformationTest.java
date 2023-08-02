package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.tudalgo.algoutils.tutor.general.TestUtils;

/**
 * Defines unit tests for {@link InputStreamJavaInformation}.
 *
 * @author Nhan Huynh
 */
@DisplayName("InputStreamJavaInformation")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InputStreamJavaInformationTest extends JavaInformationTest {

    @BeforeAll
    @Override
    public void setUp() {
        information = new InputStreamJavaInformation(
            () -> TestUtils.getResourceAsStream(TestUtils.ROOT.resolve(TEST_FILE))
        );
    }
}
