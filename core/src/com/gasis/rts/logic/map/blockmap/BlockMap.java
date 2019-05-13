package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.map.Map;
import com.gasis.rts.resources.Resources;

/**
 * Map made of blocks (or tiles)
 */
public class BlockMap implements Map {

    // map dimensions
    protected short width;
    protected short height;

    // blocks in the map
    protected Block[][] blocks;

    /**
     * Initializes an empty map
     *
     * @param width width of the map in blocks
     * @param height height of the map in block
     */
    public BlockMap(short width, short height) {
        this.width = width;
        this.height = height;

        blocks = new Block[width][height];
    }

    /**
     * Adds a block to the map
     *
     * @param block block to add
     * @param x x coordinate of the block
     * @param y y coordinate of the block
     */
    public void addBlock(Block block, short x, short y) {
        block.setX(x);
        block.setY(y);

        blocks[x][y] = block;
    }

    /**
     * Gets a block from the map
     *
     * @param x x coordinate of the block
     * @param y y coordinate of the block
     * @return block at (x, y)
     */
    public Block getBlock(short x, short y) {
        return blocks[x][y];
    }

    /**
     * Removes a block from the map
     *
     * @param x x coordinate of the block
     * @param y y coordinate of the block
     */
    public void removeBlock(short x, short y) {
        blocks[x][y] = null;
    }

    /**
     * Renders the map to the screen
     *
     * @param batch sprite batch to draw the map to
     * @param res object used to access assets
     * @param delta time elapsed since the last render
     */
    @Override
    public void render(SpriteBatch batch, Resources res, float delta) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                blocks[x][y].render(batch, res, delta);
            }
        }
    }
}
