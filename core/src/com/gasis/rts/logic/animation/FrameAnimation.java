package com.gasis.rts.logic.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * An animation that changes frames
 */
@SuppressWarnings("unused")
public class FrameAnimation implements Animation {

    // how often the animation frame changes (in seconds)
    protected float updateInterval;

    // how much time has elapsed since the last frame change
    protected float timeSinceLastUpdate;

    // the frame that is currently visible
    protected int currentFrame;

    // is the animation played on loop (forever) ?
    protected boolean loop;

    // position of the animation
    protected float x;
    protected float y;

    // dimensions of the animation
    protected float width;
    protected float height;

    // rotation in degrees (relative to the center point) (counter-clockwise)
    protected float rotation;

    // how fast the animation rotates in degrees every second
    protected float rotationSpeed;

    // scale of the animation
    protected float scale = 1f;

    // the scale the animation should get to from start to finish
    protected float finalScale = 1f;

    // the animation observers who wait for the animation to finish
    protected List<AnimationFinishListener> finishListeners = new ArrayList<AnimationFinishListener>();

    // have the listeners been notified or not
    protected boolean listenersNotified = false;

    // name of the texture atlas
    protected String atlas;

    // frames of the animation
    protected List<String> frames;

    /**
     * Adds an animation finish listener
     *
     * @param finishListener new finish listener
     */
    public void addFinishListener(AnimationFinishListener finishListener) {
        finishListeners.add(finishListener);
    }

    /**
     * Gets the rotation speed (how many degrees in one second)
     * @return
     */
    public float getRotationSpeed() {
        return rotationSpeed;
    }

    /**
     * Gets the final scale of the animation
     * @return
     */
    public float getFinalScale() {
        return finalScale;
    }

    /**
     * Sets the rotation speed of the animation
     *
     * @param rotationSpeed how many degrees in one second
     */
    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    /**
     * Sets the final scale of the animation
     *
     * @param finalScale new final scale relative to the center of the animation
     */
    public void setFinalScale(float finalScale) {
        this.finalScale = finalScale;
    }

    /**
     * Gets the texture atlas of the animation
     * @return
     */
    public String getAtlas() {
        return atlas;
    }

    /**
     * Gets the frames of the animation
     *
     * @return iterable with frames
     */
    public Iterable<String> getFrames() {
        return frames;
    }

    /**
     * Sets the texture atlas for the animation
     *
     * @param atlas name of the new atlas
     */
    public void setAtlas(String atlas) {
        this.atlas = atlas;
    }

    /**
     * Sets the frames of the animation
     *
     * @param frames new frame list
     */
    public void setFrames(List<String> frames) {
        this.frames = frames;
    }

    /**
     * Gets the x coordinate of the animation
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the animation
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the width of the animation
     * @return
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height of the animation
     * @return
     */
    public float getHeight() {
        return height;
    }

    /**
     * Gets the rotation of the animation in degrees
     * @return
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Sets the x coordinate of the animation
     *
     * @param x new x position
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate of the animation
     *
     * @param y new y position
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the width of the animation
     *
     * @param width new width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Sets the height of the animation
     *
     * @param height new height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Sets the rotation of the animation
     *
     * @param rotation rotation in degrees
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * Sets the center x position of the animation
     *
     * @param centerX new center x position
     */
    public void setCenterX(float centerX) {
        x = centerX - width / 2;
    }

    /**
     * Sets the center y position of the animation
     *
     * @param centerY new center y position
     */
    public void setCenterY(float centerY) {
        y = centerY - height / 2;
    }

    /**
     * Gets the x position of the center point of the animation
     * @return
     */
    public float getCenterX() {
        return x + width / 2;
    }

    /**
     * Gets the y position of the center point of the animation
     * @return
     */
    public float getCenterY() {
        return y + height / 2;
    }

    /**
     * Gets the scale of the animation
     * @return
     */
    public float getScale() {
        return scale;
    }

    /**
     * Sets the scale of the animation
     *
     * @param scale new scale
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Checks if the animation is looping or not
     * @return
     */
    public boolean isLooping() {
        return loop;
    }

    /**
     * Sets the animation's loop value
     *
     * @param looping new loop value
     */
    public void setLooping(boolean looping) {
        this.loop = looping;
    }

    /**
     * Gets the frame change interval
     * @return
     */
    public float getUpdateInterval() {
        return updateInterval;
    }

    /**
     * Sets the frame change interval
     *
     * @param updateInterval new frame change interval
     */
    public void setUpdateInterval(float updateInterval) {
        this.updateInterval = updateInterval;
    }

    /**
     * Resets the state of the animation
     */
    public void resetAnimation() {
        timeSinceLastUpdate = 0;
        currentFrame = 0;
    }

    /**
     * Updates the animation
     *
     * @param delta time elapsed since the last render
     */
    public void update(float delta) {
        // check if the animation is looping
        if (currentFrame == frames.size() - 1 && !loop) {
            // notify finish listeners
            if (timeSinceLastUpdate >= updateInterval && !listenersNotified) {
                listenersNotified = true;

                for (AnimationFinishListener listener: finishListeners) {
                    listener.finished(this);
                }
            } else if (!listenersNotified) {
                timeSinceLastUpdate += delta;
            }

            return;
        }

        // update the animation frame
        if (timeSinceLastUpdate >= updateInterval) {
            timeSinceLastUpdate = 0;

            currentFrame = currentFrame == frames.size() - 1 ? 0 : currentFrame + 1;
        } else {
            timeSinceLastUpdate += delta;
        }

        // update the rotation of the animation
        rotation += rotationSpeed * delta;

        // update the scale of the animation
        scale += (finalScale - scale) / (frames.size() * updateInterval) * delta;
    }

    /**
     * Renders the animation
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    public void render(SpriteBatch batch, Resources resources) {
        batch.draw(
                resources.atlas(atlas).findRegion(frames.get(currentFrame)),
                x,
                y,
                width / 2,
                height / 2,
                width,
                height,
                scale,
                scale,
                rotation
        );
    }
}
