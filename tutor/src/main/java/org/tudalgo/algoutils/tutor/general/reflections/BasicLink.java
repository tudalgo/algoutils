package org.tudalgo.algoutils.tutor.general.reflections;

public abstract class BasicLink implements Link {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicLink basicLink = (BasicLink) o;
        return reflection().equals(basicLink.reflection());
    }
}
