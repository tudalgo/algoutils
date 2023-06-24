package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.FILE_NAMES;
import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.ROOT_PATH;
import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.deleteDirectory;
import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.loadClasses;

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

    @BeforeAll
    public void globalSetUp() throws IOException, ClassNotFoundException {
        Path[] paths = FILE_NAMES.stream().map(ROOT_PATH::resolve).toArray(Path[]::new);
        classes = loadClasses(sharedTempDir, paths);
    }

    @AfterAll
    public void globalTearDown() throws IOException {
        deleteDirectory(sharedTempDir);
    }

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


    @DisplayName("size()")
    @Test
    public void testSize() {
        Assertions.assertEquals(classes.size(), resource.size());
    }

    @DisplayName("isEmpty()")
    @Test
    public void testIsEmpty() {
        Assertions.assertFalse(resource.isEmpty());
    }

    @DisplayName("contents()")
    @Test
    public void testContents() {
        Map<String, String> expected = classes.values().stream()
            .collect(Collectors.toMap(it -> it.getKey().getName(), Map.Entry::getValue));
        Assertions.assertEquals(expected, resource.contents());
    }


    @DisplayName("getContent(String)")
    @Test
    public void testGetContent() {
        for (Map.Entry<Class<?>, String> entry : classes.values()) {
            Assertions.assertEquals(entry.getValue(), resource.getContent(entry.getKey().getName()));
        }
    }


    @DisplayName("contains(String)")
    @Test
    public void testContains() {
        for (Map.Entry<Class<?>, String> entry : classes.values()) {
            Assertions.assertTrue(resource.contains(entry.getKey().getName()));
        }
        Class<?>[] falseClasses = new Class<?>[]{
            Object.class,
            String.class,
            Integer.class,
        };
        for (Class<?> clazz : falseClasses) {
            Assertions.assertFalse(resource.contains(clazz.getName()));
        }
    }

    @DisplayName("iterator()")
    @Test
    public void testIterator() {
        Map<String, String> expected = classes.values().stream()
            .collect(Collectors.toMap(it -> it.getKey().getName(), Map.Entry::getValue));
        Map<String, String> actual = new HashMap<>();

        for (Map.Entry<String, String> entry : resource) {
            actual.put(entry.getKey(), entry.getValue());
        }

        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("stream()")
    @Test
    public void testStream() {
        Map<String, String> expected = classes.values().stream()
            .collect(Collectors.toMap(it -> it.getKey().getName(), Map.Entry::getValue));
        Map<String, String> actual = resource.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Assertions.assertEquals(expected, actual);
    }
}
