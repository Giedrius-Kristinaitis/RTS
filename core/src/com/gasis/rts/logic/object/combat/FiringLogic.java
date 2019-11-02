package com.gasis.rts.logic.object.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.render.Renderable;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;

import java.util.*;

/**
 * Logic for firing shots from fire sources
 */
public class FiringLogic implements Renderable, OwnerProvider {

    // fire sources used to fire shots
    private Map<String, FireSource> fireSources = new HashMap<String, FireSource>();

    // how many sequential shots are fired when not in siege mode
    protected byte shotCount;

    // how many sequential shots are fired when in siege mode
    protected byte siegeModeShotCount;

    // interval between sequential shots when not in siege mode
    protected float shotInterval;

    // interval between sequential shots when in siege mode
    protected float siegeModeShotInterval;

    // how much time has elapsed since the last shot (in seconds)
    private float timeSinceLastShot;

    // how many shots are waiting to be fired
    private byte enqueuedShots;

    // target of the fire
    public Point target = new Point();

    // reload speed (in seconds)
    protected float reloadSpeed;

    // reload speed when in siege mode (in seconds)
    protected float siegeModeReloadSpeed;

    // how much time has elapsed since the last reload (in seconds)
    private float timeSinceLastReload;

    // is it the first time enqueueing shots
    private boolean initialEnqueue = true;

    // the index of the fire source (in the fireSourceNames list) that will be fired next
    private byte nextFiringSourceIndex = 0;

    // names of all fire sources (used to pick the correct fire source to fire)
    private List<String> fireSourceNames = new ArrayList<String>();

    // the object that owns the firing logic
    private GameObject owner;

    /**
     * Sets the owner of the firing logic
     *
     * @param owner new owner
     */
    public void setOwner(GameObject owner) {
        this.owner = owner;
    }

    /**
     * Gets the owner of the firing logic
     * @return
     */
    @Override
    public GameObject getOwner() {
        return owner;
    }

    /**
     * Checks if the firing logic can be removed (all projectile animations have finished)
     * @return
     */
    public boolean canBeRemoved() {
        for (FireSource source: fireSources.values()) {
            if (!source.allAnimationsFinished()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Adds a target reach listener to the fire sources
     *
     * @param listener listener to add
     */
    public void addTargetReachedListener(TargetReachListener listener) {
        for (FireSource source: fireSources.values()) {
            source.addTargetReachListener(listener);
        }
    }

    /**
     * Removes a target reach listener
     *
     * @param listener listener to remove
     */
    public void removeTargetReachListener(TargetReachListener listener) {
        for (FireSource source: fireSources.values()) {
            source.removeTargetReachListener(listener);
        }
    }

    /**
     * Sets the damage provider for fire sources
     *
     * @param provider provider to use
     */
    public void setDamageProvider(DamageValueProvider provider) {
        for (FireSource source: fireSources.values()) {
            source.setDamageProvider(provider);
        }
    }

    /**
     * Gets the normal shot count
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

    /**
     * Adds a fire source
     *
     * @param name name used to identify the source
     * @param source source to add
     */
    public void addFireSource(String name, FireSource source) {
        fireSources.put(name, source);
        fireSourceNames.add(name);
        source.setOwnerProvider(this);
    }

    /**
     * Enqueues shots to be fired
     *
     * @param siegeMode is the firing thing in siege mode
     */
    public void enqueueShots(boolean siegeMode) {
        if (enqueuedShots == 0) {
            enqueuedShots = siegeMode ? siegeModeShotCount : shotCount;

            timeSinceLastReload = timeSinceLastShot;

            if (initialEnqueue) {
                timeSinceLastReload = siegeMode ? siegeModeReloadSpeed : reloadSpeed;
                timeSinceLastShot = siegeMode ? siegeModeShotInterval : shotInterval;
                initialEnqueue = false;
            }
        }
    }

    /**
     * Checks if there are enqueued shots
     * @return
     */
    public boolean hasEnqueuedShots() {
        return enqueuedShots > 0;
    }

    /**
     * Removes all enqueued shots
     */
    public void removeEnqueuedShots() {
        enqueuedShots = 0;
    }

    /**
     * Updates the state of the object
     *
     * @param togglingSiegeMode is the firing thing switching between siege mode right now
     * @param siegeMode is the firing thing in siege mode
     * @param facingDirection the direction the firing thing is facing
     * @param delta time elapsed since the last update
     * @param x x coordinate to which the firing points are relative
     * @param y y coordinate to which the firing points are relative
     *
     * @return true if a shot was fired
     */
    public boolean update(boolean togglingSiegeMode, boolean siegeMode, byte facingDirection, float delta, float x, float y) {
        for (FireSource source: fireSources.values()) {
            source.update(delta);
        }

        if (target == null || togglingSiegeMode) {
            return false;
        }

        boolean fired = false;

        // update firing logic
        if (enqueuedShots > 0) {
            if (siegeMode && timeSinceLastShot >= Math.min(siegeModeShotInterval, siegeModeReloadSpeed)) {
                fired = launchShot(true, facingDirection, x, y);
            } else if (!siegeMode && timeSinceLastShot >= Math.min(shotInterval, reloadSpeed)) {
                fired = launchShot(false, facingDirection, x, y);
            }
        }

        if (!fired) {
            timeSinceLastShot += delta;
            timeSinceLastReload += delta;
        }

        return fired;
    }

    /**
     * Launches a shot at the target
     *
     * @param siegeMode is the firing thing in siege mode
     * @param facingDirection the direction the firing thing is facing
     * @param x x coordinate to which the firing points are relative
     * @param y y coordinate to which the firing points are relative
     *
     * @return true if a shot was fired
     */
    private boolean launchShot(boolean siegeMode, byte facingDirection, float x, float y) {
        if (facingDirection == Unit.NONE) {
            removeEnqueuedShots();
            timeSinceLastShot = 0;
            return false;
        }

        boolean fired = false;

        // select the correct fire source to fire and increment fire source index
        FireSource source = fireSources.get(fireSourceNames.get(nextFiringSourceIndex));

        // launch a shot
        if ((!siegeMode && timeSinceLastReload >= reloadSpeed) || (siegeMode && timeSinceLastReload >= siegeModeReloadSpeed)) {
            if (source.isEnabled() && ((siegeMode && source.isPresentInSiegeMode()) || (!siegeMode && source.isPresentOutOfSiegeMode()))) {
                source.setX(x + source.getFirePoints().get(facingDirection).x);
                source.setY(y + source.getFirePoints().get(facingDirection).y);
                source.fire(facingDirection, target.x, target.y);

                fired = true;
                enqueuedShots--;
                timeSinceLastShot = 0;
            }

            if (nextFiringSourceIndex == fireSourceNames.size() - 1) {
                nextFiringSourceIndex = 0;
            } else {
                nextFiringSourceIndex++;
            }
        }

        return fired;
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        for (FireSource source: fireSources.values()) {
            source.render(batch, resources);
        }
    }

    /**
     * Resets the state of the logic
     */
    public void reset() {
        enqueuedShots = 0;
        timeSinceLastShot = 0;
        timeSinceLastReload = 0;
        initialEnqueue = true;
        nextFiringSourceIndex = 0;
    }

    /**
     * Gets all logic's fire sources
     * @return
     */
    public Iterable<FireSource> getFireSources() {
        return fireSources.values();
    }
}
