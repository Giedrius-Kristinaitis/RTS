package com.gasis.rts.logic.animation;

import com.badlogic.gdx.files.FileHandle;

/**
 * Loads an animation from an animation file
 */
public abstract class AnimationLoader {

    // has the animation been loaded from the file
    protected boolean loaded = false;

    /**
     * Loads the animation from a file
     *
     * @param animationFile animation file to load
     *
     * @return true if the animation was loaded successfully
     */
    public abstract boolean load(FileHandle animationFile);

    /**
     * Creates a new instance of the animation
     *
     * @return new instance of the loaded animation, null if the animation is not loaded
     */
    public abstract Animation newInstance();
}
