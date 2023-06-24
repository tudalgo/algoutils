package org.tudalgo.algoutils.tutor.general.io;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;
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

import static org.tudalgo.algoutils.tutor.general.io.ResourceUtils.getQualifiedClassName;

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
     * Constructs a new Java stdlib resource with the given source.
     *
     * @param source the source of this resource
     */
    public JavaStdlibResource(Path source) {
        this.source = source;
    }

    /**
     * Constructs a new Java stdlib resource using the default source path.
     */
    public JavaStdlibResource() {
        this(getStdlibSource());
    }


    /**
     * Returns the path of the source code of the java standard library. The default location is the src.zip file in
     * the java home directory.
     *
     * @return the path of the source code of the java standard library
     */
    public static Path getStdlibSource() {
        try (Stream<Path> stream = Files.walk(Path.of(System.getProperty("java.home")))) {
            return stream.filter(path -> path.endsWith("src.zip")).findFirst().orElseThrow();
        } catch (IOException e) {
            throw new NoSuchElementException("Could not find java home", e);
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
                // Skip info files
                if (!JavaResource.isJavaFile(name)
                    || name.endsWith("package-info.java")
                    || name.endsWith("module-info.java")) {
                    continue;
                }

                String sourceCode = new String(file.getInputStream(entry).readAllBytes(), Charset.defaultCharset());
                String className = getQualifiedClassName(sourceCode);
                entries.put(className, entry);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            return getEntries().entrySet().stream().parallel().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                try {
                    return new String(file.getInputStream(entry.getValue()).readAllBytes(), Charset.defaultCharset());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getContent(String className) {
        if (!contains(className)) {
            throw new NoSuchElementException(className);
        }
        ZipEntry entry = getEntries().get(className);
        try (ZipFile file = new ZipFile(source.toFile())) {
            return new String(file.getInputStream(entry).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
