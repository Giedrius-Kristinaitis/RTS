package com.gasis.rts.logic.map.blockmap;


import com.gasis.rts.logic.map.MapLayer;

/**
 * A layer of blocks
 */
public class BlockMapLayer implements MapLayer {

    // blocks of the layer
    private Block[][] blocks;

    // dimensions of the layer
    protected short width;
    protected short height;

    // name of the layer
    protected String name;

    /**
     * Default class constructor. Initializes the block array
     *
     * @param width width of the layer in blocks
     * @param height height of the layer in blocks
     */
    public BlockMapLayer(String name, short width, short height) {
        this.name = name;
        this.width = width;
        this.height = height;

        blocks = new Block[width][height];
    }

    /**
     * Adds a block to the layer
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
     * Gets a block from the layer
     *
     * @param x x coordinate of the block
     * @param y y coordinate of the block
     * @return block at (x, y)
     */
    public Block getBlock(short x, short y) {
        return blocks[x][y];
    }

    /**
     * Removes a block from the layer
     *
     * @param x x coordinate of the block
     * @param y y coordinate of the block
     */
    public void removeBlock(short x, short y) {
        blocks[x][y] = null;
    }

    /**
     * Sets the name of the layer
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the layer
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the width of the layer in blocks
     * @return
     */
    public short getWidth() {
        return width;
    }

    /**
     * Gets the height of the layer in blocks
     * @return
     */
    public short getHeight() {
        return height;
    }

    /**
     * Sets the width of the layer
     * @param width new width in blocks
     */
    public void setWidth(short width) {
        this.width = width;
    }

    /**
     * Sets the height of the layer
     * @param height new height in blocks
     */
    public void setHeight(short height) {
        this.height = height;
    }
}
