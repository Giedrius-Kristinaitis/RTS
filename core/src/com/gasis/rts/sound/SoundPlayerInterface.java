package com.gasis.rts.sound;

import com.badlogic.gdx.audio.Music;

/**
 * Plays all kinds of sounds
 */
public interface SoundPlayerInterface {

    /**
     * Plays music
     *
     * @param name name of the loaded music
     */
    Music playMusic(String name);

    /**
     * Plays a sound effect at a specific position
     *
     * @param name name of the sound effect
     * @param x x position of the sound effect
     * @param y y position of the sound effect
     */
    void playSoundEffect(String name, float x, float y);

    /**
     * Plays a sound effect
     *
     * @param name name of the sound effect
     */
    void playSoundEffect(String name);
}
