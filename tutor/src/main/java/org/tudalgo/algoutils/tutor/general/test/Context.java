package org.tudalgo.algoutils.tutor.general.test;

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

    interface Builder<CT extends Context, BT extends Builder<CT, BT>> {

        CT build();

        Builder<CT, BT> property(String key, Object value);

        Builder<CT, BT> subject(Object subject);

        interface Factory<CT extends Context, BT extends Builder<CT, BT>> {

            Builder<CT, BT> builder();
        }
    }
}
