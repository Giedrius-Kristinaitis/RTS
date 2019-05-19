package com.gasis.rts.logic.animation.complexanimation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.resources.Resources;

/**
 * An animation of a flying projectile
 */
public abstract class ProjectileAnimation implements Animation {

    // the flying projectile
    protected FrameAnimation projectile;

    // the animation at the end of the firing thing
    protected FrameAnimation fireAnimation;

    // the animation that gets played when the projectile reaches it's target
    protected FrameAnimation endAnimation;

    protected float x;
    protected float y;

    /**
     * Updates the animation
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {

    }

    /**
     * Renders the animation
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {

    }
}
