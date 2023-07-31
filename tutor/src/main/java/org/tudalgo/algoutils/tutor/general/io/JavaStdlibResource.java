package org.tudalgo.algoutils.tutor.general.io;

import org.tudalgo.algoutils.tutor.general.io.parser.JavaSourceCodeParser;
import org.tudalgo.algoutils.tutor.general.io.parser.JavaSourceCodeStringParser;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
     * The default source file name of the java standard library.
     */
    private static final String SRC_FILE_NAME = "src.zip";

    /**
     * The source of this resource.
     */
    private final Path source;

    /**
     * The content source of the classes in this resource. (Class name -> Zip entry
     */

    private @Nullable Map<String, ZipEntry> entries;

    /**
     * The content of the classes in this resource. (Class name -> source code)
     */
    private @Nullable Map<String, String> content;

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
            return stream.filter(path -> path.endsWith(SRC_FILE_NAME)).findFirst().orElseThrow();
        } catch (IOException e) {
            throw new NoSuchElementException("Could not find java home", e);
        }
    }

    protected Map<String, ZipEntry> getEntries() {
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
                try (JavaSourceCodeParser parser = new JavaSourceCodeStringParser(sourceCode)) {
                    entries.put(parser.getQualifiedName(), entry);
                }
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
        if (content != null) return content;
        // Do not use get() here because it will open a new ZipFile for each entry
        try (ZipFile file = new ZipFile(source.toFile())) {
            content = getEntries().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                try {
                    return new String(file.getInputStream(entry.getValue()).readAllBytes(), Charset.defaultCharset());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
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
            throw new RuntimeException(e);
        }
    }

    public boolean contains(String className) {
        return getEntries().containsKey(className);
    }

}
