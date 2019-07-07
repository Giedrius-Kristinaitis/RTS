package com.gasis.rts.logic.map.blockmap;

import com.gasis.rts.logic.map.Map;
import com.gasis.rts.logic.map.MapLayer;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 2D map made of blocks (or tiles)
 */
public class BlockMap implements Map {

    // map dimensions
    protected short width;
    protected short height;

    // map layers
    protected Deque<BlockMapLayer> layers = new LinkedList<BlockMapLayer>();

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
     * Checks if the map block at (x, y) is occupied or not
     *
     * @param x x of the block
     * @param y y of the block
     * @return
     */
    public boolean isBlockOccupied(short x, short y) {
        for (BlockMapLayer layer: layers) {
            Block block = layer.getBlock(x, y);

            if (block != null && block.getOccupyingObject() != null) {
                return true;
            }
        }

        return false;
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
