package org.tudalgo.algoutils.tutor.general.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public abstract class AbstractArrayNodeConversion<T> implements ArgumentConverter {

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof ArrayNode arrayNode)) {
            throw new ArgumentConversionException("Input type is not a JSON array");
        } else if (!context.getParameter().getType().isArray()) {
            throw new ArgumentConversionException("Parameter type is not an array type");
        } else {
            return convert(arrayNode, context);
        }
    }

    /**
     * Converts a JSON array into another type.
     *
     * @param arrayNode the JSON array node to be converted
     * @param context   context in which the parameter is converted
     * @return a converted object
     * @throws ArgumentConversionException if the conversion fails
     */
    public abstract T[] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException;

    /**
     * Quickly converts elements in a JSON array by mapping its nodes to another value and collecting them in an array.
     *
     * @param arrayNode       the JSON array node to take the nodes from
     * @param mappingFunction mapping function to use on the array nodes
     * @param generator       a function to create the array for the mapped values
     * @return an array with type {@link T} that contains the mapped values
     */
    T[] mappingConversion(ArrayNode arrayNode, Function<JsonNode, T> mappingFunction, IntFunction<T[]> generator) {
        Stream.Builder<JsonNode> streamBuilder = Stream.builder();
        arrayNode.forEach(streamBuilder::add);
        return streamBuilder.build()
            .map(mappingFunction)
            .toArray(generator);
    }
}
