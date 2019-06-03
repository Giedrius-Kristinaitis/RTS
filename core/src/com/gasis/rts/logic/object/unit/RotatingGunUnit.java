package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.object.combat.RotatingGun;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * A unit that has one or more rotating guns
 */
public class RotatingGunUnit extends Unit {

    // rotating guns of the unit
    protected Map<String, FixedRotatingGun> guns = new HashMap<String, FixedRotatingGun>();

    /**
     * Adds a rotating gun to the unit
     *
     * @param name name used to identify the gun
     * @param gun gun to add
     * @param coordinates coordinates of the point the gun rotates around
     */
    public void addGun(String name, RotatingGun gun, Point coordinates) {

    }

    /**
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        super.update(delta);

        // update the rotating guns
        for (FixedRotatingGun gun: guns.values()) {
            gun.gun.update(delta);
        }
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        super.render(batch, resources);

        // render the rotating guns
        for (FixedRotatingGun gun: guns.values()) {
            gun.gun.render(batch, resources);
        }
    }

    /**
     * One of the rotating guns of the unit
     */
    protected class FixedRotatingGun {

        protected RotatingGun gun;
        protected Point coordinates;

        protected FixedRotatingGun(RotatingGun gun, Point coordinates) {
            this.gun = gun;
            this.coordinates = coordinates;
        }
    }
}
