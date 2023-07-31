package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
