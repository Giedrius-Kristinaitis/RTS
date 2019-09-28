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
    boolean mouseMoved(float x, float y);

    /**
     * Called when the mouse if pressed down
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    boolean mouseDown(float x, float y);

    /**
     * Called when the mouse is released
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    boolean mouseUp(float x, float y);

    /**
     * Called when the mouse is scrolled
     *
     * @param amount scroll amount
     */
    boolean mouseScrolled(int amount);

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

    /**
     * Checks if the container is vertically scrollable
     *
     * @return
     */
    boolean isVerticallyScrollable();

    /**
     * Checks if the container is horizontally scrollable
     *
     * @return
     */
    boolean isHorizontallyScrollable();

    /**
     * Updates children components' positions
     */
    void updateChildrenPositions();
}
