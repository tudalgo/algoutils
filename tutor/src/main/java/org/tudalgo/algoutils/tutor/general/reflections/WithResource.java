package org.tudalgo.algoutils.tutor.general.reflections;

/**
 * An interface for links to identify if the source code is available.
 *
 * @author Nhan Huynh
 */
public interface WithResource {

    /**
     * Returns {@code true} if the source code is available for this link.
     *
     * @return {@code true} if the source code is available for this link
     */
    boolean isResourceAvailable();

}
