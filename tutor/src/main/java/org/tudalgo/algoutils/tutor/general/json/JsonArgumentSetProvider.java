package org.tudalgo.algoutils.tutor.general.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This class is used to provide arguments to a test method annotated with {@link JsonParameterSetTest}.
 */
public class JsonArgumentSetProvider implements ArgumentsProvider, AnnotationConsumer<JsonParameterSetTest> {

    /**
     * The path to the JSON file.
     *
     * @see JsonParameterSetTest#value()
     */
    private String jsonPath;
    /**
     * The name of the field in the test class that contains a map of custom converters.
     *
     * @see JsonParameterSetTest#customConverters()
     */
    private String customConvertersFieldName;

    @Override
    public void accept(final JsonParameterSetTest jsonParameterSetTest) {
        jsonPath = jsonParameterSetTest.value();
        customConvertersFieldName = jsonParameterSetTest.customConverters();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final var testClass = context.getRequiredTestClass();
        Map<String, Function<JsonNode, ?>> customConverters = null;
        if (!customConvertersFieldName.isBlank()) {
            customConverters = (Map<String, Function<JsonNode, ?>>) testClass
                .getField(customConvertersFieldName)
                .get(context.getTestInstance().orElse(null));
        }
        final var mapper = new ObjectMapper();
        final var url = testClass.getResource(jsonPath);
        Assertions.assertNotNull(url, "Could not find JSON file: " + jsonPath);
        final var rootNode = mapper.readTree(url);
        Assertions.assertTrue(rootNode.isArray(), "The root node of the JSON file should be an array.");
        final var argumentSets = new ArrayList<Arguments>();
        for (final var jsonNode : rootNode) {
            argumentSets.add(Arguments.of(new JsonParameterSet(jsonNode, mapper, customConverters)));
        }
        return argumentSets.stream();
    }
}
