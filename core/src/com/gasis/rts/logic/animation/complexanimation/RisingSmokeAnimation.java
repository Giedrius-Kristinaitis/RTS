package com.gasis.rts.logic.animation.complexanimation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationLoader;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

/**
 * A rising smoke animation used for building (power plant, machine factory...) animations
 */
public class RisingSmokeAnimation implements Animation {

    // used to load frame animations
    protected static final FrameAnimationLoader loader = new FrameAnimationLoader();

    // position of the animation (bottom-left corner)
    protected float x;
    protected float y;

    // all smoke ball animations that make up this animation
    protected final FrameAnimation[] smokeBalls = new FrameAnimation[5];

    // how much time has elapsed since the start of the animation
    protected float timeElapsed;

    /**
     * Class constructor
     */
    public RisingSmokeAnimation(float x, float y) {
        loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "smoke_ball"));

        for (int i = 0; i < smokeBalls.length; i++) {
            smokeBalls[i] = loader.newInstance(x, y, x, y + smokeBalls.length * 0.35f, true);
            smokeBalls[i].setLooping(true);
        }
    }

    /**
     * Updates the animation
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        smokeBalls[0].update(delta);

        for (int i = 1; i < smokeBalls.length; i++) {
            if (timeElapsed >= smokeBalls[i].getUpdateInterval() * i) {
                smokeBalls[i].update(delta);
            }
        }

        timeElapsed += delta;
    }

    /**
     * Renders the animation
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        smokeBalls[0].render(batch, resources);

        for (int i = 1; i < smokeBalls.length; i++) {
            if (timeElapsed >= smokeBalls[i].getUpdateInterval() * i) {
                smokeBalls[i].render(batch, resources);
            }
        }
    }
}
