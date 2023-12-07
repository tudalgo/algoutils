package org.tudalgo.algoutils.reflect;

import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A utility class used for JUnit tests which provides reflective access to some properties and
 * assertions.
 * This class is deprecated and will be removed in a future release.
 * Use the new API located in the {@link org.tudalgo.algoutils.tutor.general.reflections} package instead.
 *
 * @author Ruben Deisenroth
 */
@Deprecated(since = "0.7.0", forRemoval = true)
public final class TestUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private TestUtils() {
    }

    public static final int BRIDGE = 0x00000040;
    public static final int VARARGS = 0x00000080;
    public static final int SYNTHETIC = 0x00001000;
    public static final int ANNOTATION = 0x00002000;
    public static final int ENUM = 0x00004000;
    public static final int MANDATED = 0x00008000;

    /**
     * Tests whether the modifiers are correct.
     *
     * @param expected the expected modifier count
     * @param actual   the actual modifier count
     * @param name     the name of the field to be checked
     */
    public static void assertModifier(final int expected, final int actual, final String name) {
        if (expected < 0) {
            return;
        }
        assertEquals(expected, actual, String.format("Falsche Modifiers für %s! Gefordert: %s Erhalten: %s", name,
            Modifier.toString(expected), Modifier.toString(actual)));
    }

    /**
     * Tests whether the modifiers of a class are correct.
     *
     * @param expected the expected modifier count
     * @param clazz    the class to be checked
     */
    public static void assertModifier(final int expected, final Class<?> clazz) {
        assertModifier(expected, clazz.getModifiers(), "Klasse " + clazz.getName());
    }

    /**
     * Tests whether the modifiers of a method are correct.
     *
     * @param expected the expected modifier count
     * @param method   the method to be checked
     */
    public static void assertModifier(final int expected, final Method method) {
        assertModifier(expected, method.getModifiers(),
            "Methode " + method.getDeclaringClass() + "." + method.getName());
    }

    /**
     * Tests whether the modifiers of a constructor are correct.
     *
     * @param expected    the expected modifier count
     * @param constructor the constructor to be checked
     */
    public static void assertModifier(final int expected, final Constructor<?> constructor) {
        assertModifier(expected, constructor.getModifiers(),
            "Konstruktor " + constructor.getDeclaringClass() + "." + constructor.getName());
    }

    /**
     * Tests whether the modifiers of a field are correct.
     *
     * @param expected the expected modifier count
     * @param attribut the field to be checked
     */
    public static void assertModifier(final int expected, final Field attribut) {
        assertModifier(expected, attribut.getModifiers(),
            "Attribut " + attribut.getDeclaringClass() + "." + attribut.getName());
    }

    /**
     * Returns {@code true} if {@link TestCycleResolver#getTestCycle} does not return {@code null}.
     * This method is deprecated and will be removed in a future release.
     * Use {@link Utils#isJagrRun()} instead.
     *
     * @return {@code true} if {@link TestCycleResolver#getTestCycle} does not return {@code null}
     */
    @Deprecated(since = "0.7.0", forRemoval = true)
    public static boolean isAutograderRun() {
        return TestCycleResolver.getTestCycle() != null;
    }
}
