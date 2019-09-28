package com.gasis.rts.ui.component;

/**
 * Abstract UI component
 */
public class AbstractComponent implements ComponentInterface {

    /**
     * Sets the texture atlas to use for the component
     *
     * @param textureAtlas name of the atlas to use
     */
    @Override
    public void setTextureAtlas(String textureAtlas) {

    }

    /**
     * Sets the component's background texture
     *
     * @param backgroundTexture background texture
     */
    @Override
    public void setBackgroundTexture(String backgroundTexture) {

    }

    /**
     * Sets the component's hover background texture
     *
     * @param hoverBackgroundTexture hover background texture
     */
    @Override
    public void setHoverBackgroundTexture(String hoverBackgroundTexture) {

    }

    /**
     * Sets the component's active background texture
     *
     * @param activeBackgroundTexture active background texture
     */
    @Override
    public void setActiveBackgroundTexture(String activeBackgroundTexture) {

    }

    /**
     * Gets the component's texture atlas
     *
     * @return
     */
    @Override
    public String getTextureAtlas() {
        return null;
    }

    /**
     * Gets the component's background texture
     *
     * @return
     */
    @Override
    public String getBackgroundTexture() {
        return null;
    }

    /**
     * Gets the component's hover background texture
     *
     * @return
     */
    @Override
    public String getHoverBackgroundTexture() {
        return null;
    }

    /**
     * Gets the component's active background texture
     *
     * @return
     */
    @Override
    public String getActiveBackgroundTexture() {
        return null;
    }

    /**
     * Sets the component's x position
     *
     * @param x x
     */
    @Override
    public void setX(float x) {

    }

    /**
     * Sets the component's y position
     *
     * @param y y
     */
    @Override
    public void setY(float y) {

    }

    /**
     * Sets the component's width
     *
     * @param width width
     */
    @Override
    public void setWidth(float width) {

    }

    /**
     * Sets the component's height
     *
     * @param height width
     */
    @Override
    public void setHeight(float height) {

    }

    /**
     * Gets the component's x
     *
     * @return
     */
    @Override
    public float getX() {
        return 0;
    }

    /**
     * Gets the component's y
     *
     * @return
     */
    @Override
    public float getY() {
        return 0;
    }

    /**
     * Gets the component's width
     *
     * @return
     */
    @Override
    public float getWidth() {
        return 0;
    }

    /**
     * Gets the component's height
     *
     * @return
     */
    @Override
    public float getHeight() {
        return 0;
    }
}
