package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.*;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Defines unit tests for {@link JavaStdlibResource}.
 *
 * @author Nhan Huynh
 */
public class JavaStdlibResourceTest {

    /**
     * The resource to test.
     */
    private JavaResource resource;

    /**
     * Sets up the resource to test.
     */
    @BeforeEach
    public void setUp() {
        resource = new JavaStdlibResource();
    }

    /**
     * Tests {@link JavaStdlibResource#classNames()}.
     */
    @DisplayName("classNames")
    @Test
    public void testClassNames() {
        Assertions.assertFalse(resource.classNames().isEmpty());
    }

    /**
     * Tests {@link JavaStdlibResource#size()}.
     */
    @DisplayName("size()")
    @Test
    public void testSize() {
        Assertions.assertTrue(resource.size() > 0);
    }

    /**
     * Tests {@link JavaStdlibResource#isEmpty()}.
     */
    @DisplayName("isEmpty()")
    @Test
    public void testIsEmpty() {
        Assertions.assertFalse(resource.isEmpty());
    }

    /**
     * Tests {@link JavaStdlibResource#contents()}.
     */
    @DisplayName("contents()")
    @Test
    public void testContents() {
        Assertions.assertTrue(resource.contents().size() > 0);
    }

    /**
     * Defines unit tests for {@link JavaStdlibResource#get(String)}.
     */
    @DisplayName("get(String)")
    @Nested
    public class GetContentTest {

        /**
         * Tests with a class that exists.
         */
        @DisplayName("Class exists")
        @Test
        public void testNormalBehaviour() {
            Assertions.assertNotNull(resource.get(Object.class.getName()));
        }

        /**
         * Tests with a class that does not exist.
         */
        @DisplayName("Class does not exist")
        @Test
        public void testExceptionalBehaviour() {
            Assertions.assertThrows(NoSuchElementException.class, () -> resource.get("myPackage.MyClass"));
        }
    }

    /**
     * Defines unit tests for {@link JavaStdlibResource#contains(String)}.
     */
    @DisplayName("contains(String)")
    @Nested
    public class ContainsTest {

        /**
         * Tests with a class that exists.
         */
        @DisplayName("Class exists")
        @Test
        public void testExists() {
            Assertions.assertTrue(resource.contains(Object.class.getName()));
        }

        /**
         * Tests with a class that does not exist.
         */
        @DisplayName("Class does not exist")
        @Test
        public void testDoesNotExist() {
            Assertions.assertFalse(resource.contains("myPackage.MyClass"));
        }
    }

    /**
     * Tests {@link JavaStdlibResource#iterator()}.
     */
    @DisplayName("iterator()")
    @Test
    public void testIterator() {
        Iterator<Map.Entry<String, String>> it = resource.iterator();
        Assertions.assertTrue(it.hasNext());
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (entry.getKey().equals(Object.class.getName())) {
                return;
            }
        }
        Assertions.fail("Iterator did not contain " + Object.class.getName());
    }

    /**
     * Tests {@link JavaStdlibResource#stream()}.
     */
    @DisplayName("stream()")
    @Test
    public void testStream() {
        Assertions.assertTrue(resource.stream().findAny().isPresent());
        Assertions.assertTrue(resource.stream().anyMatch(entry -> entry.getKey().equals(Object.class.getName())));
    }
}
