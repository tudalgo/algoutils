package org.tudalgo.algoutils.tutor.general.io;

import org.sourcegrade.jagr.api.testing.SourceFile;
import org.sourcegrade.jagr.api.testing.Submission;
import org.sourcegrade.jagr.api.testing.TestCycle;

import javax.annotation.Nullable;
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
import static org.tudalgo.algoutils.tutor.general.io.ResourceUtils.getQualifiedClassName;

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
     * The source of this resource.
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

    @Override
    public Set<String> classNames() {
        return contents().keySet();
    }

    @Override
    public int size() {
        return contents().size();
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

    @Override
    public Map<String, String> contents() {
        if (contents != null) return contents;
        contents = new HashMap<>();
        @SuppressWarnings("UnstableApiUsage")
        @Nullable TestCycle cycle = getTestCycle();
        if (cycle != null) {
            Submission submission = cycle.getSubmission();
            for (String className : cycle.getClassLoader().getClassNames()) {
                String path = JavaResource.toPathName(className);
                @Nullable SourceFile sourceFile = submission.getSourceFile(path);
                if (sourceFile != null) {
                    String content = fixContent(sourceFile.getContent());
                    contents.put(className, content);
                }
            }
        } else {
            Set<Path> excluded = EXCLUDED_DIRECTORIES.stream()
                .map(source::resolve)
                .collect(Collectors.toUnmodifiableSet());

            // Current test run is a local run
            try (Stream<Path> paths = Files.walk(source)) {
                for (Path path : paths.filter(it -> it.toString().endsWith(JavaResource.EXTENSION)
                    && excluded.stream().noneMatch(it::startsWith)).toList()) {
                    if (!JavaResource.isJavaFile(path)) {
                        continue;
                    }
                    try {
                        String content = Files.readString(path);
                        String className = getQualifiedClassName(content);
                        contents.put(className, Files.readString(path));
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
    public String getContent(String className) throws NoSuchElementException {
        if (!contains(className)) {
            throw new NoSuchElementException(className);
        }
        return contents().get(className);
    }
}
