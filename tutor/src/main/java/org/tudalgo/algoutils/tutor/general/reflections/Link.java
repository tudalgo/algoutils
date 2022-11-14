package org.tudalgo.algoutils.tutor.general.reflections;

public interface Link {

    Object reflection();

    Kind kind();

    /**
     * <p>An enumeration of kinds of links.</p>
     *
     * @author Dustin Glaser
     */
    enum Kind {
        PACKAGE,
        CLASS,
        INTERFACE,
        ENUM,
        RECORD,
        FIELD,
        CONSTRUCTOR,
        METHOD,
        PRIMITIVE,
        ARRAY,
        ANNOTATION
    }
}
