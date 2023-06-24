package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.nio.file.Path;

/**
 * Defines unit tests for {@link JavaResource}.
 *
 * @author Nhan Huynh
 */
@DisplayName("JavaResource")
public class JavaResourceTest {

    /**
     * Fixes a path by replacing all forward slashes with the system's file separator.
     *
     * @param path the path to fix
     * @return the fixed path
     */
    private static String fixPath(String path) {
        return path.replace("/", File.separator);
    }

    @DisplayName("toClassName(String)")
    @Nested
    public class ToClassNameTest {

        @DisplayName("Exceptional behavior: Non-Java files")
        @Test
        public void testException() {
            Path path = Path.of("java.util.List.jar");
            Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class,
                () -> JavaResource.toClassName(path));
            Assertions.assertEquals("Path %s must be a Java file".formatted(path), throwable.getMessage());
        }

        @DisplayName("Normal behavior: Java files")
        @Test
        public void testNormal() {
            Path path = Path.of("java/util/List.java");
            String className = JavaResource.toClassName(path);
            Assertions.assertEquals(fixPath("java.util.List"), className);
        }
    }

    @DisplayName("toPathName(String)")
    @Nested
    public class ToPathNameTest {

        @DisplayName("Normal behavior: Java files")
        @Test
        public void testNormal() {
            String className = "java.util.List";
            String pathName = JavaResource.toPathName(className);
            Assertions.assertEquals(fixPath("java/util/List.java"), pathName);
        }
    }

    @DisplayName("isJavaFile")
    @Nested
    public class isJavaFileTest {

        @DisplayName("isJavaFile(Path)")
        @Nested
        public class isJavaFilePathTest {

            @DisplayName("Normal behavior: Java files")
            @ParameterizedTest(name = "Path: {0}")
            @ValueSource(strings = {
                "java/util/List.java",
                "com/google/common/collect/ImmutableList.java",
                "java/util/Map.java",
                "apple/swing/JList.java",
            })
            public void testTrue(String path) {
                Assertions.assertTrue(JavaResource.isJavaFile(Path.of(path)));
            }

            @DisplayName("Normal behavior: Non-Java files")
            @ParameterizedTest(name = "Path: {0}")
            @ValueSource(strings = {
                "java/util/List.jar",
                "com/google/common/collect/ImmutableList.class",
                "java/util/Map.txt",
                "apple/swing/JList.java.txt",
            })
            public void testFalse(String path) {
                ;
                Assertions.assertFalse(JavaResource.isJavaFile(Path.of(path)));
            }
        }

        @DisplayName("isJavaFile(Path)")
        @Nested
        public class isJavaFileStringTest {

            @DisplayName("Normal behavior: Java files")
            @ParameterizedTest(name = "Path: {0}")
            @ValueSource(strings = {
                "java/util/List.java",
                "com/google/common/collect/ImmutableList.java",
                "java/util/Map.java",
                "apple/swing/JList.java",
            })
            public void testTrue(String path) {
                Assertions.assertTrue(JavaResource.isJavaFile(path));
            }

            @DisplayName("Normal behavior: Non-Java files")
            @ParameterizedTest(name = "Path: {0}")
            @ValueSource(strings = {
                "java/util/List.jar",
                "com/google/common/collect/ImmutableList.class",
                "java/util/Map.txt",
                "apple/swing/JList.java.txt",
            })
            public void testFalse(String path) {
                Assertions.assertFalse(JavaResource.isJavaFile(path));
            }
        }
    }
}
