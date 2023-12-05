package org.tudalgo.algoutils.student.test;

/**
 * Task is a functional interface that can be used to execute code that potentially throws a {@link Throwable}.
 */
@FunctionalInterface
public interface Task {

    /**
     * Executes the task.
     *
     * @throws Throwable the {@link Throwable} that was thrown during the execution
     */
    void execute() throws Throwable;
}
