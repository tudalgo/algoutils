package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.tudalgo.algoutils.tutor.general.TestUtils;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Defines unit tests for {@link SubmissionJavaResource}.
 *
 * @author Nhan Huynh
 */
@DisplayName("JavaSubmissionResource")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubmissionJavaResourceTest {

    public static final Set<String> CLASS_NAMES = Set.of("BankAccount", "Car");

    /**
     * The shared temporary directory to store the compiled classes.
     */
    @TempDir
    private static Path sharedTempDir;

    /**
     * The classes in the resource.
     */
    private Map<String, String> classes;

    /**
     * The resource to test.
     */
    private JavaResource resource;

    /**
     * Loads the classes in the resource.
     *
     * @throws UncheckedIOException if an I/O error occurs
     */
    @BeforeAll
    public void globalSetUp() throws UncheckedIOException {
        CLASS_NAMES.stream()
            .map(name -> TestUtils.ROOT.resolve("%s.java".formatted(name)))
            .forEach(path -> TestUtils.write(sharedTempDir, path));
        classes = CLASS_NAMES.stream()
            .map(name -> {
                String qualifiedName = TestUtils.PACKAGE_NAME + "." + name;
                Path path = TestUtils.ROOT.resolve("%s.java".formatted(name));
                String sourceCode = TestUtils.getContent(path);
                return Map.entry(qualifiedName, sourceCode);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Cleans up the shared temporary directory.
     *
     * @throws UncheckedIOException if an I/O error occurs
     */
    @AfterAll
    public void globalTearDown() throws UncheckedIOException {
        TestUtils.deleteDirectory(sharedTempDir);
    }

    /**
     * Sets up the resource to test.
     */
    @BeforeEach
    public void setUp() {
        resource = new SubmissionJavaResource(sharedTempDir);
    }

    /**
     * Tests {@link SubmissionJavaResource#classNames()}.
     */
    @DisplayName("classNames()")
    @Test
    public void testClassNames() {
        Assertions.assertEquals(classes.keySet(), resource.classNames());
    }

    /**
     * Tests {@link SubmissionJavaResource#size()}.
     */
    @DisplayName("size()")
    @Test
    public void testSize() {
        Assertions.assertEquals(classes.size(), resource.size());
    }

    /**
     * Tests {@link SubmissionJavaResource#isEmpty()}.
     */
    @DisplayName("isEmpty()")
    @Test
    public void testIsEmpty() {
        Assertions.assertFalse(resource.isEmpty());
    }

    /**
     * Tests {@link SubmissionJavaResource#contents()}.
     */
    @DisplayName("contents()")
    @Test
    public void testContents() {
        Assertions.assertEquals(classes.keySet(), resource.contents().keySet());
        Assertions.assertEquals(
            classes.values().stream().map(String::trim).toList(),
            resource.contents().values().stream().map(String::trim).toList()
        );
    }

    /**
     * Defines unit tests for {@link SubmissionJavaResource#get(String)}.
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
            classes.forEach(
                (name, sourceCode) -> Assertions.assertEquals(sourceCode.trim(), resource.get(name).trim())
            );
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
     * Defines unit tests for {@link SubmissionJavaResource#contains(String)}.
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
            classes.keySet().forEach(name -> Assertions.assertTrue(resource.contains(name)));
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
     * Tests {@link SubmissionJavaResource#iterator()}.
     */
    @DisplayName("iterator()")
    @Test
    public void testIterator() {
        Iterable<Map.Entry<String, String>> it = () -> resource.iterator();
        Map<String, String> actual = StreamSupport.stream(it.spliterator(), false)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Assertions.assertEquals(classes.keySet(), actual.keySet());
        Assertions.assertEquals(
            classes.values().stream().map(String::trim).toList(),
            resource.contents().values().stream().map(String::trim).toList()
        );
    }

    /**
     * Tests {@link SubmissionJavaResource#stream()}.
     */
    @DisplayName("stream()")
    @Test
    public void testStream() {
        Map<String, String> actual = resource.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Assertions.assertEquals(classes.keySet(), actual.keySet());
        Assertions.assertEquals(
            classes.values().stream().map(String::trim).toList(),
            resource.contents().values().stream().map(String::trim).toList()
        );
    }
}
