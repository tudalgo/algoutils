package org.tudalgo.algoutils.tutor.general;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Defines utility methods for testing resources.
 *
 * @author Nhan Huynh
 */
public final class ResourcesUtils {

    /**
     * The root path of the resource.
     */
    public static final Path ROOT_PATH = Path.of("org", "tudalgo", "algoutils", "tutor", "general");

    /**
     * The example names of the files in the resource.
     */
    public static final List<Path> FILE_NAMES = Stream.of("BankAccount", "Car", "Person", "Rectangle", "Student")
        .map(it -> it + ".java")
        .map(Path::of)
        .toList();


    /**
     * Prevents instantiation.
     */
    private ResourcesUtils() {

    }

    /**
     * Saves the contents of the given resource to the given location and compiles the files.
     *
     * @param location the location to save the resource to
     * @param paths    the paths of the files in the resource
     * @return a map from the paths to the classes and contents of the files
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if a class cannot be found
     */
    public static Map<Path, Map.Entry<Class<?>, String>> loadClasses(
        Path location,
        Path... paths
    ) throws IOException, ClassNotFoundException {
        Map<Path, Map.Entry<Class<?>, String>> classes = new HashMap<>(paths.length);
        ClassLoader classLoader = ResourcesUtils.class.getClassLoader();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        for (Path path : paths) {
            try (InputStream inputStream = classLoader.getResourceAsStream(path.toString())) {
                assert inputStream != null;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String content = reader.lines().collect(Collectors.joining("\n"));
                    Path tmp = location.resolve(path.getFileName());
                    Files.write(tmp, content.getBytes());
                    compiler.run(null, null, null, tmp.toString());
                    String className = path.toString().replace(File.separator, ".").replace(".java", "");
                    classes.put(path, Map.entry(Class.forName(className), content));
                }
            }
        }
        return classes;
    }

    /**
     * Deletes the directory at the given path with all its contents.
     *
     * @param path the path of the directory to delete
     * @throws IOException if an I/O error occurs
     */
    public static void deleteDirectory(Path path) throws IOException {
        try (Stream<Path> paths = Files.walk(path)) {
            for (Path p : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(p);
            }
        }
    }
}
