package com.gasis.rts.sound;

import com.badlogic.gdx.audio.Music;
import com.gasis.rts.resources.Resources;

/**
 * Plays all kinds of sounds
 */
public class SoundPlayer implements SoundPlayerInterface, Music.OnCompletionListener {

    // game's assets
    private Resources resources;

    // how many sound effects are allowed to be played at once
    private final int MAX_CONCURRENT_SOUND_EFFECTS = 10;

    // how many sound effects are currently playing
    private int soundEffectCount;

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
    public void playMusic(String name) {
        Music music = resources.music(name);

        music.setVolume(1);
        music.setLooping(false);

        music.play();
    }

    /**
     * Plays a sound effect
     *
     * @param name name of the sound effect
     */
    @Override
    public void playSoundEffect(String name) {
        if (soundEffectCount < MAX_CONCURRENT_SOUND_EFFECTS) {
            Music sound = resources.music(name);

            sound.setOnCompletionListener(this);
            sound.setLooping(false);
            sound.setVolume(1);

            sound.play();

            soundEffectCount++;
        }
    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param music the Music that reached the end of the file
     */
    @Override
    public void onCompletion(Music music) {
        soundEffectCount--;
    }
}
