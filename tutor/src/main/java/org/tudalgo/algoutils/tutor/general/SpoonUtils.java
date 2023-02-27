package org.tudalgo.algoutils.tutor.general;

import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.Stringifiable;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;
import spoon.support.compiler.FileSystemFile;
import spoon.support.compiler.VirtualFile;
import spoon.support.compiler.VirtualFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.sourcegrade.jagr.api.testing.extension.TestCycleResolver.getTestCycle;

/**
 * A collection of utilities for working with spoon.
 */
public class SpoonUtils {

    private static final Pattern SPOON_NAME_PATTERN = Pattern.compile("((?!Ct|Impl)[A-Z][a-z]*)");
    private static CtModel model = null;

    private SpoonUtils() {
    }

    /**
     * @deprecated use {@link #getCtModel()} instead
     */
    @Deprecated
    public static <T, U extends CtType<?>> T getCtElementForSourceCode(
        String ignoredSourceCode,
        Class<U> ignoredKind,
        Matcher<Stringifiable> ignoredNameMatcher
    ) {
        throw new UnsupportedOperationException("use getCtModel instead");
    }

    /**
     * <p>Returns a human-readable name of the given element.</p>
     *
     * @param element the element
     * @return the human-readable name
     */
    public static String getNameOfCtElement(CtElement element) {
        return getNameOfCtElement(element.getClass());
    }

    /**
     * <p>Returns a human-readable name of the given element type.</p>
     *
     * @param type the element type
     * @return the human-readable name
     */
    public static String getNameOfCtElement(Class<?> type) {
        var name = type.getSimpleName();
        var match = SPOON_NAME_PATTERN.matcher(name);
        name = match.results().map(m -> m.group(1).toLowerCase()).collect(Collectors.joining(" "));
        return name;
    }

    /**
     * <p>Returns a <code>CtModel</code> for submission.</p>
     *
     * <p>If the test run is a test cycle run,
     * the class loader of the test cycle is used to load the submission.</p>
     *
     * @return the <code>CtModel</code>
     * @see TestCycleResolver
     */
    public static CtModel getCtModel() {
        if (model != null) {
            return model;
        }
        //noinspection UnstableApiUsage
        var cycle = getTestCycle();
        var launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(17);
        launcher.getEnvironment().setIgnoreSyntaxErrors(true);
        if (cycle != null) {
            launcher.getEnvironment().setInputClassLoader((ClassLoader) cycle.getClassLoader());
        }
        launcher.addInputResource(getSubmissionFiles());
        return model = launcher.buildModel();
    }

    private static final List<String> EXCLUDED_DIRECTORIES = List.of("graderPrivate", "graderPublic");

    private static VirtualFolder getSubmissionFiles() {
        // virtual folder for submission files
        var folder = new VirtualFolder();
        //noinspection UnstableApiUsage
        var cycle = getTestCycle();
        if (cycle != null) {
            // current test run is a grader run
            var submission = cycle.getSubmission();
            for (var name : cycle.getClassLoader().getClassNames()) {
                var path = name.replace('.', '/') + ".java";
                var sourceFile = submission.getSourceFile(path);
                if (sourceFile != null) {
                    var content = sourceFile.getContent();
                    folder.addFile(new VirtualFile(content, name));
                }
            }
        } else {
            // current test run is a local run
            try (var pathStream = Files.walk(Path.of("src"))) {
                var pathIterator = pathStream.iterator();
                while (pathIterator.hasNext()) {
                    var path = pathIterator.next();
                    var pathName = path.toString();
                    if (!pathName.endsWith(".java") || EXCLUDED_DIRECTORIES.contains(pathName.split("/")[1])) {
                        // current file is not a java file or not a submission file
                        continue;
                    }
                    // add file to virtual folder
                    folder.addFile(new FileSystemFile(path.toFile()));
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot read submission files.", e);
            }
        }
        return folder;
    }
}
