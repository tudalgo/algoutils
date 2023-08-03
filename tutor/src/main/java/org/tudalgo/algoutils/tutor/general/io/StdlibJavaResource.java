package org.tudalgo.algoutils.tutor.general.io;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.tutor.general.Streams;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Java stdlib resource which can access the source code of the java standard library.
 *
 * @author Nhan Huynh
 */
public class StdlibJavaResource extends AbstractJavaResource {

    /**
     * The default capacity for the stored entries. This number reflects approximately the number of classes in the
     * java standard library.
     */
    private static final int DEFAULT_CAPACITY = 14580;

    /**
     * The default source file name of the java standard library.
     */
    public static final String SRC_FILE_NAME = "src.zip";

    /**
     * The cache of the stored entries instead of loading the source code of the entire java standard library.
     */
    private @Nullable Map<String, ZipArchiveEntry> cache;

    /**
     * The content source of the classes in this resource.
     */
    private final Map<String, String> contents = new ConcurrentHashMap<>(DEFAULT_CAPACITY);

    /**
     * Constructs a new Java stdlib resource with the given source.
     *
     * @param source the source of this resource
     */
    public StdlibJavaResource(Path source) {
        super(source);
    }

    /**
     * Constructs a new Java stdlib resource using the default source path.
     */
    public StdlibJavaResource() {
        this(getStdlibSource());
    }

    /**
     * Returns the path of the source code of the java standard library. The default location is the src.zip file in
     * the java home directory.
     *
     * @return the path of the source code of the java standard library
     */
    public static Path getStdlibSource() {
        try (Stream<Path> stream = Files.walk(Path.of(System.getProperty("java.home"))).parallel()) {
            return stream.filter(path -> path.endsWith(SRC_FILE_NAME)).findFirst().orElseThrow();
        } catch (IOException e) {
            throw new NoSuchElementException("Could not find java home", e);
        }
    }

    /**
     * Converts a path of a Java file into a class name.
     *
     * @param path the path to convert
     * @return the converted path of a Java file into a class name
     */
    private static String getClassName(Path path) {
        return path.subpath(1, path.getNameCount()).toString()
            .replace(File.separator, ".")
            .replace(".java", "");
    }

    /**
     * Loads the cache and returns it.
     *
     * @return the loaded cache
     */
    private Map<String, ZipArchiveEntry> getCache() {
        if (cache != null) return cache;
        cache = new HashMap<>(DEFAULT_CAPACITY);
        try (ZipFile file = new ZipFile(source.toFile())) {
            cache = Streams.stream(file.getEntries().asIterator())
                .filter(entry -> {
                    String fileName = entry.getName();
                    return JavaResource.isJavaFile(fileName)
                        && !fileName.endsWith("package-info.java")
                        && !fileName.endsWith("module-info.java");
                })
                .collect(Collectors.toMap(
                    entry -> getClassName(Path.of(entry.getName())),
                    Function.identity(),
                    (a, b) -> a,
                    () -> new HashMap<>(DEFAULT_CAPACITY)
                ));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return cache;
    }

    @Override
    public int size() {
        return getCache().values().size();
    }

    @Override
    public Map<String, String> contents() {
        cache = getCache();
        if (contents.size() == cache.size()) return contents;
        // Do not use get(String) since it will create a new ZipFile for each entry
        try (ZipFile file = new ZipFile(source.toFile())) {
            cache.entrySet().parallelStream().forEach(entry -> {
                String className = entry.getKey();
                ZipArchiveEntry zipEntry = entry.getValue();
                try {
                    contents.put(className, new String(file.getInputStream(zipEntry).readAllBytes()));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return contents;
    }

    @Override
    public String get(String className) throws NoSuchElementException {
        if (!contains(className)) {
            throw new NoSuchElementException(className);
        }
        if (contents.containsKey(className)) {
            return contents.get(className);
        }

        // Case: contents does not contain the class, load it from entries
        try (ZipFile file = new ZipFile(source.toFile())) {
            assert cache != null;
            ZipArchiveEntry entry = cache.get(className);
            String sourceCode = new String(file.getInputStream(entry).readAllBytes());
            contents.put(className, sourceCode);
            return sourceCode;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean contains(String className) {
        return getCache().containsKey(className);
    }
}
