package org.tudalgo.algoutils.tutor.general.io.parser;

/**
 * Parses a Java source code file and extracts information about the Java file.
 *
 * @author Nhan Huynh
 */
public interface JavaSourceCodeParser extends AutoCloseable {

    /**
     * The tokens for identifying package declaration.
     */
    String PACKAGE_KEYWORD = "package";

    /**
     * The tokens for identifying class declaration.
     */
    String CLASS_KEYWORD = "class";

    /**
     * The starting tokens for identifying JavaDoc.
     */
    String JAVA_DOC_START = "/**";

    /**
     * The ending tokens for identifying JavaDoc.
     */
    String JAVA_DOC_END = "*/";

    /**
     * The tokens for identifying single line comment.
     */
    String COMMENT_SINGLE_LINE = "//";

    /**
     * The starting tokens for identifying multi line comment.
     */
    String COMMENT_MULTI_LINE_START = "/*";

    /**
     * The ending tokens for identifying multi line comment.
     */
    String COMMENT_MULTI_LINE_END = "*/";

    /**
     * Returns {@code true} if the specified token is a valid token for a package name.
     *
     * @param token the token to be checked
     * @return {@code true} if the specified token is a valid token for a package name
     */
    static boolean isPackageNameToken(char token) {
        return Character.isAlphabetic(token) || Character.isDigit(token) || token == '.' || token == '_'
            || token == '$';
    }

    /**
     * Returns {@code true} if the specified token is a valid token for a class name.
     *
     * @param token the token to be checked
     * @return {@code true} if the specified token is a valid token for a class name
     */
    static boolean isClassNameToken(char token) {
        return Character.isAlphabetic(token) || Character.isDigit(token) || token == '_' || token == '$';
    }

    /**
     * Returns the package name of the Java source code file.
     *
     * @return the package name of the Java source code file
     */
    String getPackageName();

    /**
     * Returns the class name of the Java source code file.
     *
     * @return the class name of the Java source code file
     */
    String getClassName();

    /**
     * Returns the qualified name of the Java source code file.
     *
     * @return the qualified name of the Java source code file
     */
    default String getQualifiedName() {
        String packageName = getPackageName();
        String className = getClassName();
        return packageName.isEmpty() ? className : packageName + "." + className;
    }
}
