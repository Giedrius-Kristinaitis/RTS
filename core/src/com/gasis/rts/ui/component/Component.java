package com.gasis.rts.ui.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.ui.event.ClickListener;
import com.gasis.rts.utils.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract UI component
 */
public class Component implements ComponentInterface, Updatable, Renderable {

    // looks of the component
    protected String textureAtlas;
    protected String backgroundTexture;
    protected String hoverBackgroundTexture;
    protected String activeBackgroundTexture;

    // positioning of the component
    protected float x;
    protected float y;
    protected float width;
    protected float height;

    // is the component currently hovered over
    protected boolean hover;

    // click listeners
    protected Set<ClickListener> clickListeners = new HashSet<ClickListener>();

    // is the component currently clicked on
    protected boolean clicked;

    /**
     * Sets the texture atlas to use for the component
     *
     * @param textureAtlas name of the atlas to use
     */
    @Override
    public void setTextureAtlas(String textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    /**
     * Sets the component's background texture
     *
     * @param backgroundTexture background texture
     */
    @Override
    public void setBackgroundTexture(String backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    /**
     * Sets the component's hover background texture
     *
     * @param hoverBackgroundTexture hover background texture
     */
    @Override
    public void setHoverBackgroundTexture(String hoverBackgroundTexture) {
        this.hoverBackgroundTexture = hoverBackgroundTexture;
    }

    /**
     * Gets the component's texture atlas
     *
     * @return
     */
    @Override
    public String getTextureAtlas() {
        return textureAtlas;
    }

    /**
     * Gets the component's background texture
     *
     * @return
     */
    @Override
    public String getBackgroundTexture() {
        return backgroundTexture;
    }

    /**
     * Gets the component's hover background texture
     *
     * @return
     */
    @Override
    public String getHoverBackgroundTexture() {
        return hoverBackgroundTexture;
    }

    /**
     * Sets the component's x position
     *
     * @param x x
     */
    @Override
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the component's y position
     *
     * @param y y
     */
    @Override
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the component's width
     *
     * @param width width
     */
    @Override
    public void setWidth(float width) {
        this.width = width;

        sizeChanged();
    }

    /**
     * Sets the component's height
     *
     * @param height width
     */
    @Override
    public void setHeight(float height) {
        this.height = height;

        sizeChanged();
    }

    /**
     * Sets the size of the component
     *
     * @param width  new width
     * @param height new height
     */
    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;

        sizeChanged();
    }

    /**
     * Sets the component's position
     *
     * @param x new x
     * @param y new y
     */
    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the component's x
     *
     * @return
     */
    @Override
    public float getX() {
        return x;
    }

    /**
     * Gets the component's y
     *
     * @return
     */
    @Override
    public float getY() {
        return y;
    }

    /**
     * Gets the component's width
     *
     * @return
     */
    @Override
    public float getWidth() {
        return width;
    }

    /**
     * Gets the component's height
     *
     * @return
     */
    @Override
    public float getHeight() {
        return height;
    }

    /**
     * Sets the component's hover flag
     *
     * @param hover is the component hovered over
     */
    @Override
    public void setHover(boolean hover) {
        this.hover = hover;
    }

    /**
     * Checks if the component is hovered over
     *
     * @return
     */
    @Override
    public boolean isHover() {
        return hover;
    }

    /**
     * Adds a click listener to the component
     *
     * @param listener listener to add
     */
    @Override
    public void addClickListener(ClickListener listener) {
        clickListeners.add(listener);
    }

    /**
     * Sets the component's active background texture
     *
     * @param activeBackgroundTexture active background texture
     */
    @Override
    public void setActiveBackgroundTexture(String activeBackgroundTexture) {
        this.activeBackgroundTexture = activeBackgroundTexture;
    }

    /**
     * Gets the component's active background texture
     *
     * @return
     */
    @Override
    public String getActiveBackgroundTexture() {
        return activeBackgroundTexture;
    }

    /**
     * Sets the component's clicked flag
     *
     * @param clicked is the component clicked
     */
    @Override
    public void setClicked(boolean clicked) {
        this.clicked = clicked;

        if (clicked) {
            notifyClickListeners();
        }
    }

    /**
     * Notifies all listeners that the component has been clicked
     */
    protected void notifyClickListeners() {
        for (ClickListener listener : clickListeners) {
            listener.clicked(this);
        }
    }

    /**
     * Checks if the component is clicked
     *
     * @return
     */
    @Override
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Called when the component's size changes
     */
    @Override
    public void sizeChanged() {

    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {

    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        if (clicked && activeBackgroundTexture != null) {
            batch.draw(
                    resources.atlas(Constants.FOLDER_ATLASES + textureAtlas).findRegion(activeBackgroundTexture),
                    x,
                    y,
                    width,
                    height
            );

            return;
        }

        if (hover && hoverBackgroundTexture != null) {
            batch.draw(
                    resources.atlas(Constants.FOLDER_ATLASES + textureAtlas).findRegion(hoverBackgroundTexture),
                    x,
                    y,
                    width,
                    height
            );

            return;
        }

        if (backgroundTexture != null) {
            batch.draw(
                    resources.atlas(Constants.FOLDER_ATLASES + textureAtlas).findRegion(backgroundTexture),
                    x,
                    y,
                    width,
                    height
            );
        }
    }
}
