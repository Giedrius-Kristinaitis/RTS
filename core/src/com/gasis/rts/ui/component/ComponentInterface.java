package com.gasis.rts.ui.component;

import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.ui.event.ClickListener;

/**
 * A UI component
 */
public interface ComponentInterface extends Renderable, Updatable {

    /**
     * Adds a click listener to the component
     *
     * @param listener listener to add
     */
    void addClickListener(ClickListener listener);

    /**
     * Sets the texture atlas to use for the component
     *
     * @param textureAtlas name of the atlas to use
     */
    void setTextureAtlas(String textureAtlas);

    /**
     * Sets the component's background texture
     *
     * @param backgroundTexture background texture
     */
    void setBackgroundTexture(String backgroundTexture);

    /**
     * Sets the component's hover background texture
     *
     * @param hoverBackgroundTexture hover background texture
     */
    void setHoverBackgroundTexture(String hoverBackgroundTexture);

    /**
     * Sets the component's active background texture
     *
     * @param activeBackgroundTexture active background texture
     */
    void setActiveBackgroundTexture(String activeBackgroundTexture);

    /**
     * Gets the component's texture atlas
     *
     * @return
     */
    String getTextureAtlas();

    /**
     * Gets the component's background texture
     *
     * @return
     */
    String getBackgroundTexture();

    /**
     * Gets the component's hover background texture
     *
     * @return
     */
    String getHoverBackgroundTexture();

    /**
     * Gets the component's active background texture
     *
     * @return
     */
    String getActiveBackgroundTexture();

    /**
     * Sets the component's x position
     *
     * @param x x
     */
    void setX(float x);

    /**
     * Sets the component's y position
     *
     * @param y y
     */
    void setY(float y);

    /**
     * Sets the component's width
     *
     * @param width width
     */
    void setWidth(float width);

    /**
     * Sets the component's height
     *
     * @param height width
     */
    void setHeight(float height);

    /**
     * Gets the component's x
     *
     * @return
     */
    float getX();

    /**
     * Gets the component's y
     *
     * @return
     */
    float getY();

    /**
     * Gets the component's width
     *
     * @return
     */
    float getWidth();

    /**
     * Gets the component's height
     *
     * @return
     */
    float getHeight();
}
