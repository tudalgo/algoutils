package org.tudalgo.algoutils.tutor.general.io;

import org.apache.maven.api.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * The Java standard library resource which can access the source code of the standard library.
 *
 * @author Nhan Huynh
 */
public class JavaStdlibResource implements JavaResource {

    /**
     * The source of this resource.
     */
    private final Path source;

    /**
     * The content source of the classes in this resource.
     */

    private @Nullable Map<String, ZipEntry> entries;

    /**
     * Contracts a new resource from the given source.
     *
     * @param source the source of this resource
     */

    public JavaStdlibResource(Path source) {
        this.source = source;
    }

    /**
     * Contracts a new resource from the default java home source.
     */
    public JavaStdlibResource() {
        this(getDefaultSourceLocation());
    }

    /**
     * Returns the path of the source code of the java standard library. The default location is the src.zip file in
     * the java home directory.
     *
     * @return the path of the source code of the java standard library
     */
    public static Path getDefaultSourceLocation() {
        try (Stream<Path> stream = Files.walk(Path.of(System.getProperty("java.home")))) {
            return stream.filter(path -> path.endsWith("src.zip")).findFirst().orElseThrow();
        } catch (IOException e) {
            throw new IllegalStateException("Could not find java home");
        }
    }

    /**
     * Returns the entries of this resource which contains the source of the classes.
     *
     * @return the entries of this resource which contains the source of the classes
     */
    private Map<String, ZipEntry> getEntries() {
        if (entries != null) return entries;
        entries = new HashMap<>();
        try (ZipFile file = new ZipFile(source.toFile())) {
            Iterator<? extends ZipEntry> it = file.entries().asIterator();
            while (it.hasNext()) {
                ZipEntry entry = it.next();
                String name = entry.getName();
                if (name.endsWith("package-info.java") || name.endsWith("module-info.java")) {
                    continue;
                }
                Path path = Path.of(name);
                entries.put(JavaResource.toClassName(path.subpath(1, path.getNameCount())), entry);

            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return entries;
    }

    @Override
    public Set<String> classNames() {
        return getEntries().keySet();
    }

    @Override
    public int size() {
        return getEntries().size();
    }

    @Override
    public Map<String, String> contents() {
        // Do not use get() here because it will open a new ZipFile for each entry
        try (ZipFile file = new ZipFile(source.toFile())) {
            return getEntries().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                try {
                    return new String(file.getInputStream(entry.getValue()).readAllBytes());
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String get(String className) {
        if (!contains(className)) {
            throw new NoSuchElementException(className);
        }
        ZipEntry entry = getEntries().get(className);
        try (ZipFile file = new ZipFile(source.toFile())) {
            return new String(file.getInputStream(entry).readAllBytes());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
