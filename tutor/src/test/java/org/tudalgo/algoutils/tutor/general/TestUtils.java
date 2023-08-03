package org.tudalgo.algoutils.tutor.general;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Defines utility methods for testing.
 *
 * @author Nhan Huynh
 */
public final class TestUtils {

    /**
     * The root path of the test resources.
     */
    public static final Path ROOT = Path.of("org", "tudalgo", "algoutils", "tutor", "general");

    /**
     * The package name of the test resources.
     */
    public static final String PACKAGE_NAME = TestUtils.ROOT.toString().replace(File.separator, ".");

    /**
     * Prevents instantiation.
     */
    private TestUtils() {
    }

    /**
     * Returns an input stream to the resource at the given path inside the test resources.
     *
     * @param path the path to the resource
     * @return an input stream to the resource at the given path inside the test resources
     */
    public static InputStream getResourceAsStream(Path path) {
        return TestUtils.class.getClassLoader().getResourceAsStream(path.toString());
    }

    /**
     * Returns the content of the resource at the given path inside the test resources.
     *
     * @param path the path to the resource
     * @return the content of the resource at the given path inside the test resources
     */
    public static String getContent(Path path) {
        try (InputStream stream = getResourceAsStream(path)) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Writes the content in the resources of the given resources to the given target directory.
     *
     * @param target  the target directory
     * @param sources the resources to write
     */
    public static void write(Path target, Path... sources) {
        write(target, List.of(sources));
    }

    /**
     * Writes the content in the resources of the given resources to the given target directory.
     *
     * @param target  the target directory
     * @param sources the resources to write
     */
    public static void write(Path target, Collection<Path> sources) {
        sources.parallelStream().forEach(path -> {
            try (InputStream stream = getResourceAsStream(path)) {
                Path location = target.resolve(path.getFileName());
                Files.write(location, stream.readAllBytes());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    /**
     * Deletes the directory at the given path with all its contents.
     *
     * @param path the path of the directory to delete
     * @throws UncheckedIOException if an I/O error occurs
     */
    public static void deleteDirectory(Path path) {
        try (Stream<Path> paths = Files.walk(path)) {
            for (Path p : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(p);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
