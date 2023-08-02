package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.*;
import org.tudalgo.algoutils.tutor.general.TestUtils;

import java.nio.file.Path;

/**
 * Defines unit tests for {@link JavaInformation}.
 *
 * @author Nhan Huynh
 */
@DisplayName("JavaInformation")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class JavaInformationTest {

    /**
     * The name of the class to test.
     */
    public static final String CLASS_NAME = "BankAccount";

    /**
     * The path to the test file.
     */
    public static final Path TEST_FILE = Path.of("%s.java".formatted(CLASS_NAME));

    /**
     * The Java information to test.
     */
    protected JavaInformation information;

    /**
     * Sets up the test.
     */
    @BeforeAll
    public abstract void setUp();

    /**
     * Tests {@link JavaInformation#getPackageName()}.
     */
    @DisplayName("getPackageName()")
    @Test
    public void testGetPackageName() {
        Assumptions.assumeTrue(information != null, "information not initialized");
        String packageName = information.getPackageName();
        Assertions.assertEquals(TestUtils.PACKAGE_NAME, packageName);
    }

    /**
     * Tests {@link JavaInformation#getClassName()}.
     */
    @DisplayName("getClassName()")
    @Test
    public void testGetClassName() {
        Assumptions.assumeTrue(information != null, "information not initialized");
        String className = information.getClassName();
        Assertions.assertEquals(CLASS_NAME, className);
    }

    /**
     * Tests {@link JavaInformation#getQualifiedName()}.
     */
    @DisplayName("getQualifiedName()")
    @Test
    public void testGetQualifiedName() {
        Assumptions.assumeTrue(information != null, "information not initialized");
        String qualifiedName = information.getQualifiedName();
        Assertions.assertEquals(TestUtils.PACKAGE_NAME + "." + CLASS_NAME, qualifiedName);
    }
}
