package com.gasis.rts.ui.component.container;

import com.gasis.rts.ui.component.Component;

/**
 * A component that contains other components
 */
public interface ContainerInterface {

    /**
     * Adds a component to the container
     *
     * @param component component to add
     */
    void add(Component component);

    /**
     * Removes a component from the container
     *
     * @param component component to remove
     */
    void remove(Component component);

    /**
     * Called when the mouse is moved
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    void mouseMoved(float x, float y);

    /**
     * Called when the mouse if pressed down
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    void mouseDown(float x, float y);

    /**
     * Called when the mouse is released
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    void mouseUp(float x, float y);

    /**
     * Called when the mouse is scrolled
     *
     * @param amount scroll amount
     */
    void mouseScrolled(int amount);

    /**
     * Sets the container's vertical scrolling flag
     *
     * @param scrollable is the container vertically scrollable
     */
    void setVerticallyScrollable(boolean scrollable);

    /**
     * Sets the container's horizontal scrolling flag
     *
     * @param scrollable is the container horizontally scrollable
     */
    void setHorizontallyScrollable(boolean scrollable);
}
