package org.tudalgo.algoutils.tutor.general.assertions;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.*;

class Assertions2Test {

    @Test
    void testAssertEquals() {
        assertDoesNotThrow(() -> Assertions2.assertEquals("a", "a", Assertions2.emptyContext(), r -> "test"));
        var e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertEquals("a", "b", Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: <a>\n\tactual: <b>", e.getMessage());
        assertDoesNotThrow(() -> Assertions2.assertCallEquals("a", () -> "a", Assertions2.emptyContext(), r -> "test"));
        e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertCallEquals("a", () -> "b", Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: <a>\n\tactual: <b>", e.getMessage());
    }

    @Test
    void testAssertFalse() {
        assertDoesNotThrow(() -> Assertions2.assertFalse(false, Assertions2.emptyContext(), r -> "test"));
        var e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertFalse(true, Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: <false>\n\tactual: <true>", e.getMessage());
        assertDoesNotThrow(() -> Assertions2.assertCallFalse(() -> false, Assertions2.emptyContext(), r -> "test"));
        e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertCallFalse(() -> true, Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: <false>\n\tactual: <true>", e.getMessage());
    }

    @Test
    void testAssertNotEquals() {
        assertDoesNotThrow(() -> Assertions2.assertNotEquals("a", "b", Assertions2.emptyContext(), r -> "test"));
        var e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertNotEquals("a", "a", Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: not <a>\n\tactual: <a>", e.getMessage());
        assertDoesNotThrow(() -> Assertions2.assertCallNotEquals("a", () -> "b", Assertions2.emptyContext(), r -> "test"));
        e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertCallNotEquals("a", () -> "a", Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: not <a>\n\tactual: <a>", e.getMessage());
    }

    @Test
    void testAssertNotNull() {
        assertDoesNotThrow(() -> Assertions2.assertNotNull("a", Assertions2.emptyContext(), r -> "test"));
        var e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertNotNull(null, Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: not <null>\n\tactual: <null>", e.getMessage());
        assertDoesNotThrow(() -> Assertions2.assertCallNotNull(() -> "a", Assertions2.emptyContext(), r -> "test"));
        e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertCallNotNull(() -> null, Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: not <null>\n\tactual: <null>", e.getMessage());
    }


    @Test
    void testAssertNotSame() {
        var a = new Object();
        var b = new Object();
        assertDoesNotThrow(() -> Assertions2.assertNotSame(a, b, Assertions2.emptyContext(), r -> "test"));
        var e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertNotSame(a, a, Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: not <" + a + ">\n\tactual: <" + a + ">", e.getMessage());
        assertDoesNotThrow(() -> Assertions2.assertCallNotSame(a, () -> b, Assertions2.emptyContext(), r -> "test"));
        e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertCallNotSame(a, () -> a, Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: not <" + a + ">\n\tactual: <" + a + ">", e.getMessage());
    }

    @Test
    void testAssertNull() {
        assertDoesNotThrow(() -> Assertions2.assertNull(null, Assertions2.emptyContext(), r -> "test"));
        var e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertNull("a", Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: <null>\n\tactual: <a>", e.getMessage());
        assertDoesNotThrow(() -> Assertions2.assertCallNull(() -> null, Assertions2.emptyContext(), r -> "test"));
        e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertCallNull(() -> "a", Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: <null>\n\tactual: <a>", e.getMessage());
    }

    @Test
    void testAssertSame() {
        var a = "a";
        var b = "b";
        assertDoesNotThrow(() -> Assertions2.assertSame(a, a, Assertions2.emptyContext(), r -> "test"));
        var e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertSame(a, b, Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: <" + a + ">\n\tactual: <" + b + ">", e.getMessage());
        assertDoesNotThrow(() -> Assertions2.assertCallSame(a, () -> a, Assertions2.emptyContext(), r -> "test"));
        e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertCallSame(a, () -> b, Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: <" + a + ">\n\tactual: <" + b + ">", e.getMessage());
    }

    @Test
    void testAssertThrows() {
        assertDoesNotThrow(() -> Assertions2.assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException();
        }, Assertions2.emptyContext(), r -> "test"));

        var e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertThrows(RuntimeException.class, () -> {
        }, Assertions2.emptyContext(), r -> "x"));

        assertEquals("x:\n\texpected: exception of type <" + RuntimeException.class.getName() + ">\n\tactual: no exception was thrown", e.getMessage());

        e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertThrows(RuntimeException.class, () -> {
            throw new NullPointerException();
        }, Assertions2.emptyContext(), r -> "x"));

        var message = e.getMessage();
        message = message.replaceAll("\\d+", "0");
        assertEquals("""
                x:
                \texpected: exception of type <java.lang.RuntimeException>
                \tactual: unexpected exception was thrown

                \torg.tudalgo.algoutils.tutor.general.assertions.Assertions0Test.lambda$testAssertThrows$0(Assertions0Test.java:0)
                \torg.tudalgo.algoutils.tutor.general.assertions.basic.BasicTestOfExceptionalCall.run(BasicTestOfExceptionalCall.java:0)
                \torg.tudalgo.algoutils.tutor.general.assertions.Assertions0.assertThrows(Assertions0.java:0)""",
            message);
    }

    @Test
    void testAssertTrue() {
        assertDoesNotThrow(() -> Assertions2.assertTrue(true, Assertions2.emptyContext(), r -> "test"));
        var e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertTrue(false, Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: <true>\n\tactual: <false>", e.getMessage());
        assertDoesNotThrow(() -> Assertions2.assertCallTrue(() -> true, Assertions2.emptyContext(), r -> "test"));
        e = assertThrows(AssertionFailedError.class, () -> Assertions2.assertCallTrue(() -> false, Assertions2.emptyContext(), r -> "x"));
        assertEquals("x:\n\texpected: <true>\n\tactual: <false>", e.getMessage());
    }

    @Test
    void testFail() {
        var e = assertThrows(AssertionFailedError.class, () -> Assertions2.fail(Assertions2.emptyContext(), r -> "hello world"));
        assertEquals("hello world", e.getMessage());
    }
}
