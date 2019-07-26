package com.gasis.rts.logic.animation;

import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;

/**
 * An animation
 */
public interface Animation extends Updatable, Renderable {

    /**
     * Checks if the animation has finished
     * @return
     */
    boolean hasFinished();
}
