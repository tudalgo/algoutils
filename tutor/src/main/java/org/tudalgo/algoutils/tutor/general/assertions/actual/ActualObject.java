package org.tudalgo.algoutils.tutor.general.assertions.actual;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;

import static java.util.function.Function.identity;

/**
 * <p>A type representing the actual object in a test.</p>
 *
 * @author Dustin Glaser
 */
public interface ActualObject<T> extends Actual {

    /**
     * <p>Returns an instance representing a behavior where the given object was tested or,
     * if no object was given, an behavior where no object was returned.</p>
     *
     * @param object the object
     * @param <T>    the type of the object
     * @return the object behavior
     */
    static <T> ActualObject<T> of(T object, boolean successful, Function<String, String> formatter) {

        //noinspection DuplicatedCode
        return new ActualObject<>() {
            @Override
            public T behavior() {
                return object;
            }

            @Override
            public boolean successful() {
                return successful;
            }

            @Override
            public String string(Stringifier stringifier) {
                return formatter.apply(BRACKET_FORMATTER.apply(stringifier.stringify(this.behavior())));
            }
        };
    }

    static <T> ActualObject<T> of(T object, boolean successful) {
        return of(object, successful, identity());
    }

    /**
     * <p>Returns the object under test.</p>
     *
     * @return the object
     */
    @Override
    T behavior();

    /**
     * <p>Returns the object under test.</p>
     *
     * @return the object
     */
    default T object() {
        return behavior();
    }
}
