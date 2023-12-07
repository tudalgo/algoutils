package org.tudalgo.algoutils.tutor.general.reflections;

import com.google.common.reflect.ClassPath;
import org.sourcegrade.jagr.api.testing.RuntimeClassLoader;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.io.IOException;

import static org.tudalgo.algoutils.tutor.general.ResourceUtils.toPathString;

/**
 * Utility functions for reflections.
 *
 * @author Ruben Deisenroth
 */
public class ReflectUtils {
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and optionally
     * subpackages.
     *
     * @param packageName The base package name
     * @param recursive   whether to scan recursively
     * @return the found classes
     * @throws IOException if an I/O Exception occurs
     */
    @SuppressWarnings("UnstableApiUsage")
    public static Class<?>[] getClasses(final String packageName, boolean recursive) throws IOException {
        final var cycle = TestCycleResolver.getTestCycle();
        if (cycle != null) {
            // Auto-grader Run
            RuntimeClassLoader classLoader = cycle.getClassLoader();
            return classLoader.getClassNames().stream()
                .filter(name -> name.startsWith(packageName))
                .filter(name -> cycle.getSubmission().getSourceFile(toPathString(name)) != null)
                .map(classLoader::loadClass)
                .filter(c -> recursive || !c.getName().contains("$") || c.getDeclaringClass() != null)
                .toArray(Class<?>[]::new);
        }
        // Regular Junit Run
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final ClassPath cp = ClassPath.from(loader);
        final var classes = recursive ? cp.getTopLevelClassesRecursive(packageName) : cp.getTopLevelClasses(packageName);
        return classes
            .stream()
            .map(ClassPath.ClassInfo::load)
            .toArray(Class<?>[]::new);

    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package. (non-recursive)
     *
     * @param packageName The base package name
     * @return the found classes
     * @throws IOException if an I/O Exception occurs
     */
    public static Class<?>[] getClasses(final String packageName) throws IOException {
        return getClasses(packageName, false);
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and all subpackages.
     *
     * @param packageName The base package name
     * @return the found classes
     * @throws IOException if an I/O Exception occurs
     */
    public static Class<?>[] getClassesRecursive(final String packageName) throws IOException {
        return getClasses(packageName, true);
    }
}
