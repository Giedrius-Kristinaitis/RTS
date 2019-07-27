package com.gasis.rts.logic.animation;

/**
 * Plays animations
 */
public interface AnimationPlayerInterface {

    /**
     * Plays an animation
     *
     * @param animation animation to play
     */
    void play(Animation animation);

    /**
     * Plays an animation
     *
     * @param animation name of the animation to play
     */
    void play(String animation);
}
