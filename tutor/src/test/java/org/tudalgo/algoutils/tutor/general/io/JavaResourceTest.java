package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;

/**
 * Defines unit tests for {@link JavaResource}.
 *
 * @author Nhan Huynh
 */
@DisplayName("JavaResource")
public class JavaResourceTest {

    /**
     * Defines unit tests for {@link JavaResource#isJavaFile(Path)} and {@link JavaResource#isJavaFile(String)}.
     */
    @DisplayName("isJavaFile")
    @Nested
    public class isJavaFileTest {

        /**
         * Defines unit tests for {@link JavaResource#isJavaFile(Path)}.
         */
        @DisplayName("isJavaFile(Path)")
        @Nested
        public class isJavaFilePathTest {

            /**
             * Test normal behavior with Java files.
             *
             * @param path the path to the file
             */
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

            /**
             * Test normal behavior with non-Java files.
             *
             * @param path the path to the file
             */
            @DisplayName("Normal behavior: Non-Java files")
            @ParameterizedTest(name = "Path: {0}")
            @ValueSource(strings = {
                "java/util/List.jar",
                "com/google/common/collect/ImmutableList.class",
                "java/util/Map.txt",
                "apple/swing/JList.java.txt",
            })
            public void testFalse(String path) {
                Assertions.assertFalse(JavaResource.isJavaFile(Path.of(path)));
            }
        }

        /**
         * Defines unit tests for {@link JavaResource#isJavaFile(String)}.
         */
        @DisplayName("isJavaFile(Path)")
        @Nested
        public class isJavaFileStringTest {

            /**
             * Test normal behavior with Java files.
             *
             * @param path the path to the file
             */
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

            /**
             * Test normal behavior with non-Java files.
             *
             * @param path the path to the file
             */
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

    @DisplayName("toPath()")
    @Test
    public void testToPath() {
        String className = Object.class.getName();
        Assertions.assertEquals(Path.of("java/lang/Object.java"), JavaResource.toPath(className));
    }
}
