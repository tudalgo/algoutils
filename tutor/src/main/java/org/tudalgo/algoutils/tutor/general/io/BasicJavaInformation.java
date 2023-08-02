package org.tudalgo.algoutils.tutor.general.io;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * A basic implementation of {@link JavaInformation}.
 *
 * @author Nhan Huynh
 */
public abstract class BasicJavaInformation implements JavaInformation {

    /**
     * The package name of the Java source code file.
     */
    protected @Nullable String packageName;

    /**
     * The class name of the Java source code file.
     */
    protected @Nullable String className;

    /**
     * The supplier to get the JavaParser compilation unit used to parse the Java source code file.
     */
    private final Supplier<CompilationUnit> javaParser;

    /**
     * Constructs a new basic Java information object.
     *
     * @param javaParser the supplier to get the JavaParser compilation unit used to parse the Java source code file
     */
    protected BasicJavaInformation(Supplier<CompilationUnit> javaParser) {
        this.javaParser = javaParser;
    }

    /**
     * Uses the JavaParser library to parse the Java source code file and extracts the information.
     */
    protected void parseByJavaParser() {
        CompilationUnit compilationUnit = javaParser.get();
        TypeDeclaration<?> type = compilationUnit.findFirst(TypeDeclaration.class).orElseThrow();
        className = type.getNameAsString();
        Optional<PackageDeclaration> p = compilationUnit.getPackageDeclaration();
        if (p.isPresent()) {
            packageName = p.get().getNameAsString();
        } else {
            packageName = "";
        }
    }

    /**
     * Uses the Spoon library to parse the Java source code file and extracts the information.
     */
    protected abstract void parseBySpoon();

    /**
     * Parses the Java source code file and extracts information about the Java file.
     */
    private void parse() {
        try {
            // Use JavaParser if possible since its faster
            parseByJavaParser();
        } catch (ParseProblemException e) {
            parseBySpoon();
        }
    }

    @Override
    public String getPackageName() {
        if (packageName == null) parse();
        return packageName;
    }

    @Override
    public String getClassName() {
        if (className == null) parse();
        return className;
    }
}
