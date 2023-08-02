package org.tudalgo.algoutils.tutor.general.io;

import com.github.javaparser.StaticJavaParser;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtType;
import spoon.support.compiler.VirtualFile;

/**
 * Parses a Java source code file and extracts information about the Java file from a string.
 *
 * @author Nhan Huynh
 */
public class StringJavaInformation extends BasicJavaInformation {

    /**
     * The Java source code.
     */
    private final String sourceCode;

    /**
     * Constructs a new Java source code parser from a string.
     *
     * @param sourceCode the Java source code
     */
    public StringJavaInformation(String sourceCode) {
        super(() -> StaticJavaParser.parse(sourceCode));
        this.sourceCode = sourceCode;
    }

    @Override
    protected void parseBySpoon() {
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(17);
        launcher.getEnvironment().setIgnoreSyntaxErrors(true);
        launcher.addInputResource(new VirtualFile(sourceCode));

        CtModel model = launcher.buildModel();
        CtType<?> type = model.getAllTypes().stream().findFirst().orElseThrow();
        packageName = type.getPackage().getQualifiedName();
        className = type.getSimpleName();
    }
}
