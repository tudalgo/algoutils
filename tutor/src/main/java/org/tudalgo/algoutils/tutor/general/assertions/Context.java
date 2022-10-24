package org.tudalgo.algoutils.tutor.general.assertions;

import java.util.Collection;

/**
 * <p>A type representing the context of a test.</p>
 * <p>A context consists of its subject (for example, class, an attribute or an method) and a collection of properties.</p>
 *
 * @author Dustin Glaser
 */
public interface Context {

    /**
     * Returns all properties of this context.
     *
     * @return all properties of this context
     */
    Collection<Property> properties();

    /**
     * Returns the subject of this context.
     *
     * @return the subject of this context
     */
    Object subject();

    /**
     * <p>A builder for {@linkplain Context contexts}.</p>
     *
     * @param <BT> the type of the builder
     */
    interface Builder<BT extends Builder<BT>> {

        /**
         * Returns a {@link Context} with the given properties.
         *
         * @return the context
         */
        Context build();

        /**
         * Sets the value for the given key.
         *
         * @param key   the key
         * @param value the value
         * @return this builder
         * @deprecated use {@link #add(String, Object)} instead
         */
        @Deprecated
        default BT property(String key, Object value) {
            return add(key, value);
        }

        BT add(String key, Object value);

        BT add(Property... property);

        BT add(Context... context);

        /**
         * Sets the subject of this context.
         *
         * @param subject the subject
         * @return this builder
         */
        BT subject(Object subject);

        /**
         * <p>A factory for {@linkplain Builder builders}.</p>
         *
         * @param <BT> the type of the builder
         */
        interface Factory<BT extends Builder<BT>> {

            /**
             * Returns a new builder.
             *
             * @return a new builder
             */
            Builder<BT> builder();
        }
    }
}
