package org.tudalgo.algoutils.algoutils.tutor.general.call;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableResultTest {

    @Test
    void testAssertThrows() {
        var expected = new RuntimeException();
        var result = new ThrowableResult<>(expected);
        var actual = (RuntimeException) null;
        actual = Assertions.assertDoesNotThrow(() -> result.assertThrows(RuntimeException.class));
        assertSame(expected, actual);
        actual = Assertions.assertDoesNotThrow(() -> result.assertThrows(RuntimeException.class, "test"));
        assertSame(expected, actual);
        actual = Assertions.assertDoesNotThrow(() -> result.assertThrows(RuntimeException.class, () -> "test"));
        assertSame(expected, actual);
    }

    @Test
    void testAssertNormal() {
        var result = new ThrowableResult<>(new RuntimeException());
        var actual = (AssertionFailedError) null;
        actual = assertThrows(AssertionFailedError.class, result::assertNormal);
        assertEquals("expected no throwable, but throwable of type RuntimeException was thrown", actual.getMessage());
        actual = assertThrows(AssertionFailedError.class, () -> result.assertNormal("test"));
        assertEquals("test: expected no throwable, but throwable of type RuntimeException was thrown", actual.getMessage());
        actual = assertThrows(AssertionFailedError.class, () -> result.assertNormal(() -> "test"));
        assertEquals("test: expected no throwable, but throwable of type RuntimeException was thrown", actual.getMessage());
    }
}
