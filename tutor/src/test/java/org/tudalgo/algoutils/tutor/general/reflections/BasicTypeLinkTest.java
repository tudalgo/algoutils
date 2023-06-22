package org.tudalgo.algoutils.tutor.general.reflections;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.tudalgo.algoutils.tutor.general.io.JavaSubmissionResource;
import spoon.reflect.declaration.CtType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.FILE_NAMES;
import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.ROOT_PATH;
import static org.tudalgo.algoutils.tutor.general.ResourcesUtils.loadClasses;

/**
 * Defines unit tests for {@link BasicTypeLink}.
 *
 * @author Nhan Huynh
 */
@DisplayName("BasicTypeLink")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BasicTypeLinkTest {

    /**
     * The shared temporary directory to store the compiled classes.
     */
    @TempDir
    private static Path sharedTempDir;

    /**
     * The class to test.
     */
    Map.Entry<Class<?>, String> clazz;

    /**
     * The type link to test.
     */
    private BasicTypeLink type;

    @BeforeAll
    private void setUp() throws IOException, ClassNotFoundException {
        Path path = ROOT_PATH.resolve(FILE_NAMES.get(0));
        clazz = loadClasses(sharedTempDir, path).get(path);
        type = BasicTypeLink.of(clazz.getKey());
    }

    @AfterAll
    public void tearDown() throws IOException {
        try (Stream<Path> paths = Files.walk(sharedTempDir)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(path);
            }
        }
    }

    @DisplayName("getCtElement")
    @Test
    public void testGetCtElement() {
        CtType<?> ctElement = type.getCtElement();
        Assertions.assertNotNull(ctElement);
        Assertions.assertEquals(clazz.getKey().getName(), ctElement.getQualifiedName());
    }
}