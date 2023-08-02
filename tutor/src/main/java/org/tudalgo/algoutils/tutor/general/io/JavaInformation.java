package org.tudalgo.algoutils.tutor.general.io;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Contains information about a java class.
 *
 * @author Nhan Huynh
 */
public interface JavaInformation {

    /**
     * Returns the Java information from the Java source code file.
     *
     * @param sourceCode the Java source code
     * @return the Java information from the Java source code file
     */
    static JavaInformation from(String sourceCode) {
        return new StringJavaInformation(sourceCode);
    }

    /**
     * Returns the Java information from the Java an input stream.
     *
     * @param inputStream the input stream supplier to read the Java source code file from
     * @return the Java information from the Java an input stream
     */
    static JavaInformation from(Supplier<InputStream> inputStream) {
        return new InputStreamJavaInformation(inputStream);
    }

    /**
     * Returns the package name of the class. If the class is in the default package, an empty string is returned.
     *
     * @return the package name
     */
    String getPackageName();

    /**
     * Returns the class name of the class.
     *
     * @return the class name
     */
    String getClassName();

    /**
     * Returns the qualified name of the class.
     *
     * @return the qualified name
     */
    default String getQualifiedName() {
        String packageName = getPackageName();
        String className = getClassName();
        return packageName.isEmpty() ? className : packageName + "." + className;
    }
}
