package com.gasis.rts.logic.player.exploration;

/**
 * Abstraction for map exploration data
 */
public interface ExplorationDataInterface {

    /**
     * Initializes exploration data
     *
     * @param mapWidth
     * @param mapHeight
     */
    void init(short mapWidth, short mapHeight);

    /**
     * Sets cell's explored flag
     *
     * @param x
     * @param y
     * @param explored
     */
    void setExplored(short x, short y, boolean explored);

    /**
     * Checks if a cell is explored
     * @param x
     * @param y
     * @return
     */
    boolean isExplored(short x, short y);

    /**
     * Sets cell's visibility
     *
     * @param x
     * @param y
     * @param visible
     */
    void setVisible(short x, short y, boolean visible);

    /**
     * Checks if a cell is visible
     *
     * @param x
     * @param y
     * @return
     */
    boolean isVisible(short x, short y);
}
