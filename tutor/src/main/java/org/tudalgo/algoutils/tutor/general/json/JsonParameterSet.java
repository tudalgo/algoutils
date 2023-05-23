package org.tudalgo.algoutils.tutor.general.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 * A set of test parameters that are stored in a json file.
 */
public class JsonParameterSet {
    /**
     * The root node of the json file.
     */
    private final JsonNode rootNode;

    /**
     * The object mapper that is used to convert the json nodes to java objects.
     */
    private final ObjectMapper objectMapper;

    /**
     * The custom converters that are used to convert the json nodes to java objects.
     */
    private final Map<String, Function<JsonNode, ?>> customConverters;

    /**
     * Creates a new {@link JsonParameterSet}.
     *
     * @param rootNode         the root node of the json file.
     * @param objectMapper     the object mapper that is used to convert the json nodes to java objects.
     * @param customConverters the custom converters that are used to convert the json nodes to java objects.
     */
    public JsonParameterSet(
        final JsonNode rootNode,
        final ObjectMapper objectMapper,
        final Map<String, Function<JsonNode, ?>> customConverters
    ) {
        this.rootNode = rootNode;
        this.objectMapper = objectMapper;
        this.customConverters = customConverters;
    }

    /**
     * Returns the root node of the json file.
     *
     * @return the root node of the json file.
     */
    public JsonNode getRootNode() {
        return rootNode;
    }

    /**
     * Returns the {@link ObjectMapper} that is used to convert the json nodes to java objects.
     *
     * @return the {@link ObjectMapper} that is used to convert the json nodes to java objects.
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Retrieves the value of the given key from the json file and converts it to the given type.
     *
     * <p>if the given type is null, it is assumed that a {@linkplain #customConverters custom converter}
     * for the given key exists which maps the json node to the desired type.</p>
     *
     * @param key  the key of the value.
     * @param type the class of the type to which the value should be converted.
     * @param <T>  the type to which the value should be converted.
     * @return the value of the given key from the json file converted to the given type.
     * @throws IllegalArgumentException if the given key does not exist in the json file.
     */
    public <T> T get(final String key, final Class<T> type) {
        final var jsonNode = getRootNode().get(key);
        if (jsonNode == null) {
            throw new IllegalArgumentException("The given key does not exist in the json node. The Key: " + key);
        }
        return JsonConverters.convert(
            jsonNode,
            key,
            type,
            getObjectMapper(),
            Objects.requireNonNullElse(customConverters, JsonConverters.DEFAULT_CONVERTERS)
        );
    }

    /**
     * Retrieves the value of the given key from the json file.
     *
     * @param key the key of the value.
     * @param <T> the type of the value.
     * @return the value of the given key from the json file.
     */
    public <T> T get(final String key) {
        return get(key, null);
    }

    /**
     * Retrieves the value of the given key from the json file and converts it to a boolean.
     *
     * @param key the key of the value.
     * @return the value of the given key from the json file converted to a boolean.
     */
    public boolean getBoolean(final String key) {
        return get(key, Boolean.class);
    }

    /**
     * Retrieves the value of the given key from the json file and converts it to a byte.
     *
     * @param key the key of the value.
     * @return the value of the given key from the json file converted to a byte.
     */
    public byte getByte(final String key) {
        return get(key, Byte.class);
    }

    /**
     * Retrieves the value of the given key from the json file and converts it to a short.
     *
     * @param key the key of the value.
     * @return the value of the given key from the json file converted to a short.
     */
    public short getShort(final String key) {
        return get(key, Short.class);
    }

    /**
     * Retrieves the value of the given key from the json file and converts it to an int.
     *
     * @param key the key of the value.
     * @return the value of the given key from the json file converted to an int.
     */
    public int getInt(final String key) {
        return get(key, Integer.class);
    }

    /**
     * Retrieves the value of the given key from the json file and converts it to a long.
     *
     * @param key the key of the value.
     * @return the value of the given key from the json file converted to a long.
     */
    public long getLong(final String key) {
        return get(key, Long.class);
    }

    /**
     * Retrieves the value of the given key from the json file and converts it to a float.
     *
     * @param key the key of the value.
     * @return the value of the given key from the json file converted to a float.
     */
    public float getFloat(final String key) {
        return get(key, Float.class);
    }

    /**
     * Retrieves the value of the given key from the json file and converts it to a double.
     *
     * @param key the key of the value.
     * @return the value of the given key from the json file converted to a double.
     */
    public double getDouble(final String key) {
        return get(key, Double.class);
    }

    /**
     * Retrieves the value of the given key from the json file and converts it to a char.
     *
     * @param key the key of the value.
     * @return the value of the given key from the json file converted to a char.
     */
    public char getChar(final String key) {
        return get(key, Character.class);
    }

    /**
     * Retrieves the value of the given key from the json file and converts it to a {@link String}.
     *
     * @param key the key of the value.
     * @return the value of the given key from the json file converted to a {@link String}.
     */
    public String getString(final String key) {
        return get(key, String.class);
    }

    /**
     * Returns a list of all available keys in the json file.
     *
     * @return a list of all available keys in the json file.
     */
    public List<String> availableKeys() {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(rootNode.fields(), Spliterator.ORDERED),
                false
            )
            .map(Map.Entry::getKey)
            .toList();
    }

    /**
     * Returns a {@link Context} containing all available keys and their values. Useful for writing Tests.
     * <p>If some keys should not be visible in the output, they can be ignored by passing them as parameters.</p>
     *
     * @param ignoreKeys keys to ignore for the output
     * @return a {@link Context} containing all available keys and their values.
     */
    public Context toContext(final String... ignoreKeys) {
        final var builder = Assertions2.contextBuilder();
        for (final var key : availableKeys()) {
            if (Arrays.asList(ignoreKeys).contains(key)) {
                continue;
            }
            builder.add(key, get(key));
        }
        return builder.build();
    }
}
