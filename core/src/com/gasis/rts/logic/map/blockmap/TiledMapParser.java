package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.gasis.rts.logic.map.MapParser;

/**
 * Parses a tiled map into a BlockMap
 */
public class TiledMapParser implements MapParser<TiledMap> {

    /**
     * Parses a given tiled map into a block map
     *
     * @param map tiled map to parse
     * @return parsed block map
     */
    @Override
    public BlockMap parse(TiledMap map) {
        MapProperties properties = map.getProperties();

        // get the dimensions of the map
        short width = properties.get("width", Short.class);
        short height = properties.get("height", Short.class);

        BlockMap blockMap = new BlockMap(width, height);

        MapLayers layers = map.getLayers();

        addBlocksToMap(blockMap, (TiledMapTileLayer) layers.get("ground"), "ground");
        addBlocksToMap(blockMap, (TiledMapTileLayer) layers.get("hills"), "hills");
        addBlocksToMap(blockMap, (TiledMapTileLayer) layers.get("passable_objects"), "passable_objects");
        addBlocksToMap(blockMap, (TiledMapTileLayer) layers.get("trees_and_bushes"), "trees_and_bushes");

        return blockMap;
    }

    /**
     * Adds blocks from a map layer to the given map
     *
     * @param map map to add to
     * @param layer layer to take blocks from
     */
    private void addBlocksToMap(BlockMap map, TiledMapTileLayer layer, String atlas) {
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTile tile = layer.getCell(x, y).getTile();

                if (tile != null) {
                    VisibleBlock block = (VisibleBlock) map.getBlock((short) x, (short) y);

                    if (block == null) {
                        block = new VisibleBlock();
                        map.addBlock(block, (short) x, (short) y);
                    }

                    BlockImage image = new BlockImage();
                    image.atlas = atlas;
                    image.texture = String.valueOf(tile.getId());

                    block.addImage(image, false);
                }
            }
        }
    }
}
