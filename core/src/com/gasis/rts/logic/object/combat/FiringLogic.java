package com.gasis.rts.logic.object.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Logic for firing shots from fire sources
 */
public class FiringLogic implements Renderable {

    // fire sources used to fire shots
    private Map<String, FireSource> fireSources = new HashMap<String, FireSource>();

    // how many sequential shots are fired when not in siege mode
    public byte shotCount;

    // how many sequential shots are fired when in siege mode
    public byte siegeModeShotCount;

    // interval between sequential shots when not in siege mode
    public float shotInterval;

    // interval between sequential shots when in siege mode
    public float siegeModeShotInterval;

    // how much time has elapsed since the last shot (in seconds)
    private float timeSinceLastShot;

    // how many shots are waiting to be fired
    private byte enqueuedShots;

    // target of the fire
    public Point target;

    // reload speed (in seconds)
    public float reloadSpeed;

    // reload speed when in siege mode (in seconds)
    public float siegeModeReloadSpeed;

    // how much time has elapsed since the last reload (in seconds)
    private float timeSinceLastReload;

    /**
     * Adds a fire source
     *
     * @param name name used to identify the source
     * @param source source to add
     */
    public void addFireSource(String name, FireSource source) {
        fireSources.put(name, source);
    }

    /**
     * Enqueues shots to be fired
     *
     * @param siegeMode is the firing thing in siege mode
     */
    public void enqueueShots(boolean siegeMode) {
        if (enqueuedShots == 0) {
            enqueuedShots = siegeMode ? siegeModeShotCount : shotCount;
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
     * @param siegeMode is the firing thing in siege mode
     * @param facingDirection the direction the firing thing is facing
     * @param delta time elapsed since the last update
     * @param x x coordinate to which the firing points are relative
     * @param y y coordinate to which the firing points are relative
     *
     * @return true if a shot was fired
     */
    public boolean update(boolean siegeMode, byte facingDirection, float delta, float x, float y) {
        for (FireSource source: fireSources.values()) {
            source.update(delta);
        }

        if (target == null) {
            return false;
        }

        boolean fired = false;

        // update firing logic
        if (enqueuedShots > 0) {
            if (siegeMode && timeSinceLastShot >= siegeModeShotInterval) {
                fired = launchShot(true, facingDirection, x, y);
            } else if (!siegeMode && timeSinceLastShot >= shotInterval) {
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
        boolean fired = false;

        for (FireSource source: fireSources.values()) {
            if ((siegeMode && timeSinceLastReload >= siegeModeReloadSpeed) || (!siegeMode && timeSinceLastReload >= reloadSpeed)) {
                source.setX(x + source.getFirePoints().get(facingDirection).x);
                source.setY(y + source.getFirePoints().get(facingDirection).y);
                source.fire(facingDirection, target.x, target.y);
                fired = true;
                enqueuedShots--;
                timeSinceLastShot = 0;

                if (enqueuedShots == 0) {
                    timeSinceLastReload = 0;
                }
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
}
