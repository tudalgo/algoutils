package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.*;

/**
 * Defines unit tests for {@link JavaSubmissionResource}.
 *
 * @author Nhan Huynh
 */
@DisplayName("JavaSubmissionResource")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JavaSubmissionResourceTest {

    /**
     * The shared temporary directory to store the compiled classes.
     */
    @TempDir
    private static Path sharedTempDir;

    /**
     * The classes in the resource.
     */
    private Map<Path, Map.Entry<Class<?>, String>> classes;

    /**
     * The resource to test.
     */
    private JavaResource resource;

    /**
     * Loads the classes in the resource.
     *
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if a class cannot be found
     */
    @BeforeAll
    public void globalSetUp() throws IOException, ClassNotFoundException {
        Path[] paths = FILE_NAMES.stream().map(ROOT_PATH::resolve).toArray(Path[]::new);
        classes = loadClasses(sharedTempDir, paths);
    }

    /**
     * Cleans up the shared temporary directory.
     *
     * @throws IOException if an I/O error occurs
     */
    @AfterAll
    public void globalTearDown() throws IOException {
        deleteDirectory(sharedTempDir);
    }

    /**
     * Sets up the resource to test.
     */
    @BeforeEach
    public void setUp() {
        resource = new JavaSubmissionResource(sharedTempDir);
    }

    @DisplayName("classNames()")
    @Test
    public void testClassNames() {
        Assertions.assertEquals(
            classes.values().stream().map(Map.Entry::getKey).map(Class::getName).collect(Collectors.toSet()),
            resource.classNames()
        );
    }

    /**
     * Tests {@link JavaSubmissionResource#size()} ()}.
     */
    @DisplayName("size()")
    @Test
    public void testSize() {
        Assertions.assertEquals(classes.size(), resource.size());
    }

    /**
     * Tests {@link JavaSubmissionResource#isEmpty()}.
     */
    @DisplayName("isEmpty()")
    @Test
    public void testIsEmpty() {
        Assertions.assertFalse(resource.isEmpty());
    }

    /**
     * Tests {@link JavaSubmissionResource#contents()}.
     */
    @DisplayName("contents()")
    @Test
    public void testContents() {
        Map<String, String> expected = classes.values().stream()
            .collect(Collectors.toMap(it -> it.getKey().getName(), Map.Entry::getValue));
        Assertions.assertEquals(expected.keySet(), resource.contents().keySet());
        Assertions.assertEquals(
            expected.values().stream().map(String::trim).toList(),
            resource.contents().values().stream().map(String::trim).toList()
        );
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
            classes.values().forEach(entry -> {
                String name = entry.getKey().getName();
                Assertions.assertEquals(entry.getValue().trim(), resource.get(name).trim());
            });
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
     * Defines unit tests for {@link JavaSubmissionResource#contains(String)}.
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
            classes.values().stream().map(Map.Entry::getKey).forEach(name -> Assertions.assertTrue(resource.contains(name.getName())));
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
     * Tests {@link JavaSubmissionResource#iterator()}.
     */
    @DisplayName("iterator()")
    @Test
    public void testIterator() {
        Iterable<Map.Entry<String, String>> it = () -> resource.iterator();
        Map<String, String> actual = StreamSupport.stream(it.spliterator(), false).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, String> expected = classes.values().stream().collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
        Assertions.assertEquals(expected.keySet(), actual.keySet());
        Assertions.assertEquals(
            expected.values().stream().map(String::trim).toList(),
            resource.contents().values().stream().map(String::trim).toList()
        );
    }

    /**
     * Tests {@link JavaSubmissionResource#stream()}.
     */
    @DisplayName("stream()")
    @Test
    public void testStream() {
        Map<String, String> actual = resource.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, String> expected = classes.values().stream().collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
        Assertions.assertEquals(expected.keySet(), actual.keySet());
        Assertions.assertEquals(
            expected.values().stream().map(String::trim).toList(),
            resource.contents().values().stream().map(String::trim).toList()
        );
    }
}
