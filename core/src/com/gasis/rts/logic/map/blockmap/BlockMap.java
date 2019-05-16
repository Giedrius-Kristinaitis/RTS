package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.map.Map;
import com.gasis.rts.logic.map.MapLayer;
import com.gasis.rts.resources.Resources;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * 2D map made of blocks (or tiles)
 */
public class BlockMap implements Map {

    // map dimensions
    protected short width;
    protected short height;

    // map layers
    protected Deque<MapLayer> layers = new LinkedList<MapLayer>();

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
     * Adds a new layer to the map
     *
     * @param layer layer to add
     * @param bottom should the layer be added to the bottom or not
     */
    public void addMapLayer(MapLayer layer, boolean bottom) {
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
     * Renders the map to the screen
     *
     * @param batch sprite batch to draw the map to
     * @param res object used to access assets
     * @param delta time elapsed since the last render
     */
    @Override
    public void render(SpriteBatch batch, Resources res, float delta) {
        for (MapLayer layer: layers) {
            layer.render(batch, res, delta);
        }
    }
}
