package com.gasis.rts.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.math.MathUtils;
import com.gasis.rts.resources.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Plays all kinds of sounds
 */
public class SoundPlayer implements SoundPlayerInterface, Updatable {

    // game's assets
    private Resources resources;

    // time elapsed since the last sound update
    private float timer;

    // keeps track of how many times a certain sound has been played
    private Map<String, Integer> playCounts = new HashMap<String, Integer>();

    // how many times per second is the same sound allowed to be played
    private final int MAX_SAME_SOUND_PLAYS_PER_SECOND = 4;

    // how far away can sound effects be heard
    private final float SOUND_EFFECT_HEARING_RADIUS = 30f;

    // where is point of view located
    private float povX;
    private float povY;

    /**
     * Default class constructor
     */
    public SoundPlayer(Resources resources) {
        this.resources = resources;
    }

    /**
     * Plays music
     *
     * @param name name of the loaded music
     */
    @Override
    public Music playMusic(String name) {
        Music music = resources.music(name);

        music.setVolume(1);
        music.setLooping(false);

        music.play();

        return music;
    }

    /**
     * Plays a sound effect
     *
     * @param name name of the sound effect
     */
    @Override
    public void playSoundEffect(String name) {
        playSpatialSoundEffect(name, 1, 0);
    }

    /**
     * Plays a sound effect with spatial effect
     *
     * @param name   name of the sound effect
     * @param volume volume of the effect
     * @param pan    pan of the effect (between -1 and 1)
     */
    private void playSpatialSoundEffect(String name, float volume, float pan) {
        if (playCounts.containsKey(name) && playCounts.get(name) >= MAX_SAME_SOUND_PLAYS_PER_SECOND) {
            return;
        }

        Sound sound = resources.sound(name);
        sound.play(volume, 1, pan);

        if (playCounts.containsKey(name)) {
            playCounts.put(name, playCounts.get(name) + 1);
        } else {
            playCounts.put(name, 1);
        }
    }

    /**
     * Plays a sound effect at a specific position
     *
     * @param name name of the sound effect
     * @param x    x position of the sound effect
     * @param y    y position of the sound effect
     */
    @Override
    public void playSoundEffect(String name, float x, float y) {
        float distance = MathUtils.distance(x, povX, y, povY);

        if (distance < SOUND_EFFECT_HEARING_RADIUS) {
            playSpatialSoundEffect(name,
                    1 - distance / SOUND_EFFECT_HEARING_RADIUS * 0.5f,
                    (x - povX) / SOUND_EFFECT_HEARING_RADIUS);
        }
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        if (timer >= 1f) {
            if (!playCounts.isEmpty()) {
                for (String name : playCounts.keySet()) {
                    playCounts.put(name, 0);
                }
            }

            timer = 0;
        } else {
            timer += delta;
        }
    }

    /**
     * Sets the current position of the point of view
     *
     * @param x current x
     * @param y current y
     */
    public void setCurrentViewPosition(float x, float y) {
        povX = x;
        povY = y;
    }
}
