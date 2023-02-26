package org.tudalgo.algoutils.tutor.general.io;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Java resource is a collection of Java source files from a single source which allows access to the content of
 * the classes in the resource.
 *
 * @author Nhan Huynh
 */
public interface JavaResource extends Iterable<Map.Entry<String, String>> {

    /**
     * The file extension of Java source files.
     */
    String EXTENSION = ".java";

    /**
     * Converts a path a Java file to a Java class name.
     *
     * @param path the path to convert
     *
     * @return the converted Java file to a Java class name
     */

    static String toClassName(Path path) {
        return path.toString().replace(File.separator, ".").replace(".java", "");
    }

    /**
     * Converts a Java class name to a path name.
     *
     * @param className the class name to convert
     *
     * @return the converted class name to a path name
     */
    static String toPathName(String className) {
        return className.replace(".", File.separator) + EXTENSION;
    }

    /**
     * Returns the names of all classes in this resource.
     *
     * @return the names of all classes in this resource
     */
    Set<String> classNames();

    /**
     * Returns the number of classes in this resource.
     *
     * @return the number of classes in this resource
     */
    int size();

    /**
     * Returns {@code true} if this resource is empty.
     *
     * @return {@code true} if this resource is empty
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the contents of all classes in this resource. The map is indexed by the class name.
     *
     * @return the contents of all classes in this resource
     */
    default Map<String, String> contents() {
        return classNames().stream().collect(Collectors.toMap(it -> it, this::get));
    }

    /**
     * Returns the content of the class with the given name.
     *
     * @param className the name of the class
     *
     * @return the content of the class with the given name
     *
     * @throws NoSuchElementException if the class with the given name does not exist
     */
    String get(String className) throws NoSuchElementException;

    /**
     * Returns {@code true} if this resource contains a class with the given name.
     *
     * @param className the name of the class
     *
     * @return {@code true} if this resource contains a class with the given name
     */
    default boolean contains(String className) {
        return classNames().contains(className);
    }

    @Override
    default Iterator<Map.Entry<String, String>> iterator() {
        return contents().entrySet().iterator();
    }

    /**
     * Returns a sequential {@code Stream} with this resource as its source.
     *
     * @return a sequential {@code Stream} with this resource as its source
     */
    default Stream<Map.Entry<String, String>> stream() {
        return contents().entrySet().stream();
    }

}
