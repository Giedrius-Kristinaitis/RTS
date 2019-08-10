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
     * Plays a sound effect
     *
     * @param name name of the sound effect
     */
    Music playSoundEffect(String name);
}
