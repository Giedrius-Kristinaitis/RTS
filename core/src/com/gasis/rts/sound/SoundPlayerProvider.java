package com.gasis.rts.sound;

import com.gasis.rts.resources.Resources;

/**
 * Provides sound player's instance
 */
public class SoundPlayerProvider {

    // the sound player
    private static SoundPlayerInterface soundPlayer;

    /**
     * Gets sound player instance
     * @return
     */
    public static SoundPlayerInterface getSoundPlayer() {
        return soundPlayer;
    }

    /**
     * Initializes the sound player instance
     *
     * @param resources game's assets
     */
    public static void initialize(Resources resources) {
        soundPlayer = new SoundPlayer(resources);
    }

    /**
     * Initializes the sound player instance
     *
     * @param soundPlayerInstance sound player instance to use
     */
    public static void initialize(SoundPlayerInterface soundPlayerInstance) {
        soundPlayer = soundPlayerInstance;
    }
}
