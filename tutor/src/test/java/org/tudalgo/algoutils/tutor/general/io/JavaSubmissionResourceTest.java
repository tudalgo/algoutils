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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.FILE_NAMES;
import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.ROOT_PATH;
import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.loadClasses;

/**
 * Defines unit tests for {@link JavaSubmissionResource}.
 *
 * @author Nhan Huynh
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("JavaSubmissionResource")
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
        try (Stream<Path> paths = Files.walk(sharedTempDir)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(path);
            }
        }
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
        Assertions.assertIterableEquals(expected.entrySet(), resource);
    }

    @DisplayName("stream()")
    @Test
    public void testStream() {
        Map<String, String> expected = classes.values().stream()
            .collect(Collectors.toMap(it -> it.getKey().getName(), Map.Entry::getValue));
        Iterator<Map.Entry<String, String>> it = expected.entrySet().iterator();
        Iterator<Map.Entry<String, String>> actual = resource.stream().iterator();
        while (it.hasNext()) {
            Assertions.assertEquals(it.next(), actual.next());
        }
        Assertions.assertFalse(actual.hasNext());
    }


}
