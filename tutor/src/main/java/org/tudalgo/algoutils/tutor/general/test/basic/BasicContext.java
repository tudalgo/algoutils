package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.test.Context;
import org.tudalgo.algoutils.tutor.general.test.Property;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicContext implements Context {

    private final Object subject;

    private final List<Property> properties;

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

    public static final class Builder implements Context.Builder {

        private final List<Property> properties = new LinkedList<>();
        private Object subject;

        private Builder() {

        }

        @Override
        public Context build() {
            return new BasicContext(subject, properties);
        }

        @Override
        public Context.Builder property(String key, Object value) {
            if (key != null) {
                properties.add(new BasicProperty(key, value));
            }
            return this;
        }

        @Override
        public Context.Builder subject(Object subject) {
            this.subject = subject;
            return this;
        }

        public static class Factory implements Context.Builder.Factory {

            @Override
            public Context.Builder builder() {
                return new BasicContext.Builder();
            }
        }
    }
}