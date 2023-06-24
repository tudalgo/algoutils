package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.FILE_NAMES;
import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.ROOT_PATH;
import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.deleteDirectory;
import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.loadClasses;

/**
 * Defines unit tests for {@link ResourceUtils}.
 *
 * @author Nhan Huynh
 */
@DisplayName("ResourceUtils")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResourceUtilsTest {

    /**
     * The shared temporary directory to store the compiled classes.
     */
    @TempDir
    private static Path sharedTempDir;

    /**
     * The classes in the resource.
     */
    private Map<Path, Map.Entry<Class<?>, String>> classes;

    @BeforeAll
    public void globalSetUp() throws IOException, ClassNotFoundException {
        Path[] paths = FILE_NAMES.stream().map(ROOT_PATH::resolve).toArray(Path[]::new);
        classes = loadClasses(sharedTempDir, paths);
    }

    @AfterAll
    public void globalTearDown() throws IOException {
        deleteDirectory(sharedTempDir);
    }

    @DisplayName("getPackageName(String)")
    @Nested
    public class GetPackageNameTest {

        @DisplayName("Normal behavior: Package exists")
        @Test
        public void testExists() {
            for (Map.Entry<Class<?>, String> entry : classes.values()) {
                String actual = ResourceUtils.getPackageName(entry.getValue());
                String expected = entry.getKey().getPackageName();
                Assertions.assertEquals(expected, actual);
            }
        }

        @DisplayName("Exceptional behavior: Package does not exist")
        @Test
        public void testNotExists() {
            String sourceCode = """
                public class NotExists {
                }
                """;
            Assertions.assertEquals("", ResourceUtils.getPackageName(sourceCode));
        }
    }

    @DisplayName("getClassName(String)")
    @Nested
    public class GetClassNameTest {

        @DisplayName("Normal behavior: Class exists")
        @Test
        public void testExists() {
            for (Map.Entry<Class<?>, String> entry : classes.values()) {
                String actual = ResourceUtils.getClassName(entry.getValue());
                String expected = entry.getKey().getSimpleName();
                Assertions.assertEquals(expected, actual);
            }
        }

        @DisplayName("Exceptional behavior: Package does not exist")
        @Test
        public void testNotExists() {
            String sourceCode = """
                public NotExists {
                }
                """;
            Throwable throwable = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ResourceUtils.getClassName(sourceCode)
            );
            Assertions.assertEquals(
                "No class declaration found in the given source code",
                throwable.getMessage()
            );
        }
    }

    @DisplayName("getQualifiedClassName(String)")
    @Nested
    public class GetQualifiedClassNameTest {

        @DisplayName("Normal behavior: Package exists")
        @Test
        public void testExists() {
            for (Map.Entry<Class<?>, String> entry : classes.values()) {
                String actual = ResourceUtils.getQualifiedClassName(entry.getValue());
                String expected = entry.getKey().getName();
                Assertions.assertEquals(expected, actual);
            }
        }

        @DisplayName("Exceptional behavior: Package does not exist")
        @Test
        public void testNotExists() {
            String sourceCode = """
                public class NotExists {
                }
                """;
            Assertions.assertEquals("NotExists", ResourceUtils.getQualifiedClassName(sourceCode));
        }
    }
}
