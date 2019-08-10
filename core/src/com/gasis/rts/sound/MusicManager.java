package com.gasis.rts.sound;

import com.badlogic.gdx.audio.Music;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.resources.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages music
 */
public class MusicManager implements MusicManagerInterface, Updatable, Music.OnCompletionListener {

    // used to generate random track indexes when shuffling music
    private final Random random = new Random();

    // game's assets
    private Resources resources;

    // used to play music
    private SoundPlayerInterface soundPlayer;

    // music tracks managed by the music manager
    private List<String> tracks = new ArrayList<String>();

    // are the tracks being shuffled
    private boolean shuffle = false;

    // the index of the currently playing track
    private int currentTrackIndex = -1;

    // the index of the next track that will be played
    private int nextTrackIndex = -1;

    // the currently playing track
    private Music currentTrack;

    /**
     * Default class constructor
     */
    public MusicManager(Resources resources, SoundPlayerInterface soundPlayer) {
        this.resources = resources;
        this.soundPlayer = soundPlayer;
    }

    /**
     * Adds a music track to be played by the manager
     *
     * @param name name of the track
     */
    @Override
    public void addTrack(String name) {
        tracks.add(name);
    }

    /**
     * Sets the music shuffle value
     *
     * @param shuffle are tracks being shuffled or played in order
     */
    @Override
    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    /**
     * Starts playing music
     */
    @Override
    public void start() {
        startNextTrack();
    }

    /**
     * Stops playing music
     */
    @Override
    public void stop() {
        stopCurrentTrack();
    }

    /**
     * Starts playing the next music track
     */
    private void startNextTrack() {
        if (nextTrackIndex == -1) {
            assignNextTrackIndex();
            enqueueNextTrackToBeLoaded();
        }

        currentTrackIndex = nextTrackIndex;

        if (!resources.isLoaded(tracks.get(currentTrackIndex))) {
            resources.finishLoading();
        }

        currentTrack = soundPlayer.playMusic(tracks.get(currentTrackIndex));

        if (currentTrack != null) {
            currentTrack.setOnCompletionListener(this);
        }

        assignNextTrackIndex();
        enqueueNextTrackToBeLoaded();
    }

    /**
     * Enqueues the next music track to be loaded by the resource manager
     */
    private void enqueueNextTrackToBeLoaded() {
        if (!resources.isLoaded(tracks.get(nextTrackIndex))) {
            resources.load(tracks.get(nextTrackIndex), Music.class);
        }
    }

    /**
     * Stops the current music track
     */
    private void stopCurrentTrack() {
        if (currentTrack != null) {
            currentTrack.stop();
            resources.unload(tracks.get(currentTrackIndex), Music.class);
            currentTrack = null;
        }
    }

    /**
     * Assigns the index of the next track that will be played
     */
    private void assignNextTrackIndex() {
        nextTrackIndex = nextTrackIndex == tracks.size() - 1 ? 0 : nextTrackIndex + 1;

        if (shuffle) {
            nextTrackIndex = random.nextInt(tracks.size());
        }
    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param music the Music that reached the end of the file
     */
    @Override
    public void onCompletion(Music music) {
        startNextTrack();
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        if (nextTrackIndex != -1 && !resources.isLoaded(tracks.get(nextTrackIndex))) {
            resources.update();
        }
    }
}
