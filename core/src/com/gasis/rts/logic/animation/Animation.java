package com.gasis.rts.logic.animation;

import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.render.Renderable;

/**
 * An animation
 */
public interface Animation extends Updatable, Renderable {

    /**
     * Checks if the animation has finished
     *
     * @return
     */
    boolean hasFinished();
}
