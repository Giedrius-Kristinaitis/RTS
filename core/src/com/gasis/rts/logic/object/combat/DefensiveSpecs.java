package com.gasis.rts.logic.object.combat;

/**
 * Defensive specs of a game object
 */
public class DefensiveSpecs {

    protected float defence;
    protected float maxHp;
    protected float sightRange;

    /**
     * Default class constructor
     */
    public DefensiveSpecs() {}

    /**
     * Constructor with arguments
     *
     * @param defence
     * @param maxHp
     */
    public DefensiveSpecs(float defence, float maxHp, float sightRange) {
        this.defence = defence;
        this.maxHp = maxHp;
        this.sightRange = sightRange;
    }

    /**
     * Gets the defence
     * @return
     */
    public float getDefence() {
        return defence;
    }

    /**
     * Sets the defence
     *
     * @param defence new defence
     */
    public void setDefence(float defence) {
        this.defence = defence;
    }

    /**
     * Gets the maximum amount of hp the object can have
     * @return
     */
    public float getMaxHp() {
        return maxHp;
    }

    /**
     * Sets the maximum amount of hp the object can have
     *
     * @param maxHp new maximum hp
     */
    public void setMaxHp(float maxHp) {
        this.maxHp = maxHp;
    }

    /**
     * Gets the sight range
     * @return
     */
    public float getSightRange() {
        return sightRange;
    }

    /**
     * Sets the sight range
     *
     * @param sightRange new sight range
     */
    public void setSightRange(float sightRange) {
        this.sightRange = sightRange;
    }
}
