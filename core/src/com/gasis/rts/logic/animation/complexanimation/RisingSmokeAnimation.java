package com.gasis.rts.logic.animation.complexanimation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.resources.Resources;

/**
 * A rising smoke animation used for building (power plant, machine factory...) animations
 */
public class RisingSmokeAnimation implements Animation {

    // position of the animation (bottom-left corner)
    protected float x;
    protected float y;

    // all smoke ball animations that make up this animation
    protected final FrameAnimation[] smokeBalls = new FrameAnimation[5];

    /**
     * Class constructor
     */
    public RisingSmokeAnimation(float x, float y) {
        for (int i = 0; i < smokeBalls.length; i++) {
            smokeBalls[i] = FrameAnimationFactory.getInstance().create(FrameAnimationFactory.ID_WHITE_SMOKE_BALL, x, y, x, y + smokeBalls.length * 0.35f, true);
            smokeBalls[i].setLooping(true);
            smokeBalls[i].setDelay(i * smokeBalls[i].getUpdateInterval() * 0.9f);
        }
    }

    /**
     * Updates the animation
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        for (FrameAnimation animation: smokeBalls) {
            animation.update(delta);
        }
    }

    /**
     * Renders the animation
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        for (FrameAnimation animation: smokeBalls) {
            animation.render(batch, resources);
        }
    }
}
