package org.tudalgo.algoutils.tutor.general.io;

import com.github.javaparser.StaticJavaParser;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtType;
import spoon.support.compiler.VirtualFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

/**
 * Parses a Java source code file and extracts information about the Java file from an input stream.
 *
 * @author Nhan Huynh
 */
public class InputStreamJavaInformation extends BasicJavaInformation {

    /**
     * The input stream supplier to read the Java source code file from.
     */
    private final Supplier<InputStream> inputStream;

    /**
     * Constructs a new Java source code parser from an input stream supplier to read the Java source code file.
     *
     * @param inputStream the input stream supplier to read the Java source code file from
     */
    public InputStreamJavaInformation(Supplier<InputStream> inputStream) {
        super(() -> StaticJavaParser.parse(inputStream.get()));
        this.inputStream = inputStream;
    }

    @Override
    protected void parseBySpoon() {
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(17);
        launcher.getEnvironment().setIgnoreSyntaxErrors(true);

        try (InputStream stream = inputStream.get()) {
            launcher.addInputResource(new VirtualFile(new String(stream.readAllBytes(), StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        CtModel model = launcher.buildModel();
        CtType<?> type = model.getAllTypes().stream().findFirst().orElseThrow();
        packageName = type.getPackage().getQualifiedName();
        className = type.getSimpleName();
    }

}
