package org.tudalgo.algoutils.tutor.general.reflections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spoon.reflect.declaration.CtMethod;

import java.util.Arrays;

@DisplayName("BasicTypeLink")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BasicMethodLinkTest {
    private BasicTypeLink type;
    private BasicMethodLink method;


    @BeforeAll
    private void setUp() {
        type = BasicTypeLink.of(Object.class);
        method = BasicMethodLink.of(
            Arrays.stream(Object.class.getMethods())
                .filter(it -> it.getName().equals("toString"))
                .findFirst()
                .orElseThrow()
        );
    }

    @DisplayName("getCtElement")
    @Test
    public void testGetCtElement() {
        CtMethod<?> ctElement = method.getCtElement();
        Assertions.assertNotNull(ctElement);
        Assertions.assertEquals("toString", ctElement.getSimpleName());
    }
}
