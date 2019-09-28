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
    protected boolean verticallyScrollable = true;
    protected boolean horizontallyScrollable = true;

    // container's child components
    protected Set<Component> childComponents = new HashSet<Component>();

    // container's child containers
    protected Set<Container> childContainers = new HashSet<Container>();

    // last recorded mouse position
    protected static float lastMouseX;
    protected static float lastMouseY;

    /**
     * Adds a component to the container
     *
     * @param component component to add
     */
    @Override
    public void add(Component component) {
        if (component instanceof Container) {
            childContainers.add((Container) component);
        } else {
            childComponents.add(component);
        }

        updateChildPosition(component);
    }

    /**
     * Removes a component from the container
     *
     * @param component component to remove
     */
    @Override
    public void remove(Component component) {
        if (!childComponents.remove(component)) {
            childContainers.remove(component);
        }
    }

    /**
     * Called when the mouse is moved
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    @Override
    public boolean mouseMoved(float x, float y) {
        boolean eventHandled = false;

        for (Container container : childContainers) {
            eventHandled = container.mouseMoved(x, y);

            if (eventHandled) {
                break;
            }
        }

        for (Component component : childComponents) {
            if (!eventHandled && x >= component.getX() && y >= component.getY() && x <= component.getX() + component.getWidth() && y <= component.getY() + component.getHeight()) {
                component.setHover(true);
                eventHandled = true;
                break;
            } else {
                component.setHover(false);
            }
        }

        if (!eventHandled && x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height) {
            hover = true;
            eventHandled = true;
        } else {
            hover = false;
        }

        lastMouseX = x;
        lastMouseY = y;

        return eventHandled;
    }

    /**
     * Called when the mouse if pressed down
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    @Override
    public boolean mouseDown(float x, float y) {
        boolean eventHandled = false;

        for (Container container : childContainers) {
            eventHandled = container.mouseDown(x, y);

            if (eventHandled) {
                break;
            }
        }

        for (Component component : childComponents) {
            if (!eventHandled && x >= component.getX() && y >= component.getY() && x <= component.getX() + component.getWidth() && y <= component.getY() + component.getHeight()) {
                component.setClicked(true);
                eventHandled = true;
                break;
            }
        }

        if (!eventHandled && x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height) {
            clicked = true;
            eventHandled = true;
        }

        return eventHandled;
    }

    /**
     * Called when the mouse is released
     *
     * @param x x of the mouse
     * @param y y of the mouse
     */
    @Override
    public boolean mouseUp(float x, float y) {
        updateChildrenClickStatus(false);

        clicked = false;

        for (Container container : childContainers) {
            container.mouseUp(x, y);
        }

        return true;
    }

    /**
     * Sets children clicked flag to the given one
     *
     * @param clicked are children clicked or not
     */
    protected void updateChildrenClickStatus(boolean clicked) {
        for (Component component : childComponents) {
            component.setClicked(clicked);
        }
    }

    /**
     * Called when the mouse is scrolled
     *
     * @param amount scroll amount
     */
    @Override
    public boolean mouseScrolled(int amount) {
        boolean eventHandled = false;

        for (Container container : childContainers) {
            eventHandled = container.mouseScrolled(amount);

            if (eventHandled) {
                break;
            }
        }

        if (!eventHandled && lastMouseX >= x && lastMouseY >= y && lastMouseX <= x + width && lastMouseY <= y + height) {
            eventHandled = true;
        }

        return eventHandled;
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
     * Checks if the container is vertically scrollable
     *
     * @return
     */
    @Override
    public boolean isVerticallyScrollable() {
        return verticallyScrollable;
    }

    /**
     * Checks if the container is horizontally scrollable
     *
     * @return
     */
    @Override
    public boolean isHorizontallyScrollable() {
        return horizontallyScrollable;
    }

    /**
     * Updates children components' positions
     */
    @Override
    public void updateChildrenPositions() {
        for (Component component : childComponents) {
            updateChildPosition(component);
        }

        for (Container container : childContainers) {
            updateChildPosition(container);
        }
    }

    /**
     * Sets the component's position
     *
     * @param x new x
     * @param y new y
     */
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        updateChildrenPositions();
    }

    /**
     * Sets the texture atlas to use for the component
     *
     * @param textureAtlas name of the atlas to use
     */
    @Override
    public void setTextureAtlas(String textureAtlas) {
        super.setTextureAtlas(textureAtlas);
    }

    /**
     * Sets the component's x position
     *
     * @param x x
     */
    @Override
    public void setX(float x) {
        super.setX(x);

        updateChildrenPositions();
    }

    /**
     * Sets the component's y position
     *
     * @param y y
     */
    @Override
    public void setY(float y) {
        super.setY(y);

        updateChildrenPositions();
    }

    /**
     * Updates child's position
     *
     * @param child child to update
     */
    protected void updateChildPosition(Component child) {
        child.setX(x + child.getX());
        child.setY(y + child.getY());
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        super.update(delta);

        for (Component component : childComponents) {
            component.update(delta);
        }

        for (Container container : childContainers) {
            container.update(delta);
        }
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

        for (Component component : childComponents) {
            component.render(batch, resources);
        }

        for (Container container : childContainers) {
            container.render(batch, resources);
        }
    }
}
