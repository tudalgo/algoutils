package org.tudalgo.algoutils.tutor.general.conversion;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import javax.annotation.Nullable;

public abstract class AbstractArrayNodeConversion<T> implements ArgumentConverter {

    /**
     * Base conversion (n-1)d array to n-d array. The base conversion is null for n=1.
     */
    protected final @Nullable AbstractArrayNodeConversion<?> baseConverter;

    public AbstractArrayNodeConversion() {
        this(null);
    }

    public AbstractArrayNodeConversion(@Nullable AbstractArrayNodeConversion<?> baseConverter) {
        this.baseConverter = baseConverter;
    }

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

    @Nullable
    public AbstractArrayNodeConversion<?> getBaseConverter() {
        return baseConverter;
    }

    /**
     * Converts a JSON array into another type.
     *
     * @param arrayNode the JSON array node to be converted
     * @param context   context in which the parameter is converted
     *
     * @return a converted object
     *
     * @throws ArgumentConversionException if the conversion fails
     */
    public abstract T[] convert(ArrayNode arrayNode, ParameterContext context) throws ArgumentConversionException;

}
