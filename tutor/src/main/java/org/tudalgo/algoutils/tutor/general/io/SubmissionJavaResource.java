package org.tudalgo.algoutils.tutor.general.io;

import org.sourcegrade.jagr.api.testing.SourceFile;
import org.sourcegrade.jagr.api.testing.Submission;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Java submission resource which can access the source code of the submission.
 *
 * @author Nhan Huynh
 */
public class SubmissionJavaResource extends AbstractJavaResource {

    /**
     * The default source of this resource.
     */
    public static final Path DEFAULT_SOURCE = Path.of("src");

    /**
     * The directories to ignore.
     */
    public static final Set<Path> EXCLUDED_DIRECTORIES = Set.of(
        Path.of("graderPublic"),
        Path.of("graderPrivate")
    );


    /**
     * The content source of the classes in this resource.
     */
    private @Nullable Map<String, String> contents = null;


    /**
     * Constructs a new Java submission resource with the given source.
     *
     * @param source the source of this resource
     */
    public SubmissionJavaResource(Path source) {
        super(source);
    }

    /**
     * Constructs a new Java submission resource using the default source path.
     */
    public SubmissionJavaResource() {
        this(DEFAULT_SOURCE);
    }

    private Map<String, String> getContentsByTestCycle(TestCycle cycle) {
        Submission submission = cycle.getSubmission();
        Set<String> classNames = cycle.getClassLoader().getClassNames();
        Map<String, String> resources = new HashMap<>(classNames.size());
        for (String className : classNames) {
            Path path = JavaResource.toPath(className);
            @Nullable SourceFile sourceFile = submission.getSourceFile(path.toString());
            if (sourceFile != null) {
                String content = JavaResource.fixContent(sourceFile.getContent());
                resources.put(className, content);
            }
        }
        return resources;
    }

    private Map<String, String> getContentsByLocal() {
        // Current test run is a local run
        Set<Path> excluded = EXCLUDED_DIRECTORIES.stream()
            .map(source::resolve)
            .collect(Collectors.toSet());

        try (Stream<Path> paths = Files.walk(source)) {
            return paths.parallel().filter(path -> JavaResource.isJavaFile(path) && excluded.stream().noneMatch(path::startsWith))
                .map(path -> {
                    try {
                        String sourceCode = Files.readString(path);
                        JavaInformation info = JavaInformation.from(sourceCode);
                        return Map.entry(info.getQualifiedName(), sourceCode);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Map<String, String> contents() {
        if (contents != null) return contents;
        @SuppressWarnings("UnstableApiUsage")
        @Nullable TestCycle cycle = TestCycleResolver.getTestCycle();
        contents = cycle != null ? getContentsByTestCycle(cycle) : getContentsByLocal();
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
