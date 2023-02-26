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
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JavaStdlibResource implements JavaResource {

    private final Path source;

    private @Nullable Map<String, ZipEntry> entries;

    public JavaStdlibResource(Path source) {
        this.source = source;
    }

    public JavaStdlibResource() {
        this(getJavaHome());
    }

    public static Path getJavaHome() {
        try (Stream<Path> stream = Files.walk(Path.of(System.getProperty("java.home")))) {
            return stream.filter(path -> path.endsWith("src.zip")).findFirst().orElseThrow();
        } catch (IOException e) {
            throw new IllegalStateException("could not find java home");
        }
    }

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

}
