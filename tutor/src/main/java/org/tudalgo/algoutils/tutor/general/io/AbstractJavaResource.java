package org.tudalgo.algoutils.tutor.general.io;

import java.nio.file.Path;
import java.util.Objects;

/**
 * An abstract implementation of {@link JavaResource} which contains a source path to the Java resource.
 *
 * @author Nhan Huynh
 */
public abstract class AbstractJavaResource implements JavaResource {

    /**
     * The source of the Java resource.
     */
    protected final Path source;

    /**
     * Constructs a new basic Java resource object.
     *
     * @param source the source of the Java resource
     */
    protected AbstractJavaResource(Path source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractJavaResource entries)) return false;
        return Objects.equals(source, entries.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source);
    }
}
