package org.tudalgo.algoutils.tutor.general.io.parser;

import java.io.IOException;

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
     * The tokens for identifying interface declaration.
     */
    String INTERFACE_KEYWORD = "interface";

    /**
     * The tokens for identifying enum declaration.
     */
    String ENUM_KEYWORD = "enum";

    /**
     * The tokens for identifying record declaration.
     */
    String RECORD_KEYWORD = "record";

    /**
     * The tokens for identifying annotation declaration.
     */
    String ANNOTATION_KEYWORD = "@interface";

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
     * Returns the keywords for identifying class declaration.
     *
     * @return the keywords for identifying class declaration
     */
    static String[] classKeywords() {
        return new String[]{CLASS_KEYWORD, INTERFACE_KEYWORD, ENUM_KEYWORD, RECORD_KEYWORD, ANNOTATION_KEYWORD};
    }

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

    @Override
    void close() throws IOException;
}
