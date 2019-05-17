package com.gasis.rts.logic.animation;

import java.awt.*;

/**
 * Callback that gets called when an animation finishes
 */
public interface AnimationFinishListener {

    /**
     * Notifies the observer that the animation has finished
     *
     * @param animation the animation that just finished
     */
    void finished(FrameAnimation animation);
}
