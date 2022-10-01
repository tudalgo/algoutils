package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.assertions.Property;

public record BasicProperty(
    String key,
    Object value
) implements Property {
}
