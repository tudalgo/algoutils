package org.tudalgo.algoutils.tutor.general.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * A collection of simple {@link ArgumentConverter}s for using JSON arrays in JUnit tests.
 */
public class ArrayConverter {

    /**
     * Automatically convert {@link ArrayNode} to any supported parameter type.
     */
    public static class Auto implements ArgumentConverter {

        private static final Map<Class<?>, AbstractArrayNodeConversion<?>> CONVERSION_MAP = Map.ofEntries(
            Map.entry(java.math.BigDecimal.class, new BigDecimal()),
            Map.entry(java.math.BigInteger.class, new BigInteger()),
            Map.entry(java.lang.Boolean.class, new Boolean()),
            Map.entry(java.lang.Double.class, new Double()),
            Map.entry(java.lang.Float.class, new Float()),
            Map.entry(java.lang.Integer.class, new Integer()),
            Map.entry(java.lang.Long.class, new Long()),
            Map.entry(java.lang.Number.class, new Number()),
            Map.entry(java.lang.Short.class, new Short()),
            Map.entry(java.lang.String.class, new String()),
            Map.entry(java.math.BigDecimal[].class, new BigDecimal2D()),
            Map.entry(java.math.BigInteger[].class, new BigInteger2D()),
            Map.entry(java.lang.Boolean[].class, new Boolean2D()),
            Map.entry(java.lang.Double[].class, new Double2D()),
            Map.entry(java.lang.Float[].class, new Float2D()),
            Map.entry(java.lang.Integer[].class, new Integer2D()),
            Map.entry(java.lang.Long[].class, new Long2D()),
            Map.entry(java.lang.Number[].class, new Number2D()),
            Map.entry(java.lang.Short[].class, new Short2D()),
            Map.entry(java.lang.String[].class, new String2D())
    );

