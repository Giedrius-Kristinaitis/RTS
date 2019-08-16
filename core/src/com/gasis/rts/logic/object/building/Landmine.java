package com.gasis.rts.logic.object.building;

import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.combat.LandmineListener;
import com.gasis.rts.math.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * A landmine
 */
public class Landmine extends OffensiveBuilding {

    // detonation listeners
    protected Set<LandmineListener> listeners = new HashSet<LandmineListener>();

    // the scale of the landmine
    protected byte scale;

    /**
     * Default class constructor
     */
    public Landmine(BlockMap map) {
        super(map);
    }

    /**
     * Adds a detonation listener
     *
     * @param listener listener to add
     */
    public void addListener(LandmineListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a detonation listener
     *
     * @param listener listener to remove
     */
    public void removeListener(LandmineListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sets the landmine's scale
     *
     * @param scale new scale
     */
    public void setScale(byte scale) {
        this.scale = scale;
    }

    /**
     * Gets the landmine's scale
     * @return
     */
    public byte getScale() {
        return scale;
    }

    /**
     * Detonates the landmine
     */
    public void detonate() {
        for (LandmineListener listener: listeners) {
            listener.landmineDetonated(this);
        }
    }
}
