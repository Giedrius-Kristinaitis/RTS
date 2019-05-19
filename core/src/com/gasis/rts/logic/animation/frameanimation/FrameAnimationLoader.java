package com.gasis.rts.logic.animation.frameanimation;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.logic.animation.AnimationLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads a frame animation from an animation file
 */
public class FrameAnimationLoader extends AnimationLoader {

    // name of the texture atlas of the animation
    protected String atlas;

    // names of the frames of the animation
    protected final List<String> frames = new ArrayList<String>();

    // the duration of the animation in seconds
    protected float duration;

    // is the animation on loop
    protected boolean loop;

    // rotation in degrees around the center point (counter-clockwise)
    protected float rotation;

    // scale relative to the center point
    protected float scale;

    // dimensions of the animation
    protected float width;
    protected float height;

    // how fast the animation rotates in degrees every second
    protected float rotationSpeed;

    // the scale the animation should get to from start to finish
    protected float finalScale;

    // delay of the animation in seconds
    protected float delay;

    /**
     * Loads the animation from a file
     *
     * @param animationFile animation file to load
     *
     * @return true if the animation was loaded successfully
     */
    @Override
    public boolean load(FileHandle animationFile) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(animationFile.read()));

            // get data
            readData(reader);

            // read each frame line by line
            readFrames(reader);

            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return (loaded = true);
    }

    /**
     * Reads the data of the animation
     *
     * @param reader buffered reader to read from
     * @throws Exception
     */
    protected void readData(BufferedReader reader) throws Exception {
        atlas = reader.readLine().trim();
        width = Float.parseFloat(reader.readLine());
        height = Float.parseFloat(reader.readLine());
        delay = Float.parseFloat(reader.readLine());
        duration = Float.parseFloat(reader.readLine());
        loop = Boolean.parseBoolean(reader.readLine());
        rotation = Float.parseFloat(reader.readLine());
        rotationSpeed = Float.parseFloat(reader.readLine());
        scale = Float.parseFloat(reader.readLine());
        finalScale = Float.parseFloat(reader.readLine());
    }

    /**
     * Reads the frames of the animation
     *
     * @param reader buffered reader to read from
     * @throws Exception
     */
    protected void readFrames(BufferedReader reader) throws Exception {
        String line = null;

        while ((line = reader.readLine()) != null) {
            frames.add(line.trim());
        }
    }

    /**
     * Creates a new instance of the animation
     *
     * @return new instance of the loaded animation, null if the animation is not loaded
     */
    @Override
    public FrameAnimation newInstance() {
        if (!loaded) {
            throw new IllegalStateException("Animation not loaded");
        }

        FrameAnimation animation = new FrameAnimation();

        animation.setAtlas(atlas);
        animation.setFrames(frames);
        animation.setFrameCount(frames.size());
        animation.setWidth(width);
        animation.setHeight(height);
        animation.setDelay(delay);
        animation.setUpdateInterval(duration / (float) frames.size());
        animation.setLooping(loop);
        animation.setRotation(rotation);
        animation.setInitialRotation(rotation);
        animation.setRotationSpeed(rotationSpeed);
        animation.setScale(scale);
        animation.setInitialScale(scale);
        animation.setFinalScale(finalScale);

        return animation;
    }

    /**
     * Creates a new instance of the animation with the specified position
     *
     * @param x x coordinate of the animation
     * @param y y coordinate of the animation
     * @param finalX final x position of the animation
     * @param finalY final y position of the animation
     * @param center are the given coordinates of the center point or the bottom-left corner
     *
     * @return new instance of the loaded animation, null if the animation is not loaded
     */
    public FrameAnimation newInstance(float x, float y, float finalX, float finalY, boolean center) {
        FrameAnimation animation = newInstance();

        if (animation == null) {
            return null;
        }

        if (center) {
            animation.setCenterX(x);
            animation.setCenterY(y);
        } else {
            animation.setX(x);
            animation.setY(y);
        }

        animation.setInitialX(animation.getX());
        animation.setInitialY(animation.getY());
        animation.setFinalCenterX(finalX);
        animation.setFinalCenterY(finalY);

        return animation;
    }
}