        @Override
        public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
            Class<?> parameterType = context.getParameter().getType();
            if (!(source instanceof ArrayNode arrayNode)) {
                throw new ArgumentConversionException("Input type is not a JSON array");
            } else if (!parameterType.isArray()) {
                throw new ArgumentConversionException("Parameter type is not an array type");
            } else if (!CONVERSION_MAP.containsKey(parameterType.componentType())) {
                throw new ArgumentConversionException("Parameter type %s is not supported".formatted(parameterType.getTypeName()));
            } else {
                return CONVERSION_MAP.get(parameterType.componentType()).convert(arrayNode, context);
            }
        }

    }

    /* ***************************************************************************************************************
     *                                                 1D array converters                                           *
     * ***************************************************************************************************************/

    /**
     * Quickly converts elements in a JSON array by mapping its nodes to another value and collecting them in an array.
     *
     * @param arrayNode       the JSON array node to take the nodes from
     * @param mappingFunction mapping function to use on the array nodes
     * @param generator       a function to create the array for the mapped values
     * @param <T>             the type of the array to create
     *
     * @return an array with type {@link T} that contains the mapped values
     */
    private static <T> T[] mappingConversion1D(ArrayNode arrayNode, Function<JsonNode, T> mappingFunction, IntFunction<T[]> generator) {
        Stream.Builder<JsonNode> streamBuilder = Stream.builder();
        arrayNode.forEach(streamBuilder::add);
        return streamBuilder.build().map(mappingFunction).toArray(generator);
    }

    public static class BigDecimal extends AbstractArrayNodeConversion<java.math.BigDecimal> {

        @Override
        public java.math.BigDecimal[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion1D(arrayNode, JsonNode::decimalValue, java.math.BigDecimal[]::new);
        }

    }

    public static class BigInteger extends AbstractArrayNodeConversion<java.math.BigInteger> {

        @Override
        public java.math.BigInteger[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion1D(arrayNode, JsonNode::bigIntegerValue, java.math.BigInteger[]::new);
        }

    }

    public static class Boolean extends AbstractArrayNodeConversion<java.lang.Boolean> {

        @Override
        public java.lang.Boolean[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion1D(arrayNode, JsonNode::booleanValue, java.lang.Boolean[]::new);
        }

    }

    public static class Double extends AbstractArrayNodeConversion<java.lang.Double> {

        @Override
        public java.lang.Double[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion1D(arrayNode, JsonNode::doubleValue, java.lang.Double[]::new);
        }

    }

    public static class Float extends AbstractArrayNodeConversion<java.lang.Float> {

        @Override
        public java.lang.Float[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion1D(arrayNode, JsonNode::floatValue, java.lang.Float[]::new);
        }

    }

    public static class Integer extends AbstractArrayNodeConversion<java.lang.Integer> {

        @Override
        public java.lang.Integer[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion1D(arrayNode, JsonNode::intValue, java.lang.Integer[]::new);
        }

    }

    public static class Long extends AbstractArrayNodeConversion<java.lang.Long> {

        @Override
        public java.lang.Long[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion1D(arrayNode, JsonNode::longValue, java.lang.Long[]::new);
        }

    }

    public static class Number extends AbstractArrayNodeConversion<java.lang.Number> {

        @Override
        public java.lang.Number[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion1D(arrayNode, JsonNode::numberValue, java.lang.Number[]::new);
        }

    }

    public static class Short extends AbstractArrayNodeConversion<java.lang.Short> {

        @Override
        public java.lang.Short[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion1D(arrayNode, JsonNode::shortValue, java.lang.Short[]::new);
        }

    }

    public static class String extends AbstractArrayNodeConversion<java.lang.String> {

        @Override
        public java.lang.String[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion1D(arrayNode, JsonNode::textValue, java.lang.String[]::new);
        }

    }


    /* ***************************************************************************************************************
     *                                                 2D array converters                                           *
     * ***************************************************************************************************************/

    /**
     * Quickly converts elements in a JSON nd-array by mapping its nodes to another value and collecting them in an
     * array.
     *
     * @param conversion the conversion to use for creating the nd-array
     * @param arrayNode  the JSON array node to take the nodes from
     * @param generator  a function to create the array for the mapped values
     * @param context    context in which the parameter is converted
     * @param <T>        the type of the array to create
     *
     * @return an array with type {@link T} that contains the mapped values
     */
    @SuppressWarnings("unchecked")
    private static <T> T[] mappingConversionNd(AbstractArrayNodeConversion<T> conversion, ArrayNode arrayNode, IntFunction<T[]> generator, ParameterContext context) {
        Stream.Builder<JsonNode> streamBuilder = Stream.builder();
        arrayNode.forEach(streamBuilder::add);
        return streamBuilder.build().map(node -> {
            AbstractArrayNodeConversion<?> converter = conversion.getBaseConverter();
            // Only null for 1D arrays
            assert converter != null;
            return converter.convert(node, context);
        }).map(o -> (T) o).toArray(generator);
    }

    public static class BigDecimal2D extends AbstractArrayNodeConversion<java.math.BigDecimal[]> {

        public BigDecimal2D() {
            super(new BigDecimal());
        }

        @Override
        public java.math.BigDecimal[][] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException {
            return mappingConversionNd(this, arrayNode, java.math.BigDecimal[][]::new, context);
        }

    }

    public static class BigInteger2D extends AbstractArrayNodeConversion<java.math.BigInteger[]> {

        public BigInteger2D() {
            super(new BigDecimal());
        }

        @Override
        public java.math.BigInteger[][] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException {
            return mappingConversionNd(this, arrayNode, java.math.BigInteger[][]::new, context);
        }

    }

    public static class Boolean2D extends AbstractArrayNodeConversion<java.lang.Boolean[]> {

        public Boolean2D() {
            super(new Boolean());
        }

        @Override
        public java.lang.Boolean[][] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException {
            return mappingConversionNd(this, arrayNode, java.lang.Boolean[][]::new, context);
        }

    }

    public static class Double2D extends AbstractArrayNodeConversion<java.lang.Double[]> {

        public Double2D() {
            super(new Double());
        }

        @Override
        public java.lang.Double[][] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException {
            return mappingConversionNd(this, arrayNode, java.lang.Double[][]::new, context);
        }

    }

    public static class Float2D extends AbstractArrayNodeConversion<java.lang.Float[]> {

        public Float2D() {
            super(new Float());
        }

        @Override
        public java.lang.Float[][] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException {
            return mappingConversionNd(this, arrayNode, java.lang.Float[][]::new, context);
        }

    }

    public static class Integer2D extends AbstractArrayNodeConversion<java.lang.Integer[]> {

        public Integer2D() {
            super(new Integer());
        }

        @Override
        public java.lang.Integer[][] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException {
            return mappingConversionNd(this, arrayNode, java.lang.Integer[][]::new, context);
        }

    }

    public static class Long2D extends AbstractArrayNodeConversion<java.lang.Long[]> {

        public Long2D() {
            super(new Long());
        }

        @Override
        public java.lang.Long[][] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException {
            return mappingConversionNd(this, arrayNode, java.lang.Long[][]::new, context);
        }

    }

    public static class Number2D extends AbstractArrayNodeConversion<java.lang.Number[]> {

        public Number2D() {
            super(new Number());
        }

        @Override
        public java.lang.Number[][] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException {
            return mappingConversionNd(this, arrayNode, java.lang.Number[][]::new, context);
        }

    }

    public static class Short2D extends AbstractArrayNodeConversion<java.lang.Short[]> {

        public Short2D() {
            super(new Short());
        }

        @Override
        public java.lang.Short[][] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException {
            return mappingConversionNd(this, arrayNode, java.lang.Short[][]::new, context);
        }

    }

    public static class String2D extends AbstractArrayNodeConversion<java.lang.String[]> {

        public String2D() {
            super(new String());
        }

        @Override
        public java.lang.String[][] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException {
            return mappingConversionNd(this, arrayNode, java.lang.String[][]::new, context);
        }

    }

}
