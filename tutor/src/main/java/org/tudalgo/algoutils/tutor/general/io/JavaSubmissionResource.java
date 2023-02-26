package org.tudalgo.algoutils.tutor.general.io;

import org.apache.maven.api.annotations.Nullable;
import org.sourcegrade.jagr.api.testing.SourceFile;
import org.sourcegrade.jagr.api.testing.Submission;
import org.sourcegrade.jagr.api.testing.TestCycle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
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
     * The local source of this resource.
     */
    private static final Path LOCAL_SOURCE = Path.of("tutor/src");

    /**
     * The directories to ignore..
     */
    private static final List<Path> EXCLUDED_DIRECTORIES = Stream.of("graderPrivate", "graderPublic")
        .map(LOCAL_SOURCE::resolve)
        .toList();

    /**
     * The content source of the classes in this resource.
     */
    private @Nullable Map<String, String> contents = null;

    @Override
    public Set<String> classNames() {
        return contents.keySet();
    }

    @Override
    public int size() {
        return contents().size();
    }

    /**
     * Fixes the content of the given string.
     *
     * @param content the content to fix
     *
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
            // Current test run is a local run
            try (Stream<Path> paths = Files.walk(LOCAL_SOURCE)) {
                paths.filter(path -> path.toString().endsWith(JavaResource.EXTENSION) && EXCLUDED_DIRECTORIES.stream().noneMatch(path::startsWith))
                    .forEach(path -> {
                        try {
                            // Class name starts after prefix src/main/java
                            contents.put(
                                JavaResource.toClassName(path.subpath(3, path.getNameCount())),
                                Files.readString(path)
                            );
                        } catch (IOException e) {
                            throw new IllegalStateException("Cannot read submission file " + path, e);
                        }
                    });
            } catch (IOException e) {
                throw new IllegalStateException("Cannot read submission files", e);
            }
        }
        return contents;
    }

    @Override
    public String get(String className) {
        if (!contains(className)) {
            throw new NoSuchElementException(className);
        }
        return contents().get(className);
    }

}
