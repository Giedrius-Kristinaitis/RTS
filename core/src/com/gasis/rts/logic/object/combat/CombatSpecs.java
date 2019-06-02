package com.gasis.rts.logic.object.combat;

/**
 * Unit's or building's combat specs
 */
public class CombatSpecs {

    // object's characteristics
    protected float attack;
    protected float defence;
    protected float speed;
    protected float sightRange;
    protected float attackRange;
    protected float maxHp;

    /**
     * Default class constructor
     */
    public CombatSpecs() { }

    /**
     * Constructor with arguments
     *
     * @param attack
     * @param defence
     * @param speed
     * @param sightRange
     * @param attackRange
     * @param maxHp
     */
    public CombatSpecs(float attack, float defence, float speed, float sightRange, float attackRange, float maxHp) {
        this.attack = attack;
        this.defence = defence;
        this.speed = speed;
        this.sightRange = sightRange;
        this.attackRange = attackRange;
    }

    /**
     * Gets the attack
     * @return
     */
    public float getAttack() {
        return attack;
    }

    /**
     * Sets the attack
     *
     * @param attack new attack
     */
    public void setAttack(float attack) {
        this.attack = attack;
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
     * Gets the speed
     * @return
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets the speed
     *
     * @param speed new speed
     */
    public void setSpeed(float speed) {
        if (speed <= 0) {
            this.speed = 0.000001f; // speed can't be 0, because division by speed exists
            return;
        }

        this.speed = speed;
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

    /**
     * Gets the attack range
     * @return
     */
    public float getAttackRange() {
        return attackRange;
    }

    /**
     * Sets the attack range
     *
     * @param attackRange new attack range
     */
    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
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
}
