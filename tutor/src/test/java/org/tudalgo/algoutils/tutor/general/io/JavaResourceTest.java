package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class JavaResourceTest {


    @Test
    public void testToClassName() {
        Assertions.assertEquals(
            "java.lang.Object",
            JavaResource.toClassName(Path.of("java", "lang", "Object.java"))
        );
    }

    @Test
    public void testToPathName() {
        Assertions.assertEquals(
            Path.of("java", "lang", "Object.java").toString(),
            JavaResource.toPathName("java.lang.Object")
        );
    }

}
