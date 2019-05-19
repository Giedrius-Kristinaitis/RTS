package com.gasis.rts.logic.animation.frameanimation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

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

    // initial position of the animation
    protected float initialX;
    protected float initialY;

    // final position of the animation (where the animation is when finished)
    protected float finalX;
    protected float finalY;

    // dimensions of the animation
    protected float width;
    protected float height;

    // rotation in degrees (relative to the center point) (counter-clockwise)
    protected float rotation;

    // how fast the animation rotates in degrees every second
    protected float rotationSpeed;

    // initial rotation of the animation
    protected float initialRotation;

    // scale of the animation
    protected float scale = 1f;

    // initial scale
    protected float initialScale = 1f;

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

    // how many frames are there in the animation
    protected int frameCount;

    // delay of the animation in seconds
    protected float delay;

    // how much time of the delay has elapsed
    protected float delayTime;

    /**
     * Adds an animation finish listener
     *
     * @param finishListener new finish listener
     */
    public void addFinishListener(AnimationFinishListener finishListener) {
        finishListeners.add(finishListener);
    }

    /**
     * Gets the delay of the animation
     * @return
     */
    public float getDelay() {
        return delay;
    }

    /**
     * Sets the delay of the animation
     *
     * @param delay new delay
     */
    public void setDelay(float delay) {
        if (delayTime != 0) {
            throw new IllegalStateException("Animation delay already started");
        }

        this.delay = delay;
    }

    /**
     * Gets the number of frames in this animation
     *
     * @return
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * Sets the number of frames of the animation
     *
     * @param frameCount new number of frames
     */
    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    /**
     * Gets the final x coordinate of the animation
     * @return
     */
    public float getFinalX() {
        return finalX;
    }

    /**
     * Gets the final y coordinate of the animation
     * @return
     */
    public float getFinalY() {
        return finalY;
    }

    /**
     * Gets the final x coordinate of the center point
     *
     * @return
     */
    public float getFinalCenterX() {
        return finalX + width / 2f;
    }

    /**
     * Gets the final y coordinate of the center point
     *
     * @return
     */
    public float getFinalCenterY() {
        return finalY + height / 2f;
    }

    /**
     * Gets the initial rotation
     * @return
     */
    public float getInitialRotation() {
        return initialRotation;
    }

    /**
     * Sets the initial rotation
     *
     * @param initialRotation new initial rotation
     */
    public void setInitialRotation(float initialRotation) {
        this.initialRotation = initialRotation;
    }

    /**
     * Sets the final x coordinate of the animation
     *
     * @param finalX new final x coordinate
     */
    public void setFinalX(float finalX) {
        this.finalX = finalX;
    }

    /**
     * Sets the final y coordinate of the animation
     *
     * @param finalY new final y coordinate
     */
    public void setFinalY(float finalY) {
        this.finalY = finalY;
    }

    /**
     * Sets the final center x coordinate of the animation
     *
     * @param finalX new final x coordinate
     */
    public void setFinalCenterX(float finalX) {
        this.finalX = finalX - width / 2f;
    }

    /**
     * Sets the final center y coordinate of the animation
     *
     * @param finalY new final y coordinate
     */
    public void setFinalCenterY(float finalY) {
        this.finalY = finalY - height / 2f;
    }

    /**
     * Gets the initial x coordinate
     * @return
     */
    public float getInitialX() {
        return initialX;
    }

    /**
     * Gets the initial y coordinate
     * @return
     */
    public float getInitialY() {
        return initialY;
    }

    /**
     * Gets the initial scale
     * @return
     */
    public float getInitialScale() {
        return initialScale;
    }

    /**
     * Sets the initial x coordinate
     *
     * @param initialX new initial x
     */
    public void setInitialX(float initialX) {
        this.initialX = initialX;
    }

    /**
     * Sets the initial y coordinate
     *
     * @param initialY new initial y
     */
    public void setInitialY(float initialY) {
        this.initialY = initialY;
    }

    /**
     * Gets the initial x coordinate of the center point
     * @return
     */
    public float getInitialCenterX() {
        return initialX + width / 2f;
    }

    /**
     * Gets the initial y coordinate of the center point
     * @return
     */
    public float getInitialCenterY() {
        return initialY + height / 2f;
    }

    /**
     * Sets the initial x coordinate of the center point
     *
     * @param x new initial center x
     */
    public void setInitialCenterX(float x) {
        initialX = x - width / 2f;
    }

    /**
     * Sets the initial y coordinate of the center point
     *
     * @param y new initial y
     */
    public void setInitialCenterY(float y) {
        initialY = y - height / 2f;
    }

    /**
     * Sets the initial scale
     *
     * @param initialScale new initial scale
     */
    public void setInitialScale(float initialScale) {
        this.initialScale = initialScale;
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
     * Gets the duration of the animation
     * @return
     */
    public float getDuration() {
        return frameCount * updateInterval;
    }

    /**
     * Resets the state of the animation
     */
    public void resetAnimation() {
        timeSinceLastUpdate = 0;
        currentFrame = 0;
        x = initialX;
        y = initialY;
        rotation = initialRotation;
        scale = initialScale;
        delayTime = 0;
        listenersNotified = false;
    }

    /**
     * Updates the animation
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        if (delayTime <= delay) {
            delayTime += delta;
            return;
        }

        // check if the animation is looping
        if (currentFrame == frameCount - 1 && !loop && (frameCount > 1 || timeSinceLastUpdate >= updateInterval * frameCount)) {
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

        // check if the animation needs to be reset
        else if (currentFrame == frameCount - 1 && loop && (timeSinceLastUpdate >= updateInterval * frameCount || frameCount > 1)) {
            x = initialX;
            y = initialY;
            scale = initialScale;
            rotation = initialRotation;
            currentFrame = 0;

            if (frameCount == 1) {
                timeSinceLastUpdate = 0;
            }
        }

        // update the animation frame
        if (frameCount > 1 && timeSinceLastUpdate >= updateInterval) {
            timeSinceLastUpdate = 0;

            currentFrame = currentFrame == frameCount - 1 ? 0 : currentFrame + 1;
        } else {
            timeSinceLastUpdate += delta;
        }

        // update the rotation of the animation
        rotation += rotationSpeed * delta;

        // update the scale of the animation
        scale += (finalScale - initialScale) / (frameCount * updateInterval) * delta;

        // update the position of the animation
        x += (finalX - initialX) / (frameCount * updateInterval) * delta;
        y += ((finalY - initialY) / (frameCount * updateInterval)) * delta;
    }

    /**
     * Renders the animation
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        if (delayTime <= delay) {
            return;
        }

        batch.draw(
                resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(frames.get(currentFrame)),
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
