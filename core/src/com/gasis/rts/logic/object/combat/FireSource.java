package com.gasis.rts.logic.object.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.animation.complexanimation.ProjectileAnimation;
import com.gasis.rts.resources.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * A point from which shots are fired
 */
public class FireSource implements Updatable, Renderable {

    // coordinates of the source
    protected float x;
    protected float y;

    // animations used by the fire source
    protected List<ProjectileAnimation> animations = new ArrayList<ProjectileAnimation>();

    /**
     * Sets the x coordinate of the fire source
     *
     * @param x new x coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate of the fire source
     *
     * @param y new y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets the x coordinate of the fire source
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the fire source
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {

    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {

    }
}
