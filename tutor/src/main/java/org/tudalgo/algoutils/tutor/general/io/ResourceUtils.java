package org.tudalgo.algoutils.tutor.general.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for resources.
 *
 * @author Nhan Huynh
 */
public final class ResourceUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private ResourceUtils() {
    }

    /**
     * The regular expression pattern to match the class name.
     */
    // "package\\s+(\\w+(?:\\.\\w+)*);"
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+(\\w+(?:\\.\\w+)*);");

    /**
     * The matcher for the package declaration.
     */
    private static final Matcher PACKAGE_MATCHER = PACKAGE_PATTERN.matcher("");

    /**
     * The regular expression pattern to match the class name.
     * TODO Improve the pattern to only match the class declaration and not in comment/javadoc
     */
    private static final Pattern CLASS_PATTERN = Pattern.compile(".*\\b(class|@interface|interface|enum|record)\\s+(\\w+).*");

    /**
     * The matcher for the class declaration.
     */
    private static final Matcher CLASS_MATCHER = CLASS_PATTERN.matcher("");

    /**
     * Returns the package name of the given java source code.
     *
     * @param sourceCode the java source code to get the package name
     * @return the package name of the given java source code
     */
    public static String getPackageName(String sourceCode) {
        PACKAGE_MATCHER.reset(sourceCode);
        if (PACKAGE_MATCHER.find()) {
            return PACKAGE_MATCHER.group(1);
        }
        // Default package
        return "";
    }

    /**
     * Returns the simple class name of the given java source code.
     *
     * @param sourceCode the java source code to get the class name
     * @return the simple class name of the given java source code
     * @throws IllegalArgumentException if no class declaration is found in the given source code
     */
    public static String getClassName(String sourceCode) {
        CLASS_MATCHER.reset(sourceCode);

        if (CLASS_MATCHER.find()) {
            while (CLASS_MATCHER.group().trim().startsWith("*")) {
                if (!CLASS_MATCHER.find()) {
                    throw new IllegalArgumentException("No class declaration found in the given source code");
                }
            }
            return CLASS_MATCHER.group(2);
        }
        throw new IllegalArgumentException("No class declaration found in the given source code");
    }

    /**
     * Returns the qualified class name of the given java source code.
     *
     * @param sourceCode the java source code
     * @return the class name of the given java source code
     */
    public static String getQualifiedClassName(String sourceCode) {
        String packageName = getPackageName(sourceCode);
        String className = getClassName(sourceCode);
        if (packageName.isEmpty()) {
            return className;
        }
        return packageName + "." + className;
    }
}
