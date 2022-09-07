package org.tudalgo.algoutils.tutor.general.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.util.Map;

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
            Map.entry(java.lang.String.class, new String())
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

    public static class BigDecimal extends AbstractArrayNodeConversion<java.math.BigDecimal> {
        @Override
        public java.math.BigDecimal[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion(arrayNode, JsonNode::decimalValue, java.math.BigDecimal[]::new);
        }
    }

    public static class BigInteger extends AbstractArrayNodeConversion<java.math.BigInteger> {
        @Override
        public java.math.BigInteger[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion(arrayNode, JsonNode::bigIntegerValue, java.math.BigInteger[]::new);
        }
    }

    public static class Boolean extends AbstractArrayNodeConversion<java.lang.Boolean> {
        @Override
        public java.lang.Boolean[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion(arrayNode, JsonNode::booleanValue, java.lang.Boolean[]::new);
        }
    }

    public static class Double extends AbstractArrayNodeConversion<java.lang.Double> {
        @Override
        public java.lang.Double[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion(arrayNode, JsonNode::doubleValue, java.lang.Double[]::new);
        }
    }

    public static class Float extends AbstractArrayNodeConversion<java.lang.Float> {
        @Override
        public java.lang.Float[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion(arrayNode, JsonNode::floatValue, java.lang.Float[]::new);
        }
    }

    public static class Integer extends AbstractArrayNodeConversion<java.lang.Integer> {
        @Override
        public java.lang.Integer[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion(arrayNode, JsonNode::intValue, java.lang.Integer[]::new);
        }
    }

    public static class Long extends AbstractArrayNodeConversion<java.lang.Long> {
        @Override
        public java.lang.Long[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion(arrayNode, JsonNode::longValue, java.lang.Long[]::new);
        }
    }

    public static class Number extends AbstractArrayNodeConversion<java.lang.Number> {
        @Override
        public java.lang.Number[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion(arrayNode, JsonNode::numberValue, java.lang.Number[]::new);
        }
    }

    public static class Short extends AbstractArrayNodeConversion<java.lang.Short> {
        @Override
        public java.lang.Short[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion(arrayNode, JsonNode::shortValue, java.lang.Short[]::new);
        }
    }

    public static class String extends AbstractArrayNodeConversion<java.lang.String> {
        @Override
        public java.lang.String[] convert(ArrayNode arrayNode, ParameterContext context) {
            return mappingConversion(arrayNode, JsonNode::textValue, java.lang.String[]::new);
        }
    }
}
