package com.gasis.rts.logic.player.exploration;

import java.util.HashSet;
import java.util.Set;

/**
 * Multiplexer for exploration data
 */
public class ExplorationDataMultiplexer implements ExplorationDataInterface {

    // exploration data instances
    protected Set<ExplorationDataInterface> data = new HashSet<ExplorationDataInterface>();

    /**
     * Adds exploration data to the multiplexer
     *
     * @param explorationData data to add
     */
    public void addExplorationDataInstance(ExplorationDataInterface explorationData) {
        data.add(explorationData);
    }

    /**
     * Initializes exploration data
     *
     * @param mapWidth
     * @param mapHeight
     */
    @Override
    public void init(short mapWidth, short mapHeight) {
        // nothing to do
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
        for (ExplorationDataInterface data : this.data) {
            data.setExplored(x, y, explored);
        }
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
        for (ExplorationDataInterface data : this.data) {
            if (data.isExplored(x, y)) {
                return true;
            }
        }

        return false;
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
        for (ExplorationDataInterface data : this.data) {
            data.setVisible(x, y, visible);
        }
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
        for (ExplorationDataInterface data : this.data) {
            if (data.isVisible(x, y)) {
                return true;
            }
        }

        return false;
    }
}
