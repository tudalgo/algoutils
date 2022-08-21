package org.tudalgo.algoutils.tutor.general.test;

import java.util.Collection;

public interface Context {

    Collection<Property> properties();

    Object property(String key);

    Object subject();
}
