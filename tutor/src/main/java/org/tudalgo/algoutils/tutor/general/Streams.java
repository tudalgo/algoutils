package org.tudalgo.algoutils.tutor.general;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A collection of utilities for working with streams.
 *
 * @author Nhan Huynh
 */
public final class Streams {

    /**
     * Prevents instantiation.
     */
    private Streams() {
    }

    /**
     * Converts an iterator to a sequential stream.
     *
     * @param iterator the iterator to convert
     * @param <T>      the type of the elements
     * @return the sequential stream of the iterator
     */
    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return stream(iterator, false);
    }

    /**
     * Converts an iterator to a stream.
     *
     * @param iterator the iterator to convert
     * @param parallel whether the stream should be parallel
     * @param <T>      the type of the elements
     * @return the stream of the iterator
     */
    public static <T> Stream<T> stream(Iterator<T> iterator, boolean parallel) {
        Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }
}
