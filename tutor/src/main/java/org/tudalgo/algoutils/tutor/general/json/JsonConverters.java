package org.tudalgo.algoutils.tutor.general.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 * A class that contains methods to convert {@linkplain JsonNode JsonNodes} to java objects.
 */
public class JsonConverters {

    /**
     * Converts a {@linkplain JsonNode} to a {@linkplain List} of {@code T}s by applying the given
     * {@linkplain Function mapper} to each element of the json node.
     *
     * @param jsonNode the json node to convert.
     * @param mapper   the mapper that is applied to each element of the json node.
     */
    public static <T> List<T> toList(final JsonNode jsonNode, final Function<JsonNode, T> mapper) {
        return StreamSupport.stream(jsonNode.spliterator(), false)
            .map(mapper)
            .toList();
    }

    /**
     * Converts a {@linkplain JsonNode} to a {@linkplain Map} of {@code K}s and {@code V}s by applying the given
     * {@linkplain Function keyMapper} to each key and the {@linkplain Function valueMapper} to each value of the json
     * node.
     *
     * @param jsonNode    the json node to convert.
     * @param keyMapper   the mapper that is applied to each key of the json node.
     * @param valueMapper the mapper that is applied to each value of the json node.
     * @param <K>         the type of the keys of the map.
     * @param <V>         the type of the values of the map.
     * @return the converted map.
     */
    public static <K, V> Map<K, V> toMap(
        final JsonNode jsonNode,
        final Function<String, K> keyMapper,
        final Function<JsonNode, V> valueMapper
    ) {
        final var map = new HashMap<K, V>();
        jsonNode.fields().forEachRemaining(entry -> map.put(
            keyMapper.apply(entry.getKey()),
            valueMapper.apply(entry.getValue())
        ));
        return map;
    }

    /**
     * Converts a {@linkplain JsonNode} to a {@linkplain Map} of {@code Integer}s and {@code Integer}s by parsing the
     * keys and values of the json node.
     *
     * @param jsonNode the json node to convert.
     * @return the converted map.
     */
    public static Map<Integer, Integer> toIntMap(final JsonNode jsonNode) {
        return toMap(jsonNode, Integer::parseInt, JsonNode::asInt);
    }

    /**
     * The default converters that are used if no custom converters are given.
     */
    public static final Map<String, Function<JsonNode, ?>> DEFAULT_CONVERTERS = Map.ofEntries(
    );

    /**
     * Converts a {@linkplain JsonNode} to a {@code T} by applying the given {@linkplain Function mapper} to the json
     * node.
     *
     * @param node         the json node to convert.
     * @param key          the key of the json node.
     * @param type         the type of the object to convert to.
     * @param objectMapper the object mapper to use
     * @param converters   the custom converters to use
     * @param <T>          the type of the object to convert to.
     * @return the converted object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(
        final JsonNode node,
        final String key,
        final Class<T> type,
        final ObjectMapper objectMapper,
        final Map<String, Function<JsonNode, ?>> converters
    ) {
        if (node == null) {
            return null;
        }
        if (key != null && key.length() > 0) {
            final var converter = converters.get(key);
            if (converter != null && (type == null || type.isAssignableFrom(converter.apply(node).getClass()))) {
                return (T) converter.apply(node);
            }
        }
        return Assertions
            .assertDoesNotThrow(
                () ->
                    objectMapper.treeToValue(
                        node,
                        Objects.requireNonNullElseGet(type, () -> (Class<T>) Object.class)
                    ),
                "Invalid JSON Source."
            );
    }

    /**
     * Converts a {@linkplain JsonNode} to a {@code T} by applying the given {@linkplain Function mapper} to the json
     * node.
     *
     * @param node         the json node to convert.
     * @param key          the key of the json node.
     * @param type         the type of the object to convert to.
     * @param objectMapper the object mapper to use.
     * @param <T>          the type of the object to convert to.
     * @return the converted object.
     */
    public <T> T convert(final JsonNode node, final String key, final Class<T> type, final ObjectMapper objectMapper) {
        return convert(node, key, type, objectMapper, DEFAULT_CONVERTERS);
    }
}
