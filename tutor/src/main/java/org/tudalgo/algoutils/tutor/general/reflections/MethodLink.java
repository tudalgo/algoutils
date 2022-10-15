package org.tudalgo.algoutils.tutor.general.reflections;

import org.tudalgo.algoutils.tutor.general.match.Identifiable;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>An interface used to simply access methods.</p>
 *
 * @author Dustin Glaser
 */
public interface MethodLink extends Identifiable {

    /**
     * <p>Returns the parameter types of the method linked by this method link.</p>
     *
     * @return the parameter types
     */
    List<? extends TypeLink> getParameterList();

    /**
     * <p>Returns the return type of the method linked by this method link.</p>
     *
     * @return the return type
     */
    TypeLink getReturnType();

    /**
     * <p>Invokes the method linked by this method link with the given arguments.</p>
     *
     * @param arguments the arguments
     * @param <T>       the type of the return value
     * @return the return value
     */
    <T> T invoke(Object... arguments);

    /**
     * <p>Invokes the method linked by this method link with the given arguments.</p>
     *
     * @param arguments the arguments
     * @param <T>       the type of the return value
     * @param <E>       the type of all potential exceptions
     * @return the return value
     */
    <T, E extends Exception> T invokeExceptional(Object... arguments) throws E;

    /**
     * <p>Invokes the method linked by this method link on the given instance with the given arguments.</p>
     *
     * @param instance  the instance
     * @param arguments the arguments
     * @param <T>       the type of the return value
     * @return the return value
     */
    <T> T invokeStatic(Object instance, Object... arguments);

    /**
     * <p>Invokes the method linked by this method link on the given instance with the given arguments.</p>
     *
     * @param instance  the instance
     * @param arguments the arguments
     * @param <T>       the type of the return value
     * @param <E>       the type of all potential exceptions
     * @return the return value
     */
    <T, E extends Exception> T invokeStaticExceptional(Object instance, Object... arguments) throws E;

    /**
     * Returns the actual {@linkplain Method method} behind this method link.
     *
     * @return the method
     */
    Method link();
}
