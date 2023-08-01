package org.tudalgo.algoutils.tutor.general;

import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A utility class for resource handling.
 */
public class ResourceUtils {

    private ResourceUtils() {
    }

    private static List<Path> sources = new ArrayList<>();

    private static void loadSources() {
        if (!sources.isEmpty()) {
            return;
        }
        try (var files = Files.walk(Path.of("src"))) {
            sources = files.toList();
        } catch (IOException e) {
            throw new RuntimeException("an error occurred while reading a source files ", e);
        }
    }

    private static Path findSource(String path) {
        loadSources();
        return sources.stream().filter(p -> p.endsWith(path)).findFirst().orElse(null);
    }

    public static String getTypeContent(String className) {
        String pathString = toPathString(className);

        //noinspection UnstableApiUsage
        TestCycle testCycle = TestCycleResolver.getTestCycle();
        if (testCycle != null) {
            return requireNonNull(testCycle.getSubmission().getSourceFile(pathString)).getContent();
        }
        Path path = findSource(pathString);
        if (path == null) {
            return null;
        }
        File file = path.toFile();

        try (var reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new UncheckedIOException("An error occurred while reading the source file %s".formatted(file), e);
        }
    }

    /**
     * <p>Returns the source code of the given student solution class.</p>
     *
     * @param clazz the student solution class
     * @return the source code
     */
    public static String getTypeContent(Class<?> clazz) {
        return getTypeContent(clazz.getName());
    }

    /**
     * Returns the class name as a path string.
     *
     * @param className the class name to convert
     * @return the class name as a path string
     */
    private static String toPathString(String className) {
        return className.replace('.', '/') + ".java";
    }

    /**
     * Returns the class name as a path string.
     *
     * @param clazz the class to convert
     * @return the class name as a path string
     */
    private static String toPathString(Class<?> clazz) {
        return toPathString(clazz.getName());
    }

    public static String toShortSignature(Method method) {
        StringJoiner sj = new StringJoiner(",", method.getName() + "(", ")");
        for (Class<?> parameterType : method.getParameterTypes()) {
            sj.add(parameterType.getTypeName());
        }
        return sj.toString();
    }
}
