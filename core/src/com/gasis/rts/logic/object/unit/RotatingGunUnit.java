package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.object.combat.RotatingGun;
import com.gasis.rts.resources.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * A unit that has one or more rotating guns
 */
public class RotatingGunUnit extends Unit {

    // rotating guns of the unit
    protected Map<String, RotatingGun> guns = new HashMap<String, RotatingGun>();

    /**
     * Adds a rotating gun to the unit
     *
     * @param name name used to identify the gun
     * @param gun gun to add
     */
    public void addGun(String name, RotatingGun gun) {
        guns.put(name, gun);
    }

    /**
     * Rotates the unit if required in order for it to face the specified direction
     *
     * @param facingDirection new facing direction
     */
    @Override
    public void rotateToDirection(byte facingDirection) {
        super.rotateToDirection(facingDirection);

        // if there is no target, rotate the guns as well
        for (RotatingGun gun: guns.values()) {
            gun.rotateToDirection(facingDirection);
        }
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
        for (RotatingGun gun: guns.values()) {
            gun.setRotationPointX(getCenterX() + gun.getRelativeX());
            gun.setRotationPointY(getCenterY() + gun.getRelativeY());
            gun.update(delta);
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
        for (RotatingGun gun: guns.values()) {
            gun.render(batch, resources);
        }
    }
}
