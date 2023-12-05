package org.tudalgo.algoutils.student.test;

/**
 * This is a functional interface that can be used to execute a block of code that potentially throws a
 * {@link Throwable}.
 */
@FunctionalInterface
public interface Task {

    /**
     * Executes the task.
     *
     * @throws Throwable the potential throwable that is thrown by the task
     */
    void execute() throws Throwable;
}
