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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
     * The regular expression pattern to match the class name.
     */
    private static Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+(\\w+(?:\\.\\w+)*);");

    /**
     * The matcher for the package declaration.
     */
    private static Matcher PACKAGE_MATCHER = PACKAGE_PATTERN.matcher("");

    /**
     * The regular expression pattern to match the class name.
     */
    private static Pattern CLASS_PATTERN = Pattern.compile("class\\s+(\\w+)");

    /**
     * The matcher for the class declaration.
     */
    private static Matcher CLASS_MATCHER = CLASS_PATTERN.matcher("");

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

    /**
     * Returns the class name of the given java source code.
     *
     * @param sourceCode the java source code
     * @return the class name of the given java source code
     */
    private static String getClassName(String sourceCode) {

        String packageName = "";
        PACKAGE_MATCHER.reset(sourceCode);
        if (PACKAGE_MATCHER.find()) {
            packageName = PACKAGE_MATCHER.group(1) + ".";
        }

        // Regular expression pattern to match the class declaration

        CLASS_MATCHER.reset(sourceCode);
        if (CLASS_MATCHER.find()) {
            String className = CLASS_MATCHER.group(1);
            return packageName + className;
        }
        // No class declaration found, return only the package name
        return packageName;
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
                    try {
                        String content = Files.readString(path);
                        String className = getClassName(content);
                        contents.put(className, Files.readString(path));
                    } catch (IOException e) {
                        throw new IllegalStateException("Cannot read submission file " + path, e);
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException("Cannot read submission files", e);
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
