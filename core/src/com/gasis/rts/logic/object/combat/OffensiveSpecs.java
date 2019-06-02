package com.gasis.rts.logic.object.combat;

/**
 * Offensive specs of any object that can attack
 */
public class OffensiveSpecs {

    protected float attack;
    protected float speed;
    protected float attackRange;

    /**
     * Default class constructor
     */
    public OffensiveSpecs() { }

    /**
     * Constructor with arguments
     *
     * @param attack
     * @param speed
     * @param attackRange
     */
    public OffensiveSpecs(float attack, float speed, float attackRange) {
        this.attack = attack;
        this.speed = speed;
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
}
