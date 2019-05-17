package com.gasis.rts.logic.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

/**
 * Renders a map to the screen
 */
public interface MapRenderer {

    /**
     * Sets the x position of the rendered map portion's bottom-left corner
     *
     * @param x
     */
    void setRenderX(float x);

    /**
     * Sets the y position of the rendered map portion's bottom-left corner
     *
     * @param y
     */
    void setRenderY(float y);

    /**
     * Sets the width of the rendered map portion
     *
     * @param width
     */
    void setRenderWidth(float width);

    /**
     * Sets the height of the rendered map portion
     *
     * @param height
     */
    void setRenderHeight(float height);

    /**
     * Offsets the x rendering position by the given value
     *
     * @param offsetX
     */
    void offsetRenderX(float offsetX);

    /**
     * Offsets the y rendering position by the given value
     *
     * @param offsetY
     */
    void offsetRenderY(float offsetY);

    /**
     * Sets the map to be rendered by this renderer
     *
     * @param map
     */
    void setRenderedMap(Map map);

    /**
     * Renders the map
     *
     * @param batch sprite batch to draw to
     * @param resources game assets
     * @param delta time elapsed since the last render
     */
    void render(SpriteBatch batch, Resources resources, float delta);
}
