package com.gasis.rts.logic.object.combat;

/**
 * Holds data related to firing: reload speeds, shot counts, shot intervals
 */
public class FiringData {

    // how many sequential shots are fired when not in siege mode
    protected byte shotCount;

    // how many sequential shots are fired when in siege mode
    protected byte siegeModeShotCount;

    // reload speed in seconds when not in siege mode
    protected float reloadSpeed;

    // reload speed in seconds when in siege mode
    protected float siegeModeReloadSpeed;

    // interval in which enqueued shots are fired when not in siege mode (applies only
    // if shotCount > 1)
    protected float shotInterval;

    // interval in which enqueued shots are fired when in siege mode (applies only if
    // siegeModeShotCount > 1)
    protected float siegeModeShotInterval;

    /**
     * Gets the normal shot count
     *
     * @return
     */
    public byte getShotCount() {
        return shotCount;
    }

    /**
     * Sets the normal shot count
     *
     * @param shotCount new shot count
     */
    public void setShotCount(byte shotCount) {
        this.shotCount = shotCount;
    }

    /**
     * Gets siege mode shot count
     *
     * @return
     */
    public byte getSiegeModeShotCount() {
        return siegeModeShotCount;
    }

    /**
     * Sets siege mode shot count
     *
     * @param siegeModeShotCount new shot count
     */
    public void setSiegeModeShotCount(byte siegeModeShotCount) {
        this.siegeModeShotCount = siegeModeShotCount;
    }

    /**
     * Gets normal reload speed
     *
     * @return
     */
    public float getReloadSpeed() {
        return reloadSpeed;
    }

    /**
     * Sets normal reload speed
     *
     * @param reloadSpeed new reload speed
     */
    public void setReloadSpeed(float reloadSpeed) {
        this.reloadSpeed = reloadSpeed;
    }

    /**
     * Gets siege mode reload speed
     *
     * @return
     */
    public float getSiegeModeReloadSpeed() {
        return siegeModeReloadSpeed;
    }

    /**
     * Sets siege mode reload speed
     *
     * @param siegeModeReloadSpeed new reload speed
     */
    public void setSiegeModeReloadSpeed(float siegeModeReloadSpeed) {
        this.siegeModeReloadSpeed = siegeModeReloadSpeed;
    }

    /**
     * Gets normal shot interval
     *
     * @return
     */
    public float getShotInterval() {
        return shotInterval;
    }

    /**
     * Sets normal shot interval
     *
     * @param shotInterval new shot interval
     */
    public void setShotInterval(float shotInterval) {
        this.shotInterval = shotInterval;
    }

    /**
     * Gets siege mode shot interval
     *
     * @return
     */
    public float getSiegeModeShotInterval() {
        return siegeModeShotInterval;
    }

    /**
     * Sets siege mode shot interval
     *
     * @param siegeModeShotInterval new shot interval
     */
    public void setSiegeModeShotInterval(float siegeModeShotInterval) {
        this.siegeModeShotInterval = siegeModeShotInterval;
    }
}
