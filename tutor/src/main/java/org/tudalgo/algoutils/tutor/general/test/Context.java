package org.tudalgo.algoutils.tutor.general.test;

import java.util.Collection;

public interface Context {

    Collection<Property> properties();

    Object subject();

    interface Builder {

        Context build();

        Builder property(String key, Object value);

        Builder subject(Object subject);

        interface Factory {

            Builder builder();
        }
    }
}
