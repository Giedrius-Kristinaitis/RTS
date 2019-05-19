package com.gasis.rts.logic.animation;

/**
 * Creates animations
 */
public interface AnimationFactory {

    /**
     * Creates a new instance of the specified animation
     *
     * @param animation id of the animation
     * @return
     */
    Animation create(short animation);
}
