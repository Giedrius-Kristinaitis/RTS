package com.gasis.rts.logic.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.render.RenderQueueInterface;
import com.gasis.rts.logic.render.Renderable;
import com.gasis.rts.resources.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Plays frame animations
 */
public class FrameAnimationPlayer implements AnimationPlayerInterface, AnimationFinishListener, Updatable, Renderable {

    // all animations played by the player
    private List<FrameAnimation> animations = new ArrayList<FrameAnimation>();

    // all animations that have finished and need to be removed
    private List<FrameAnimation> animationsToRemove = new ArrayList<FrameAnimation>();

    /**
     * Notifies the observer that the animation has finished
     *
     * @param animation the animation that just finished
     */
    @Override
    public void finished(Animation animation) {
        animationsToRemove.add((FrameAnimation) animation);
    }

    /**
     * Plays an animation
     *
     * @param animation animation to play
     */
    @Override
    public void play(Animation animation) {
        if (!(animation instanceof FrameAnimation)) {
            throw new IllegalArgumentException("The animation must be a frame animation");
        }

        ((FrameAnimation) animation).addFinishListener(this);

        animations.add((FrameAnimation) animation);
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources, RenderQueueInterface renderQueue) {
        for (FrameAnimation animation : animations) {
            animation.render(batch, resources, renderQueue);
        }
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        for (FrameAnimation animation : animations) {
            animation.update(delta);
        }

        if (animationsToRemove.size() > 0) {
            for (FrameAnimation animation : animationsToRemove) {
                animations.remove(animation);
            }
        }
    }
}
