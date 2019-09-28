package com.gasis.rts.ui.component.container;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.ui.component.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains other components
 */
public class Container extends Component implements ContainerInterface {

    // scrollable flags
    protected boolean verticallyScrollable;
    protected boolean horizontallyScrollable;

    // container's children
    protected Set<Component> children = new HashSet<Component>();

    /**
     * Adds a component to the container
     *
     * @param component component to add
     */
    @Override
    public void add(Component component) {
        children.add(component);
    }

    /**
     * Removes a component from the container
     *
     * @param component component to remove
     */
    @Override
    public void remove(Component component) {
        children.remove(component);
    }

    /**
     * Called when the mouse is moved
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    @Override
    public void mouseMoved(float x, float y) {

    }

    /**
     * Called when the mouse if pressed down
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    @Override
    public void mouseDown(float x, float y) {

    }

    /**
     * Called when the mouse is released
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    @Override
    public void mouseUp(float x, float y) {

    }

    /**
     * Called when the mouse is scrolled
     *
     * @param amount scroll amount
     */
    @Override
    public void mouseScrolled(int amount) {

    }

    /**
     * Sets the container's vertical scrolling flag
     *
     * @param scrollable is the container vertically scrollable
     */
    @Override
    public void setVerticallyScrollable(boolean scrollable) {
        verticallyScrollable = scrollable;
    }

    /**
     * Sets the container's horizontal scrolling flag
     *
     * @param scrollable is the container horizontally scrollable
     */
    @Override
    public void setHorizontallyScrollable(boolean scrollable) {
        horizontallyScrollable = scrollable;
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        super.update(delta);


    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        super.render(batch, resources);
    }
}
