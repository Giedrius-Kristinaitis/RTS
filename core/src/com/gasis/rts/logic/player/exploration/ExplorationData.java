package com.gasis.rts.logic.player.exploration;

/**
 * Contains a player's map exploration data
 */
@SuppressWarnings("Duplicates")
public class ExplorationData implements ExplorationDataInterface {

    // map exploration data
    protected Cell[][] explorationData;

    // map dimensions
    protected short mapWidth;
    protected short mapHeight;

    /**
     * Initializes exploration data
     *
     * @param mapWidth
     * @param mapHeight
     */
    @Override
    public void init(short mapWidth, short mapHeight) {
        explorationData = new Cell[mapWidth][mapHeight];

        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
    }

    /**
     * Sets cell's explored flag
     *
     * @param x
     * @param y
     * @param explored
     */
    @Override
    public void setExplored(short x, short y, boolean explored) {
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
            return;
        }

        if (explorationData[x][y] == null) {
            explorationData[x][y] = new Cell();
        }

        explorationData[x][y].explored = explored;
    }

    /**
     * Checks if a cell is explored
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean isExplored(short x, short y) {
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
            return false;
        }

        return explorationData[x][y] != null && explorationData[x][y].explored;
    }

    /**
     * Sets cell's visibility
     *
     * @param x
     * @param y
     * @param visible
     */
    @Override
    public void setVisible(short x, short y, boolean visible) {
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
            return;
        }

        if (explorationData[x][y] == null) {
            explorationData[x][y] = new Cell();
        }

        explorationData[x][y].visible = visible;
    }

    /**
     * Checks if a cell is visible
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean isVisible(short x, short y) {
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
            return false;
        }

        return explorationData[x][y] != null && explorationData[x][y].visible;
    }

    /**
     * A cell on the map
     */
    protected class Cell {

        protected boolean explored;
        protected boolean visible;
    }
}
