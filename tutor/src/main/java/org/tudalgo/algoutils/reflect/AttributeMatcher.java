package org.tudalgo.algoutils.reflect;

/**
 * An attribute Matcher based on {@link IdentifierMatcher}.
 *
 * @author Ruben Deisenroth
 * @see IdentifierMatcher
 */
public class AttributeMatcher extends IdentifierMatcher {

    /**
     * The expected access modifier.
     */
    public int modifier;
    /**
     * The expected attribute type.
     */
    public Class<?> type;
    /**
     * The indicator whether to also match super implementations.
     */
    public boolean allowSuperClass;

    /**
     * Constructs and initializes an attribute matcher to match the specified attribute.
     *
     * @param name            the attribute name to match
     * @param similarity      the minimum required similarity
     * @param modifier        the expected access modifier
     * @param type            the  expected attribute type
     * @param allowSuperClass the indicator whether to also match super implementations
     */
    public AttributeMatcher(final String name, final double similarity, final int modifier, final Class<?> type, final boolean allowSuperClass) {
        super(name, similarity);
        this.modifier = modifier;
        this.type = type;
        this.allowSuperClass = allowSuperClass;
    }

    /**
     * Constructs and initializes an attribute matcher to match the specified attribute with no
     * super implementation match.
     *
     * @param name       the attribute name to match
     * @param similarity the minimum required similarity
     * @param modifier   the expected access modifier
     * @param type       the  expected attribute type
     */
    public AttributeMatcher(final String name, final double similarity, final int modifier, final Class<?> type) {
        this(name, similarity, modifier, type, false);
    }

    /**
     * Constructs and initializes an attribute matcher to match the specified attribute.
     *
     * @param name            the attribute name to match
     * @param similarity      the minimum required similarity
     * @param type            the  expected attribute type
     * @param allowSuperClass the indicator whether to also match super implementations
     */
    public AttributeMatcher(final String name, final double similarity, final Class<?> type, final boolean allowSuperClass) {
        this(name, similarity, -1, type, allowSuperClass);
    }

    /**
     * Constructs and initializes an attribute matcher to match the specified attribute with no
     * super implementation match.
     *
     * @param name       the attribute name to match
     * @param similarity the minimum required similarity
     * @param type       the  expected attribute type
     */
    public AttributeMatcher(final String name, final double similarity, final Class<?> type) {
        this(name, similarity, -1, type);
    }
}
