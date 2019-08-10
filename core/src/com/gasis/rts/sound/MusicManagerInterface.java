package com.gasis.rts.sound;

/**
 * Manages music
 */
public interface MusicManagerInterface {

    /**
     * Adds a music track to be played by the manager
     *
     * @param name name of the track
     */
    void addTrack(String name);

    /**
     * Sets the music shuffle value
     *
     * @param shuffle are tracks being shuffled or played in order
     */
    void setShuffle(boolean shuffle);

    /**
     * Starts playing music
     */
    void start();

    /**
     * Stops playing music
     */
    void stop();
}
