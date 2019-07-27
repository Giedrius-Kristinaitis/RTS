package com.gasis.rts.logic.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.resources.Resources;

/**
 * Plays animations
 */
public class AnimationPlayer implements AnimationPlayerInterface, Updatable, Renderable {

    /**
     * Plays an animation
     *
     * @param animation animation to play
     */
    @Override
    public void play(Animation animation) {

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

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {

    }
}
