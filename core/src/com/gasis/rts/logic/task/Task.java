package com.gasis.rts.logic.task;

/**
 * A task that can be executed and reverted
 */
public interface Task {

    /**
     * Executes the task
     */
    void execute();

    /**
     * Reverts the task
     */
    void revert();
}
