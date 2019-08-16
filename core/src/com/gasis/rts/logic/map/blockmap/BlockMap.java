package com.gasis.rts.logic.map.blockmap;

import com.gasis.rts.logic.map.Map;
import com.gasis.rts.logic.map.MapLayer;
import com.gasis.rts.logic.object.GameObject;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 2D map made of blocks (or tiles)
 */
@SuppressWarnings("Duplicates") // the damned IDE gives a hard time for 2 repeated lines
public class BlockMap implements Map {

    // map dimensions in blocks
    protected short width;
    protected short height;

    // map layers
    // the first (bottom) layer is always the ground layer on which occupying objects are set
    protected Deque<BlockMapLayer> layers = new LinkedList<BlockMapLayer>();

    // how many pieces of junk can exist on a single block at any time
    protected final int MAX_PIECES_OF_JUNK = 4;

    /**
     * Initializes an empty map
     *
     * @param width width of the map in blocks
     * @param height height of the map in block
     */
    public BlockMap(short width, short height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Adds a piece junk to the specified block
     *
     * @param atlas junk atlas name
     * @param junkTexture name of the junk texture
     * @param blockX block x
     * @param blockY block y
     * @param offsetX junk texture's offset in x axis
     * @param offsetY junk texture's offset in y axis
     * @param scale junk texture's scale
     */
    public void addJunk(String atlas, String junkTexture, short blockX, short blockY, float offsetX, float offsetY, float scale) {
        if (blockX < 0 || blockY < 0 || blockX >= width || blockY >= height) {
            return;
        }

        BlockMapLayer craterLayer = (BlockMapLayer) getLayerByName("junk");
        Block block = craterLayer.getBlock(blockX, blockY);
        VisibleBlock visibleBlock;

        if (!(block instanceof VisibleBlock)) {
            visibleBlock = new VisibleBlock();
            craterLayer.addBlock(visibleBlock, blockX, blockY);
        } else {
            visibleBlock = (VisibleBlock) block;
        }

        BlockImage image = new BlockImage();
        image.atlas = atlas;
        image.texture = junkTexture;
        image.offsetY = offsetY;
        image.offsetX = offsetX;
        image.scale = scale;
        image.width = Block.BLOCK_WIDTH;
        image.height = Block.BLOCK_HEIGHT;
        image.offsetX -= Block.BLOCK_WIDTH / 2f;
        image.offsetY -= Block.BLOCK_HEIGHT / 2f;

        if (visibleBlock.imageCount() >= MAX_PIECES_OF_JUNK) {
            visibleBlock.removeBottomImage();
        }

        visibleBlock.addImage(image, false);
    }

    /**
     * Occupies a map block
     *
     * @param x x of the block
     * @param y y of the block
     * @param occupier new occupying object
     */
    public void occupyBlock(short x, short y, GameObject occupier) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }

        layers.getFirst().getBlock(x, y).setOccupyingObject(occupier);
    }

    /**
     * Occupies a map block
     *
     * @param x x of the block
     * @param y y of the block
     * @param occupier new occupying passable object
     */
    public void occupyBlockPassable(short x, short y, GameObject occupier) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }

        layers.getFirst().getBlock(x, y).setPassableObject(occupier);
    }

    /**
     * Checks if the map block at (x, y) is occupied or not
     *
     * @param x x of the block
     * @param y y of the block
     * @return
     */
    public boolean isBlockOccupied(short x, short y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }

        Block block = layers.getFirst().getBlock(x, y);

        if (block != null && block.getOccupyingObject() != null) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the block at (x, y) is passable or not (the block is passable
     * event if something is occupying it)
     *
     * @param x x of the block
     * @param y y of the block
     * @return
     */
    public boolean isBlockPassable(short x, short y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }

        Block block = layers.getFirst().getBlock(x, y);

        if (block != null && block.isPassable()) {
            return true;
        }

        return false;
    }

    /**
     * Gets the object that occupies the specified block
     *
     * @param x block x
     * @param y block y
     * @return occupying object, null if there is no such object
     */
    public GameObject getOccupyingObject(short x, short y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }

        Block block = layers.getFirst().getBlock(x, y);

        if (block != null) {
            return block.getOccupyingObject();
        }

        return null;
    }

    /**
     * Adds a new layer to the map
     *
     * @param layer layer to add
     * @param bottom should the layer be added to the bottom or not
     */
    public void addMapLayer(BlockMapLayer layer, boolean bottom) {
        if (bottom) {
            layers.addFirst(layer);
        } else {
            layers.addLast(layer);
        }
    }

    /**
     * Removes a layer from the map
     *
     * @param name name of the layer to remove
     */
    public void removeMapLayer(String name) {
        MapLayer existing = getLayerByName(name);

        if (existing == null) {
            return;
        }

        layers.remove(existing);
    }

    /**
     * Returns a map layer with the specified name
     *
     * @param name name of the layer
     * @return map layer, null if not found
     */
    public MapLayer getLayerByName(String name) {
        for (MapLayer layer: layers) {
            BlockMapLayer blockLayer = (BlockMapLayer) layer;

            if (blockLayer.getName().equals(name)) {
                return blockLayer;
            }
        }

        return null;
    }

    /**
     * Gets all map layers
     *
     * @return
     */
    public Iterable<BlockMapLayer> getLayers() {
        return layers;
    }

    /**
     * Gets the width of the map in blocks
     * @return
     */
    @Override
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height of the map in blocks
     * @return
     */
    @Override
    public float getHeight() {
        return height;
    }
}
