package com.gasis.rts.logic.object;

import com.gasis.rts.logic.object.combat.OffensiveSpecs;

/**
 * Represents all game objects that can attack: units, buildings
 */
public abstract class OffensiveGameObject extends GameObject {

    // offensive combat specs of the object
    protected OffensiveSpecs offensiveSpecs;

    /**
     * Gets the offensive specs of the object
     * @return
     */
    public OffensiveSpecs getOffensiveSpecs() {
        return offensiveSpecs;
    }

    /**
     * Sets the offensive specs of the object
     *
     * @param offensiveSpecs new offensive specs
     */
    public void setOffensiveSpecs(OffensiveSpecs offensiveSpecs) {
        this.offensiveSpecs = offensiveSpecs;
    }
}
