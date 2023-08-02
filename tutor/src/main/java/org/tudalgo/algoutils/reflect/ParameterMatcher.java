package org.tudalgo.algoutils.reflect;


/**
 * A parameter matcher based on {@link IdentifierMatcher}.
 * This class is deprecated and will be removed in a future release.
 * Use the new API located in the {@link org.tudalgo.algoutils.tutor.general.reflections} package instead.
 *
 * @author Ruben Deisenroth
 * @see IdentifierMatcher
 */
@Deprecated(since = "0.7.0", forRemoval = true)
public class ParameterMatcher extends IdentifierMatcher {

    /**
     * The expected parameter type.
     */
    public Class<?> parameterType;
    /**
     * The indicator whether to also match subtypes derived from {@link #parameterType}.
     */
    public boolean allowSubTypes;

    /**
     * Constructs and initializes an attribute matcher to match the specified attribute.
     *
     * @param identifierName the identifier name to match
     * @param similarity     the minimum required similarity
     * @param parameterType  the expected parameter type
     * @param allowSubTypes  the indicator whether to also match subtypes derived from {@link
     *                       #parameterType}
     */
    public ParameterMatcher(final String identifierName, final double similarity, final Class<?> parameterType, final boolean allowSubTypes) {
        super(identifierName, similarity);
        this.parameterType = parameterType;
        this.allowSubTypes = allowSubTypes;
    }

    /**
     * Constructs and initializes an attribute matcher to match the specified attribute.
     *
     * @param identifierName the identifier name to match
     * @param similarity     the minimum required similarity
     * @param parameterType  the expected parameter type
     */
    public ParameterMatcher(final String identifierName, final double similarity, final Class<?> parameterType) {
        super(identifierName, similarity);
        this.parameterType = parameterType;
    }

    /**
     * Constructs and initializes an attribute matcher to match the specified attribute.
     *
     * @param parameterType the expected parameter type
     */
    public ParameterMatcher(final Class<?> parameterType) {
        this(null, 0, parameterType);
    }

    /**
     * Constructs and initializes an attribute matcher to match the specified attribute.
     *
     * @param parameterType the expected parameter type
     * @param allowSubTypes the indicator whether to also match subtypes derived from {@link
     *                      #parameterType}
     */
    public ParameterMatcher(final Class<?> parameterType, final boolean allowSubTypes) {
        this(null, 0, parameterType, allowSubTypes);
    }
}
