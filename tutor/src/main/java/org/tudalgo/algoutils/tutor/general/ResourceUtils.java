package org.tudalgo.algoutils.tutor.general;

import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
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

    /**
     * Returns {@code true} if the source code of the given class is available.
     *
     * @param clazz the class to check
     *
     * @return {@code true} if the source code of the given class is available
     */
    public static boolean isResourceAvailable(Class<?> clazz) {
        return getTypeContent(clazz) != null;
    }

    /**
     * <p>Returns the source code of the given student solution class.</p>
     *
     * @param clazz the student solution class
     *
     * @return the source code
     */
    public static String getTypeContent(Class<?> clazz) {
        var pathString = toPathString(clazz);
        //noinspection UnstableApiUsage
        var testCycle = TestCycleResolver.getTestCycle();
        if (testCycle != null) {
            return requireNonNull(testCycle.getSubmission().getSourceFile(pathString)).getContent();
        }
        var path = findSource(pathString);
        if (path == null) {
            return null;
        }
        var file = path.toFile();
        try (var reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("an error occurred while reading a source file", e);
        }
    }

    private static String toPathString(Class<?> clazz) {
        return clazz.getName().replace('.', '/') + ".java";
    }

    public static String toShortSignature(Method method) {
        StringJoiner sj = new StringJoiner(",", method.getName() + "(", ")");
        for (Class<?> parameterType : method.getParameterTypes()) {
            sj.add(parameterType.getTypeName());
        }
        return sj.toString();
    }

    public static String toShortSignature(Constructor<?> constructor) {
        StringJoiner sj = new StringJoiner(",", constructor.getName() + "(", ")");
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            sj.add(parameterType.getTypeName());
        }
        return sj.toString();
    }

}
