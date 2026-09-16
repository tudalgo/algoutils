package org.tudalgo.algoutils.assertions;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A context for assertions, which can hold arbitrary key-value pairs.
 * This can be used to provide additional information about the assertion,
 * which can be included in the failure message when an assertion fails.
 *
 * <p>The {@code Context} class extends {@link AbstractMap} and provides several
 * factory methods for convenient creation of context instances with key-value pairs.</p>
 *
 * @see org.tudalgo.algoutils.assertions.options.BasicOptions#withContext
 */
public class Context extends AbstractMap<String, Object> {

    private final Map<String, Object> backingMap = new HashMap<>();

    /**
     * Returns an unmodifiable view of the underlying map.
     *
     * @return an unmodifiable map containing all context entries
     */
    public Map<String, ?> asMap() {
        return Collections.unmodifiableMap(backingMap);
    }

    @Override
    public Object put(String key, Object value) {
        return backingMap.put(key, value);
    }

    @Override
    public @NotNull Set<Entry<String, Object>> entrySet() {
        return backingMap.entrySet();
    }

    /**
     * Returns a string representation of this context in a formatted output.
     * The format displays each key-value pair on separate lines with proper indentation.
     *
     * @return a formatted string representation of this context
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(10 + size() * 50);
        stringBuilder.append("Context {\n");
        for (Entry<String, Object> entry : entrySet()) {
            stringBuilder.append("  ")
                .append(entry.getKey())
                .append(" = ")
                .append(Objects.toString(entry.getValue()).replace("\n", "\n  "))
                .append("\n");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * Creates a new context with the specified key-value pairs.
     *
     * @param entries the key-value pairs to add to the context
     * @return a new context containing the specified key-value pairs
     */
    @SafeVarargs
    public static Context ofEntries(Map.Entry<String, Object>... entries) {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : entries) {
            context.put(entry.getKey(), entry.getValue());
        }
        return context;
    }

    /**
     * Creates a new context with a single key-value pair.
     *
     * @param key   the key
     * @param value the value
     * @return a new context containing the specified key-value pair
     */
    public static Context of(String key, Object value) {
        return of(key, value, null, null);
    }

    /**
     * Creates a new context with two key-value pairs.
     *
     * @param key1   the first key
     * @param value1 the first value
     * @param key2   the second key
     * @param value2 the second value
     * @return a new context containing the specified key-value pairs
     */
    public static Context of(String key1, Object value1, String key2, Object value2) {
        return of(key1, value1, key2, value2, null, null);
    }

    /**
     * Creates a new context with three key-value pairs.
     *
     * @param key1   the first key
     * @param value1 the first value
     * @param key2   the second key
     * @param value2 the second value
     * @param key3   the third key
     * @param value3 the third value
     * @return a new context containing the specified key-value pairs
     */
    public static Context of(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        return of(key1, value1, key2, value2, key3, value3, null, null);
    }

    /**
     * Creates a new context with four key-value pairs.
     *
     * @param key1   the first key
     * @param value1 the first value
     * @param key2   the second key
     * @param value2 the second value
     * @param key3   the third key
     * @param value3 the third value
     * @param key4   the fourth key
     * @param value4 the fourth value
     * @return a new context containing the specified key-value pairs
     */
    public static Context of(
        String key1, Object value1,
        String key2, Object value2,
        String key3, Object value3,
        String key4, Object value4
    ) {
        return of(key1, value1, key2, value2, key3, value3, key4, value4, null, null);
    }

    /**
     * Creates a new context with up to five key-value pairs.
     * If you need to add more entries, use the {@link Context#ofEntries} method or
     * create an instance of this class and add them manually using {@link #put}.
     * {@code null} keys are ignored and their corresponding values are not added.
     *
     * @param key1   the first key
     * @param value1 the first value
     * @param key2   the second key
     * @param value2 the second value
     * @param key3   the third key
     * @param value3 the third value
     * @param key4   the fourth key
     * @param value4 the fourth value
     * @param key5   the fifth key
     * @param value5 the fifth value
     * @return a new context containing the specified key-value pairs
     */
    public static Context of(
        String key1, Object value1,
        String key2, Object value2,
        String key3, Object value3,
        String key4, Object value4,
        String key5, Object value5
    ) {
        Context context = new Context();
        if (key1 != null) context.put(key1, value1);
        if (key2 != null) context.put(key2, value2);
        if (key3 != null) context.put(key3, value3);
        if (key4 != null) context.put(key4, value4);
        if (key5 != null) context.put(key5, value5);
        return context;
    }
}
