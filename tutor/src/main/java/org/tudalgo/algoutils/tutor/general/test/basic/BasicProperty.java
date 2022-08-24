package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.test.Property;

public record BasicProperty(
    String key,
    Object value
) implements Property {
}
