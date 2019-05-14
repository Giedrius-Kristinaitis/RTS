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
        short width = properties.get("width", Integer.class).shortValue();
        short height = properties.get("height", Integer.class).shortValue();

        BlockMap blockMap = new BlockMap(width, height);

        MapLayers layers = map.getLayers();

        addBlocksToMap(blockMap, (TiledMapTileLayer) layers.get("ground_0"), "ground.atlas");
        addBlocksToMap(blockMap, (TiledMapTileLayer) layers.get("ground_1"), "ground.atlas");
        addBlocksToMap(blockMap, (TiledMapTileLayer) layers.get("hills"), "hills.atlas");
        addBlocksToMap(blockMap, (TiledMapTileLayer) layers.get("passable_objects"), "passable_objects.atlas");
        addBlocksToMap(blockMap, (TiledMapTileLayer) layers.get("trees_and_bushes"), "trees_and_bushes.atlas");

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
                // get the cell at (x, y)
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                // if the cell is null, that means nothing is at (x, y)
                if (cell == null) {
                    continue;
                }

                // cell found at (x, y), process it
                TiledMapTile tile = cell.getTile();

                // if there is no tile at this cell or it's
                // id is 0 (in tmx format id 0 means an empty tile), continue
                if (tile != null || tile.getId() == 0) {
                    VisibleBlock block = (VisibleBlock) map.getBlock((short) x, (short) y);

                    // if there is no block at the new block map at (x, y),
                    // create a new one
                    if (block == null) {
                        block = new VisibleBlock();
                        map.addBlock(block, (short) x, (short) y);
                    }

                    // set the correct texture atlas and texture name for the tile
                    BlockImage image = new BlockImage();
                    image.atlas = atlas;

                    // subtract the id by because tmx starts counting tile ids from 1,
                    // and in the texture atlas it's from 0
                    int tileId = tile.getId() - 1;

                    // if the id is less than 10, add 0 in front to adapt to the texture atlas
                    // in the atlas there are 0's in front of numbers less than 10
                    // to keep the images properly ordered when packing them
                    // for example if there were no 0's in front, the images would
                    // be ordered like this: 1, 10, 11, 12, 2, 3, 4...
                    // and that is not the wanted behavior
                    if (tileId < 10) {
                        image.texture = "0" + tileId;
                    } else {
                        image.texture = String.valueOf(tileId);
                    }

                    // insert the image on top of the block
                    block.addImage(image, false);
                }
            }
        }
    }
}
