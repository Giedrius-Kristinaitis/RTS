package com.gasis.rts.logic.object.combat;

/**
 * Offensive specs of any object that can attack
 */
public class OffensiveSpecs {

    protected float attack;
    protected float speed;
    protected float attackRange;
    protected float siegeModeAttack;
    protected float siegeModeAttackRange;

    /**
     * Default class constructor
     */
    public OffensiveSpecs() {
    }

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
     * Another constructor with arguments
     *
     * @param attack
     * @param speed
     * @param attackRange
     */
    public OffensiveSpecs(float attack, float speed, float attackRange, float siegeModeAttack, float siegeModeAttackRange) {
        this(attack, speed, attackRange);
        this.siegeModeAttack = siegeModeAttack;
        this.siegeModeAttackRange = siegeModeAttackRange;
    }

    /**
     * Gets the attack when in siege mode
     *
     * @return
     */
    public float getSiegeModeAttack() {
        return siegeModeAttack;
    }

    /**
     * Sets the attack when in siege mode
     *
     * @param siegeModeAttack attack in siege mode
     */
    public void setSiegeModeAttack(float siegeModeAttack) {
        this.siegeModeAttack = siegeModeAttack;
    }

    /**
     * Gets the attack range when in siege mode
     *
     * @return
     */
    public float getSiegeModeAttackRange() {
        return siegeModeAttackRange;
    }

    /**
     * Sets the attack range when in siege mode
     *
     * @param siegeModeAttackRange siege mode attack range
     */
    public void setSiegeModeAttackRange(float siegeModeAttackRange) {
        this.siegeModeAttackRange = siegeModeAttackRange;
    }

    /**
     * Gets the attack
     *
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
     *
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
     *
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
