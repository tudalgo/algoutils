package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

@DisplayName("JavaResource")
public class JavaResourceTest {

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
}
