package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

public class JavaStdlibResourceTest {

    private static final String EXAMPLE_CLASS = Object.class.getName();
    private JavaResource resource;

    @BeforeEach
    public void setUp() {
        resource = new JavaStdlibResource();
    }


    @Test
    public void testGetDefaultSourceLocation() {
        Path path = JavaStdlibResource.getDefaultSourceLocation();
        Assertions.assertTrue(path.endsWith("src.zip"));
    }

    @Test
    public void testClassNames() {
        Assertions.assertFalse(resource.classNames().isEmpty());
    }

    @Test
    public void testSize() {
        Assertions.assertTrue(resource.size() > 0);
    }

    @Test
    public void testIsEmpty() {
        Assertions.assertFalse(resource.isEmpty());
    }

    @Test
    public void testContents() {
        Assertions.assertTrue(resource.contents().size() > 0);
    }

    @Test
    public void testGet() {
        Assertions.assertNotNull(resource.get(EXAMPLE_CLASS));
    }

    @Test
    public void testContains() {
        Assertions.assertTrue(resource.contains(EXAMPLE_CLASS));
    }

    @Test
    public void testIterator() {
        Iterator<Map.Entry<String, String>> it = resource.iterator();
        Assertions.assertTrue(it.hasNext());
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (entry.getKey().equals(EXAMPLE_CLASS)) {
                return;
            }
        }
        Assertions.fail("Iterator did not contain " + EXAMPLE_CLASS);
    }

    @Test
    public void testStream() {
        Assertions.assertTrue(resource.stream().findAny().isPresent());
        Assertions.assertTrue(resource.stream().anyMatch(entry -> entry.getKey().equals(EXAMPLE_CLASS)));
    }


}
