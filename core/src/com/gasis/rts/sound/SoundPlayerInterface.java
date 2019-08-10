package com.gasis.rts.sound;

/**
 * Plays all kinds of sounds
 */
public interface SoundPlayerInterface {

    /**
     * Plays music
     *
     * @param name name of the loaded music
     */
    void playMusic(String name);

    /**
     * Plays a sound effect
     *
     * @param name name of the sound effect
     */
    void playSoundEffect(String name);
}
