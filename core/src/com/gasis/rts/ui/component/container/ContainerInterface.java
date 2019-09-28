package com.gasis.rts.ui.component.container;

import com.gasis.rts.ui.component.ComponentInterface;

/**
 * A component that contains other components
 */
public interface ContainerInterface {

    /**
     * Adds a component to the container
     *
     * @param component component to add
     */
    void add(ComponentInterface component);

    /**
     * Removes a component from the container
     *
     * @param component component to remove
     */
    void remove(ComponentInterface component);
}
