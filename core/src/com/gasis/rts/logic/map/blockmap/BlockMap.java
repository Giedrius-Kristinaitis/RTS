package com.gasis.rts.logic.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Map made of blocks (or tiles)
 */
public class BlockMap implements Map {

    // map dimensions
    protected int width;
    protected int height;

    public BlockMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Renders the map to the screen
     *
     * @param batch sprite batch to draw the map to
     * @param delta time elapsed since the last render
     */
    @Override
    public void render(SpriteBatch batch, float delta) {

    }
}
