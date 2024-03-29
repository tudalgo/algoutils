package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.Property;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>A basic implementation of a context.</p>
 */
public class BasicContext implements Context {

    private final Object subject;
    private final List<Property> properties;

    /**
     * <p>Constructs a new context with the given subject and collection of properties.</p>
     *
     * @param subject    the subject
     * @param properties the properties
     */
    private BasicContext(Object subject, Collection<Property> properties) {
        this.subject = subject;
        this.properties = properties.stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Property> properties() {
        return properties;
    }

    @Override
    public Object subject() {
        return subject;
    }

    public static final class Builder implements Context.Builder<Builder> {

        private final List<Property> properties = new LinkedList<>();
        private Object subject;

        private Builder() {

        }

        @Override
        public BasicContext build() {
            return new BasicContext(subject, properties);
        }

        @Override
        public Builder add(String key, Object value) {
            return add(new BasicProperty(key, value));
        }

        @Override
        public Builder add(Property... properties) {
            for (var property : properties) {
                var index = this.properties.indexOf(property);
                if (index == -1) {
                    this.properties.add(property);
                } else {
                    this.properties.set(index, property);
                }
            }
            return this;
        }

        @Override
        public Builder add(Context... contexts) {
            for (var context : contexts) {
                context.properties().forEach(this::add);
            }
            return this;
        }

        @Override
        public Builder subject(Object subject) {
            this.subject = subject;
            return this;
        }

        public static class Factory implements Context.Builder.Factory<Builder> {

            @Override
            public Builder builder() {
                return new BasicContext.Builder();
            }
        }
    }
}
