package org.tudalgo.algoutils.tutor.general.io;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface JavaResource extends Iterable<Map.Entry<String, String>> {

    String EXTENSION = ".java";

    static String toClassName(Path path) {
        return path.toString().replace(File.separator, ".").replace(".java", "");
    }

    static String toPathName(String className) {
        return className.replace(".", File.separator) + EXTENSION;
    }

    Set<String> classNames();

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    default Map<String, String> contents() {
        return classNames().stream().collect(Collectors.toMap(it -> it, this::get));
    }

    String get(String className);

    default boolean contains(String className) {
        return classNames().contains(className);
    }

    @Override
    default Iterator<Map.Entry<String, String>> iterator() {
        return contents().entrySet().iterator();
    }

    default Stream<Map.Entry<String, String>> stream() {
        return contents().entrySet().stream();
    }

}
