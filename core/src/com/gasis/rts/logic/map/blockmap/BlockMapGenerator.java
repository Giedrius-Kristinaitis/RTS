package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.map.MapGenerator;

import java.util.List;
import java.util.Random;

/**
 * Generates a block map from a map script
 */
public class BlockMapGenerator implements MapGenerator {

    /**
     * Map generation command format:
     *
     * [name of the terrain] [x] [y] [width] [height] [shape] [thickness]
     *
     * Explanations:
     *  - name of the terrain: grass, dirt, or water
     *  - x: x of the bottom left corner of the terrain shape
     *  - y: y of the bottom left corner of the terrain shape
     *  - width: width of the terrain shape
     *  - height: height of the terrain shape
     *  - shape: type of the terrain shape: circle or rectangle
     *  - thickness: thickness of the terrain between 0 and 1
     */

    // used to generate random structures and their properties
    protected Random random = new Random();

    /**
     * Generates a map from a script file
     *
     * @param scriptFile file to generate the map from
     *
     * @return generated map, null if there was an error parsing the map script
     */
    @Override
    public BlockMap generate(FileHandle scriptFile) {
        try {
            // create a file line reader for easier reading
            FileLineReader reader = new FileLineReader(scriptFile.read(), ":");

            // read the map's meta-data
            short width = Short.parseShort(reader.readLine("width"));
            short height = Short.parseShort(reader.readLine("height"));

            long seed = Long.parseLong(reader.readLine("seed"));

            // initialize the map and other objects
            BlockMap map = new BlockMap(width, height);

            random.setSeed(seed);

            // create map layers
            map.addMapLayer(new BlockMapLayer("terrain_1", (short) map.getWidth(), (short) map.getHeight()), true);

            // read map commands and generate a map based on them
            // look at the beginning of the file to see the command format
            List<String> commands = reader.readLines("command");

            for (String command: commands) {
                processCommand(command, map);
            }

            // return the final result
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Processes a map script command
     *
     * @param command command to process
     * @param map map to apply the command to
     */
    protected void processCommand(String command, BlockMap map) {
        String[] data = command.split(" ");

        if (data[0].equals("grass") || data[0].equals("dirt") || data[0].equals("water")) {
            addTerrain(data, map);
        }
    }

    /**
     * Parses command position and dimension arguments
     *
     * @param commandData command data as a string array
     * @return array of parsed shorts
     */
    protected short[] parseDimensions(String[] commandData) {
        short[] data = new short[4];

        data[0] = Short.parseShort(commandData[1]);
        data[1] = Short.parseShort(commandData[2]);
        data[2] = Short.parseShort(commandData[3]);
        data[3] = Short.parseShort(commandData[4]);

        return data;
    }

    /**
     * Adds terrain type to the map
     *
     * @param data data of the terrain command
     * @param map map to add the terrain to
     */
    protected void addTerrain(String[] data, BlockMap map) {
        short[] dimensions = parseDimensions(data);

        float thickness = Float.parseFloat(data[6]);

        // put a rectangular terrain piece
        if (data[5].equals("rectangle")) {
            addTerrainRectangle(data[0], dimensions, thickness, (BlockMapLayer) map.getLayerByName("terrain_1"));
        }

        // put a round terrain piece
        else if (data[5].equals("circle")) {
            addTerrainCircle(data[0], dimensions, thickness, (BlockMapLayer) map.getLayerByName("terrain_1"));
        }
    }

    /**
     * Adds a terrain block to the map
     *
     * @param terrainType type of the terrain block
     * @param x x coordinate of the block
     * @param y y coordinate of the block
     * @param thickness thickness of the terrain
     * @param layer map layer to add the terrain block to
     */
    protected void addTerrainBlock(String terrainType, short x, short y, float thickness, BlockMapLayer layer) {
        if (x < 0 || x >= layer.getWidth() || y < 0 || y >= layer.getHeight()) {
            return;
        }

        // get the correct block in the map
        VisibleBlock block = (VisibleBlock) layer.getBlock(x, y);

        if (block == null) {
            block = new VisibleBlock();
            block.setPassable(true);
            layer.addBlock(block, x, y);
        }

        // decide the thickness of the block
        boolean thickTerrain = random.nextFloat() <= thickness;

        BlockImage image = new BlockImage();

        image.atlas = "t1.atlas";

        if (thickTerrain) {
            // insert a thick terrain block here
            // add the block of the correct terrain type
            if (terrainType.equals("grass")) {
                image.texture = "g" + (2 + random.nextInt(2));
            } else if (terrainType.equals("dirt")) {
                image.texture = "d" + (1 + random.nextInt(2));
            } else if (terrainType.equals("water")) {
                image.texture = "w1";
                block.setPassable(false);
            }

            if (image.texture != null) {
                // add the thick terrain block to the bottom
                BlockImage bottomImage = block.getBottomImage();

                if (bottomImage != null) {
                    bottomImage.atlas = image.atlas;
                    bottomImage.texture = image.texture;
                    bottomImage.scale = 1.01f;
                } else {
                    // no bottom image exists on this block, add a new one
                    image.scale = 1.01f;
                    block.addImage(image, true);
                }
            }
        } else {
            boolean mediumThickTerrain = random.nextFloat() <= thickness;

            if (mediumThickTerrain) {
                // insert a medium thick terrain block here
                // add the block of the correct terrain type
                if (terrainType.equals("grass")) {
                    image.texture = "gp" + (1 + random.nextInt(2));
                } else if (terrainType.equals("dirt")) {
                    image.texture = "dp" + (1 + random.nextInt(3));
                } else if (terrainType.equals("water")) {
                    image.texture = "wp1";
                }

                if (image.texture != null) {
                    block.addImage(image, false);
                }
            } // if mediumThickTerrain is false, skip this block
        }
    }

    /**
     * Adds a rectangular piece of terrain to the map
     *
     * @param terrainType type of the terrain
     * @param dimensions position and dimensions of the terrain piece
     * @param thickness thickness of the terrain
     * @param layer map layer to add the terrain to
     */
    protected void addTerrainRectangle(String terrainType, short[] dimensions, float thickness, BlockMapLayer layer) {
        for (short x = dimensions[0]; x < dimensions[0] + dimensions[2]; x++) {
            for (short y = dimensions[1]; y < dimensions[1] + dimensions[3]; y++) {
                addTerrainBlock(terrainType, x, y, thickness, layer);
            }
        }
    }

    /**
     * Adds a round (or elliptic) piece of terrain to the map
     *
     * @param terrainType type of the terrain
     * @param dimensions position and dimensions of the terrain piece
     * @param thickness thickness of the terrain
     * @param layer map layer to add the terrain to
     */
    protected void addTerrainCircle(String terrainType, short[] dimensions, float thickness, BlockMapLayer layer) {
        /*
         *  The formula of an ellipse:
         *
         *  x^2/a^2 + y^2/b^2 = 1
         *
         *  where a is half of the width
         *  and b is half of the height
         *
         *  x = +-sqrt(a^2(1 - y^2/b^2))
         *  y = +-sqrt(b^2(1 - x^2/a^2))
         */

        double halfWidth = (double) dimensions[2] / 2D;
        double halfHeight = (double) dimensions[3] / 2D;

        short centerY = (short) (dimensions[1] + dimensions[3] / 2);
        short centerX = (short) (dimensions[0] + dimensions[2] / 2);

        for (short x = dimensions[0]; x < dimensions[0] + dimensions[2]; x++) {
            short xRelativeToCenter = (short) (x - centerX);

            // calculate the y coordinate for the current x using the formula of an ellipse
            short absY = (short) (Math.sqrt(
                    Math.pow(halfHeight, 2) * (1 - Math.pow(xRelativeToCenter, 2) / Math.pow(halfWidth, 2)))
            );

            // draw bottom half of the line for the current x
            for (short y = (short) (centerY - absY); y <= centerY; y++) {
                addTerrainBlock(terrainType, x, y, thickness, layer);
            }

            // draw top half of the line for the current x
            for (short y = (short) (centerY + absY); y > centerY; y--) {
                addTerrainBlock(terrainType, x, y, thickness, layer);
            }
        }
    }
}
