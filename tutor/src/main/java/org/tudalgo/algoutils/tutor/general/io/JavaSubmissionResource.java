package org.tudalgo.algoutils.tutor.general.io;


import org.sourcegrade.jagr.api.testing.SourceFile;
import org.sourcegrade.jagr.api.testing.Submission;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.tudalgo.algoutils.tutor.general.io.parser.JavaSourceCodeParser;
import org.tudalgo.algoutils.tutor.general.io.parser.JavaSourceCodeStringParser;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.sourcegrade.jagr.api.testing.extension.TestCycleResolver.getTestCycle;

/**
 * A Java submission resource which can access the source code of the submission.
 *
 * @author Nhan Huynh
 */
public class JavaSubmissionResource implements JavaResource {

    /**
     * The default source of this resource.
     */
    private static final Path DEFAULT_SRC = Path.of("src");

    /**
     * The directories to ignore.
     */
    private static final Set<Path> EXCLUDED_DIRECTORIES = Stream.of("Private", "Public")
        .map(it -> "grader" + it)
        .map(Path::of)
        .collect(Collectors.toUnmodifiableSet());

    /**
     * The path to the source file.
     */
    private final Path source;

    /**
     * The content source of the classes in this resource.
     */
    private @Nullable Map<String, String> contents = null;

    /**
     * Constructs a new Java submission resource with the given source.
     *
     * @param source the source of this resource
     */
    public JavaSubmissionResource(Path source) {
        this.source = source;
    }

    /**
     * Constructs a new Java submission resource using the default source path.
     */
    public JavaSubmissionResource() {
        this(DEFAULT_SRC);
    }

    /**
     * Fixes the content of the given string.
     *
     * @param content the content to fix
     * @return the fixed content
     */
    private static String fixContent(String content) {
        return new String(content.getBytes(), US_ASCII) + "\n";
    }


    /**
     * Converts a Java class name into a path to a Java file.
     *
     * @param className the path to convert
     * @return the converted Java class name into a path to a Java file
     */
    protected static String toPathName(String className) {
        return className.replace(".", File.separator) + EXTENSION;
    }

    @Override
    public Map<String, String> contents() {
        if (contents != null) return contents;
        contents = new HashMap<>();
        @SuppressWarnings("UnstableApiUsage")
        @Nullable TestCycle cycle = getTestCycle();
        if (cycle != null) {
            Submission submission = cycle.getSubmission();
            for (String className : cycle.getClassLoader().getClassNames()) {
                String path = toPathName(className);
                @Nullable SourceFile sourceFile = submission.getSourceFile(path);
                if (sourceFile != null) {
                    String content = fixContent(sourceFile.getContent());
                    contents.put(className, content);
                }
            }
        } else {
            // Current test run is a local run
            Set<Path> excluded = EXCLUDED_DIRECTORIES.stream()
                .map(source::resolve)
                .collect(Collectors.toSet());

            try (Stream<Path> paths = Files.walk(source)) {
                for (Path path : paths.filter(p -> JavaResource.isJavaFile(p)
                    && excluded.stream().noneMatch(p::startsWith)).toList()) {
                    if (!JavaResource.isJavaFile(path)) {
                        continue;
                    }
                    try {
                        String sourceCode = Files.readString(path);
                        try (JavaSourceCodeParser parser = new JavaSourceCodeStringParser(sourceCode)) {
                            contents.put(parser.getQualifiedName(), Files.readString(path));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Cannot read submission file " + path, e);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot read submission files", e);
            }
        }
        return contents;
    }

    @Override
    public String get(String className) throws NoSuchElementException {
        if (!contains(className)) {
            throw new NoSuchElementException(className);
        }
        return contents().get(className);
    }
}
