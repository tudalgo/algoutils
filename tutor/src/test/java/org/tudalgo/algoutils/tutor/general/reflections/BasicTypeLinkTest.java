package org.tudalgo.algoutils.tutor.general.reflections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spoon.reflect.declaration.CtType;

@DisplayName("BasicTypeLink")
public class BasicTypeLinkTest {

    private final BasicTypeLink type = BasicTypeLink.of(Object.class);

    @DisplayName("getCtElement")
    @Test
    public void testGetCtElement() {
        CtType<?> ctElement = type.getCtElement();
        Assertions.assertNotNull(ctElement);
        Assertions.assertEquals("java.lang.Object", ctElement.getQualifiedName());
    }
}
