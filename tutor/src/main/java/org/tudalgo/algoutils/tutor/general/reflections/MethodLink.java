package org.tudalgo.algoutils.tutor.general.reflections;

import org.tudalgo.algoutils.tutor.general.match.Identifiable;

import java.lang.reflect.Method;

/**
 * <p>An interface used to simply access methods.</p>
 *
 * @author Dustin Glaser
 */
public interface MethodLink extends WithModifiers, WithName, WithType, WithTypeList, WithImports, Identifiable {

    /**
     * <p>Returns the return type of the method linked by this method link.</p>
     *
     * @return the return type
     */
    TypeLink returnType();

    /**
     * <p>Invokes the method linked by this method link on the given instance with the given arguments.</p>
     *
     * @param instance  the instance
     * @param arguments the arguments
     * @param <T>       the type of the return value
     *
     * @return the return value
     */
    <T> T invoke(Object instance, Object... arguments) throws Exception;

    /**
     * <p>Invokes the method linked by this method link with the given arguments.</p>
     *
     * @param arguments the arguments
     * @param <T>       the type of the return value
     *
     * @return the return value
     */
    <T> T invokeStatic(Object... arguments) throws Exception;


    @Override
    default Kind kind() {
        return Kind.METHOD;
    }

    /**
     * Returns the actual {@linkplain Method method} behind this method link.
     *
     * @return the method
     */
    Method reflection();

    @Override
    default String name() {
        return reflection().getName();
    }

    @Override
    default int modifiers() {
        return reflection().getModifiers();
    }

}
