package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Defines unit tests for {@link JavaSubmissionResource}.
 *
 * @author Nhan Huynh
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("JavaSubmissionResource")
public class JavaSubmissionResourceTest {

    /**
     * The root path of the resource.
     */
    private static final Path ROOT_PATH = Path.of("org", "tudalgo", "algoutils", "tutor", "general", "io");

    /**
     * The names of the files in the resource.
     */
    private static final List<Path> FILE_NAMES = Stream.of("BankAccount", "Car", "Person", "Rectangle", "Student")
        .map(it -> it + ".java")
        .map(Path::of)
        .toList();

    /**
     * The shared temporary directory to store the compiled classes.
     */
    @TempDir
    private static Path sharedTempDir;

    /**
     * The classes in the resource.
     */
    private final Map<Path, Class<?>> classes = new HashMap<>(FILE_NAMES.size());

    /**
     * The contents of the files in the resource.
     */
    private final Map<Path, String> contents = new HashMap<>(FILE_NAMES.size());

    /**
     * The resource to test.
     */
    private JavaResource resource;

    @BeforeAll
    public void globalSetUp() throws IOException, ClassNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        for (Path file : FILE_NAMES) {
            Path path = ROOT_PATH.resolve(file);
            try (InputStream inputStream = classLoader.getResourceAsStream(path.toString())) {
                assert inputStream != null;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String content = reader.lines().collect(Collectors.joining("\n"));
                    Path tmp = sharedTempDir.resolve(file);
                    Files.write(tmp, content.getBytes());
                    compiler.run(null, null, null, tmp.toString());
                    String className = JavaResource.toClassName(path);
                    classes.put(file, Class.forName(className));
                    contents.put(file, content);
                }
            }
        }
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
            classes.values().stream().map(Class::getName).collect(Collectors.toSet()),
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
        Map<String, String> expected = new HashMap<>(FILE_NAMES.size());
        for (Path file : FILE_NAMES) {
            expected.put(classes.get(file).getName(), contents.get(file));
        }
        Assertions.assertEquals(expected, resource.contents());
    }

    @DisplayName("getContent(String)")
    @Test
    public void testGet() {
        for (Path file : FILE_NAMES) {
            Assertions.assertEquals(contents.get(file), resource.getContent(classes.get(file).getName()));
        }
    }


    @DisplayName("contains(String)")
    @Test
    public void testContains() {
        for (Path file : FILE_NAMES) {
            Assertions.assertTrue(resource.contains(classes.get(file).getName()));
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
        Iterator<Map.Entry<String, String>> it = resource.iterator();
        Set<String> values = classes.values().stream().map(Class::getName).collect(Collectors.toSet());

        Assertions.assertTrue(it.hasNext());
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            Assertions.assertTrue(values.contains(entry.getKey()));
            Assertions.assertTrue(contents.values().stream().anyMatch(entry.getValue()::equals));
        }
    }

    @DisplayName("stream()")
    @Test
    public void testStream() {
        Assertions.assertTrue(resource.stream().findAny().isPresent());
        Set<String> values = classes.values().stream().map(Class::getName).collect(Collectors.toSet());
        Assertions.assertTrue(resource.stream().anyMatch(entry -> values.contains(entry.getKey()) &&
            contents.values().stream().anyMatch(entry.getValue()::equals)));
    }
}
