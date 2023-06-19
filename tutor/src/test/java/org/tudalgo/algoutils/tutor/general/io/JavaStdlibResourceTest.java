package org.tudalgo.algoutils.tutor.general.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;

/**
 * Defines unit tests for {@link JavaStdlibResource}.
 *
 * @author Nhan Huynh
 */
@DisplayName("JavaStdlibResource")
public class JavaStdlibResourceTest {
    private static final String EXAMPLE_CLASS = Object.class.getName();
    private JavaResource resource;

    @BeforeEach
    public void setUp() {
        resource = new JavaStdlibResource();
    }

    @DisplayName("classNames()")
    @Test
    public void testClassNames() {
        Assertions.assertFalse(resource.classNames().isEmpty());
    }

    @DisplayName("size()")
    @Test
    public void testSize() {
        Assertions.assertTrue(resource.size() > 0);
    }

    @DisplayName("isEmpty()")
    @Test
    public void testIsEmpty() {
        Assertions.assertFalse(resource.isEmpty());
    }

    @DisplayName("contents()")
    @Test
    public void testContents() {
        Assertions.assertTrue(resource.contents().size() > 0);
    }

    @DisplayName("getContent(String)")
    @Test
    public void testGet() {
        Assertions.assertNotNull(resource.getContent(EXAMPLE_CLASS));
    }

    @DisplayName("contains(String)")
    @Test
    public void testContains() {
        Assertions.assertTrue(resource.contains(EXAMPLE_CLASS));
    }

    @DisplayName("iterator()")
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

    @DisplayName("stream()")
    @Test
    public void testStream() {
        Assertions.assertTrue(resource.stream().findAny().isPresent());
        Assertions.assertTrue(resource.stream().anyMatch(entry -> entry.getKey().equals(EXAMPLE_CLASS)));
    }
}
