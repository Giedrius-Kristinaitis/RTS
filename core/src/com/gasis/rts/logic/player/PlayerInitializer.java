package com.gasis.rts.logic.player;

import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.math.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Initializes players with initial state and objects (buildings, units)
 */
public class PlayerInitializer implements PlayerInitializerInterface {

    /**
     * Initializes players
     *
     * @param players players to initialize
     * @param map     game map
     */
    @Override
    public void initializePlayers(Iterable<Player> players, BlockMap map) {
        List<Point> baseLocations = map.getBaseLocations();

        for (Player player : players) {
            initializePlayer(player, baseLocations);
        }
    }

    /**
     * Initializes a single player
     *
     * @param player        player to initialize
     * @param baseLocations all possible base locations
     */
    protected void initializePlayer(Player player, List<Point> baseLocations) {
        int locationIndex = (int) (Math.random() * (double) baseLocations.size());

        Point baseLocation = baseLocations.get(locationIndex);

        baseLocations.remove(locationIndex);

        placeInitialBuilding(player, baseLocation);
    }

    /**
     * Places the given player's initial building at the specified location
     *
     * @param player       player to place the building for
     * @param baseLocation base location in map coordinates
     */
    protected void placeInitialBuilding(Player player, Point baseLocation) {
        BuildingLoader loader = player.getFaction().getBuildingLoaders()
                .get(player.getFaction().getInitialBuilding());

        Building initialBuilding = loader.newInstance();

        float blockX = baseLocation.x - initialBuilding.getWidthInBlocks() / 2f;
        float blockY = baseLocation.y - initialBuilding.getHeightInBlocks() / 2f;

        initialBuilding.setX(baseLocation.x * Block.BLOCK_WIDTH - initialBuilding.getWidthInBlocks() / 2f * Block.BLOCK_WIDTH);
        initialBuilding.setY(baseLocation.y * Block.BLOCK_HEIGHT - initialBuilding.getHeightInBlocks() / 2f * Block.BLOCK_HEIGHT);

        if (initialBuilding.getWidthInBlocks() % 2 != 0) {
            initialBuilding.setX(initialBuilding.getX() - Block.BLOCK_WIDTH / 2f);
        }

        if (initialBuilding.getHeightInBlocks() % 2 != 0) {
            initialBuilding.setY(initialBuilding.getY() - Block.BLOCK_HEIGHT / 2f);
        }

        initialBuilding.setXInBlocks((short) blockX);
        initialBuilding.setYInBlocks((short) blockY);
        initialBuilding.initializeAnimations();

        // occupy blocks on the map
        List<Point> blocks = new ArrayList<Point>();

        for (int x = (int) blockX; x < (int) blockX + initialBuilding.getWidthInBlocks(); x++) {
            for (int y = (int) blockY; y < (int) blockY + initialBuilding.getHeightInBlocks(); y++) {
                blocks.add(new Point(x, y));
            }
        }

        initialBuilding.occupyBlocks(blocks);

        initialBuilding.setOwner(player);
        initialBuilding.setBeingConstructed(true);

        player.addBuilding(initialBuilding);

        initialBuilding.update(loader.getConstructionTime());
    }
}
