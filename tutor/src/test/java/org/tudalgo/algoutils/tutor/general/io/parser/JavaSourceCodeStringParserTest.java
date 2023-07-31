package org.tudalgo.algoutils.tutor.general.io.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.tudalgo.algoutils.tutor.general.io.parser.JavaSourceCodeParser;
import org.tudalgo.algoutils.tutor.general.io.parser.JavaSourceCodeStringParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * Defines unit tests for {@link JavaSourceCodeStringParser}.
 *
 * @author Nhan Huynh
 */
public class JavaSourceCodeStringParserTest {

    /**
     * Returns the class name from the given path.
     *
     * @param path the path to the class
     * @return the class name
     */
    private static String getClassName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    /**
     * Tests if the given path to a java file has the expected information.
     *
     * @param path     the path to the java file
     * @param expected the expected information
     * @param actual   the actual information
     * @throws IOException if an I/O error occurs
     */

    private void assertName(String path, String expected, Function<JavaSourceCodeParser, String> actual) throws IOException {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)) {
            Assumptions.assumeTrue(stream != null);
            String sourceCode = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            try (JavaSourceCodeParser parser = new JavaSourceCodeStringParser(sourceCode)) {
                Assertions.assertEquals(expected, actual.apply(parser));
            }
        }
    }

    /**
     * Tests if the package name is correctly parsed.
     *
     * @param expected the expected package name
     * @param path     the path to the java file to parse
     * @throws IOException if an I/O error occurs
     */
    @DisplayName("getPackageName()")
    @ParameterizedTest(name = "File: {1}")
    @JsonClasspathSource({
        "org/tudalgo/algoutils/tutor/general/io/parser/packageName/ComplexComment.json",
        "org/tudalgo/algoutils/tutor/general/io/parser/packageName/Empty.json",
        "org/tudalgo/algoutils/tutor/general/io/parser/packageName/SimpleComment.json",
        "org/tudalgo/algoutils/tutor/general/io/parser/packageName/Simple.json",
    })
    public void testGetPackageName(
        @Property("expected") String expected,
        @Property("path") String path
    ) throws IOException {
        assertName(path, expected, JavaSourceCodeParser::getPackageName);
    }

    /**
     * Tests if the class name is correctly parsed.
     *
     * @param expected the expected class name
     * @param path     the path to the java file to parse
     * @throws IOException if an I/O error occurs
     */
    @DisplayName("getClassName()")
    @ParameterizedTest(name = "File: {1}")
    @JsonClasspathSource({
        "org/tudalgo/algoutils/tutor/general/io/parser/className/Comment.json",
        "org/tudalgo/algoutils/tutor/general/io/parser/className/ComplexJavaDoc.json",
        "org/tudalgo/algoutils/tutor/general/io/parser/className/NoModifier.json",
        "org/tudalgo/algoutils/tutor/general/io/parser/className/Simple.json",
        "org/tudalgo/algoutils/tutor/general/io/parser/className/SimpleJavaDoc.json",
    })
    public void testGetClassName(
        @Property("expected") String expected,
        @Property("path") String path
    ) throws IOException {
        assertName(path, expected, JavaSourceCodeParser::getClassName);
    }

    /**
     * Tests if the qualified name is correctly parsed.
     *
     * @param packageName the expected package name
     * @param className   the expected class name
     * @param path        the path to the java file to parse
     * @throws IOException if an I/O error occurs
     */
    @DisplayName("getQualifiedName()")
    @ParameterizedTest(name = "File: {2}")
    @JsonClasspathSource({
        "org/tudalgo/algoutils/tutor/general/io/parser/qualifiedName/Comment.json",
        "org/tudalgo/algoutils/tutor/general/io/parser/qualifiedName/NoPackage.json",
        "org/tudalgo/algoutils/tutor/general/io/parser/qualifiedName/Simple.json",
    })
    public void testGetQualifiedName(
        @Property("package") String packageName,
        @Property("class") String className,
        @Property("path") String path
    ) throws IOException {
        assertName(
            path,
            packageName.isBlank() ? className : packageName + "." + className,
            JavaSourceCodeParser::getQualifiedName
        );
    }
}
