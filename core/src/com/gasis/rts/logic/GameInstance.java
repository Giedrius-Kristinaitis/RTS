package com.gasis.rts.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gasis.rts.logic.animation.FrameAnimationPlayer;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.faction.Faction;
import com.gasis.rts.logic.map.MapRenderer;
import com.gasis.rts.logic.map.blockmap.*;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.combat.DestructionHandler;
import com.gasis.rts.logic.object.combat.TargetAssigner;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.logic.player.controls.PlayerControls;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.sound.MusicManager;
import com.gasis.rts.sound.SoundPlayer;
import com.gasis.rts.sound.SoundPlayerProvider;
import com.gasis.rts.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Game instance. Holds game state, draws the game world and updates it
 */
public class GameInstance implements Updatable {

    // resources used by the game
    private Resources resources;

    // the game world's camera
    private OrthographicCamera cam;

    // game's viewport
    private Viewport viewport;

    // renders the game map
    private MapRenderer mapRenderer;

    // game map
    private BlockMap map;

    // all players in the game
    private List<Player> players = new ArrayList<Player>();

    // the object that handles controlling of a player's point of view
    private PlayerControls playerControls;

    private PlayerControls playerControls2;

    // map scrolling logic
    private BlockMapScroller mapScroller;

    // map zooming logic
    private BlockMapZoomer mapZoomer;

    // handle destruction
    private DestructionHandler destructionHandler;

    // plays animations
    private FrameAnimationPlayer animationPlayer;

    // assigns targets to offensive objects
    private TargetAssigner targetAssigner;

    // current mouse coordinates (in screen units)
    private Vector3 screenCoords = new Vector3();

    // used to play sounds
    private SoundPlayer soundPlayer;

    // used to play music
    private MusicManager musicManager;

    /**
     * Default class constructor
     *
     * @param resources resources used by the game
     */
    public GameInstance(Resources resources) {
        this.resources = resources;

        // initialize sounds and music
        soundPlayer = new SoundPlayer(resources);
        musicManager = new MusicManager(resources, soundPlayer);

        SoundPlayerProvider.initialize(soundPlayer);

        // initialize the map
        map = new BlockMapGenerator().generate(Gdx.files.internal(Constants.FOLDER_MAPS + "main.map"));

        // initialize the map renderer
        mapRenderer = new BlockMapRenderer();
        mapRenderer.setRenderedMap(map);

        // load all animations in advance
        FrameAnimationFactory.loadAnimations();

        // initialize map scroller and zoomer
        mapScroller = new BlockMapScroller(map, mapRenderer);
        mapZoomer = new BlockMapZoomer();

        // initialize animation player
        animationPlayer = new FrameAnimationPlayer();

        // initialize destruction handler
        destructionHandler = new DestructionHandler(map, animationPlayer);

        // initialize target assigner
        targetAssigner = new TargetAssigner();

        // create some test players
        Player one = new Player(destructionHandler, targetAssigner, map);
        Player two = new Player(destructionHandler, targetAssigner, map);

        one.initialize(Gdx.files.internal(Constants.FOLDER_FACTIONS + "rebels"), map);
        two.initialize(Gdx.files.internal(Constants.FOLDER_FACTIONS + "confederation"), map);

        players.add(two);
        players.add(one);

        targetAssigner.setPlayers(players, map);

        // initialize soundtracks
        initializeSoundtrack(two.getFaction());
        initializeSoundtrack(one.getFaction());

        musicManager.setShuffle(true);
        musicManager.start();

        // initialize player controls
        playerControls = new PlayerControls(map, two, targetAssigner);
        playerControls2 = new PlayerControls(map, one, targetAssigner);
    }

    /**
     * Initializes faction's soundtrack
     *
     * @param faction faction to get soundtrack from
     */
    private void initializeSoundtrack(Faction faction) {
        for (String soundtrack: faction.getSoundtrack()) {
            musicManager.addTrack(Constants.FOLDER_SOUNDS + soundtrack);
        }
    }

    /**
     * Called when the game should render itself
     *
     * @param batch sprite batch to draw sprites with
     */
    public void draw(SpriteBatch batch) {
        mapRenderer.render(batch, resources);

        for (Player player: players) {
            for (Unit unit: player.getUnits()) {
                unit.render(batch, resources);
            }

            for (Building building: player.getBuildings()) {
                building.render(batch, resources);
            }
        }

        animationPlayer.render(batch, resources);

        playerControls.render(batch, resources);
        playerControls2.render(batch, resources);
    }

    /**
     * Draws texture-less shapes
     *
     * @param shapeRenderer renderer to draw shapes to
     */
    public void draw(ShapeRenderer shapeRenderer) {
        playerControls.render(shapeRenderer);
        playerControls2.render(shapeRenderer);
    }

    /**
     * Called when the game state should be updated
     *
     * @param delta time elapsed since last update
     */
    @Override
    public void update(float delta) {
        soundPlayer.update(delta);
        musicManager.update(delta);

        playerControls.update(delta);
        playerControls2.update(delta);

        mapZoomer.updateMapZoom(cam, delta);
        mapScroller.updateMapScroll(cam, delta);

        animationPlayer.update(delta);

        for (Player player: players) {
            for (Unit unit: player.getUnits()) {
                unit.update(delta);
            }

            for (Building building: player.getBuildings()) {
                building.update(delta);
            }

            player.update(delta);
        }
    }


    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in Input.Keys
     */
    public void keyDown(int keycode) {
        mapScroller.startScrolling(keycode);

        playerControls.keyDown(keycode);
        playerControls2.keyDown(keycode);

        switch (keycode) {
            case Input.Keys.F11:
                toggleFullscreen();
                break;
        }
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in Input.Keys
     */
    public void keyUp(int keycode) {
        mapScroller.stopScrolling(keycode);
        playerControls.keyUp(keycode);
        playerControls2.keyUp(keycode);
    }

    /**
     * Called when the screen was touched or a mouse button was pressed
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchDown(int screenX, int screenY, int pointer, int button) {
        convertScreenCoordsToWorldCoords(screenX, screenY);

        playerControls.touchDown(screenCoords.x, screenCoords.y, pointer, button);
        playerControls2.touchDown(screenCoords.x, screenCoords.y, pointer, button);
    }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchUp(int screenX, int screenY, int pointer, int button) {
        convertScreenCoordsToWorldCoords(screenX, screenY);

        playerControls.touchUp(screenCoords.x, screenCoords.y, pointer, button);
        playerControls2.touchUp(screenCoords.x, screenCoords.y, pointer, button);
    }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event
     */
    public void touchDragged(int screenX, int screenY, int pointer) {
        mouseMoved(screenX, screenY);

        convertScreenCoordsToWorldCoords(screenX, screenY);

        playerControls.touchDragged(screenCoords.x, screenCoords.y, pointer);
        playerControls2.touchDragged(screenCoords.x, screenCoords.y, pointer);
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     */
    public void mouseMoved(int screenX, int screenY) {
        mapScroller.initiateMouseScrolling(screenX, screenY);

        convertScreenCoordsToWorldCoords(screenX, screenY);

        playerControls.mouseMoved(screenCoords.x, screenCoords.y);
        playerControls2.mouseMoved(screenCoords.x, screenCoords.y);
    }

    /**
     * Converts screen coordinates into world's coordinates
     *
     * @param screenX
     * @param screenY
     * @return
     */
    protected Vector3 convertScreenCoordsToWorldCoords(int screenX, int screenY) {
        screenCoords.x = screenX;
        screenCoords.y = screenY;
        cam.unproject(screenCoords, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        return screenCoords;
    }

    /**
     * Called when the mouse wheel is scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     */
    public void scrolled(int amount) {
        mapZoomer.updateZoomSpeed(amount);
    }

    /**
     * Called when the size of the screen changes
     *
     * @param width new width
     * @param height new height
     */
    public void screenSizeChanged(int width, int height) {
        float halfHeight = cam.viewportHeight / 2f;
        float halfWidth = cam.viewportWidth / 2f;

        if (halfWidth < halfHeight * ((float) width / (float) height)) {
            halfHeight = halfWidth * ((float) height / (float) width);
        } else {
            halfWidth = halfHeight * ((float) width / (float) height);
        }

        mapScroller.setHalfScreenDimensions(halfWidth, halfHeight);
        mapScroller.setScreenDimensions(width, height);
    }

    /**
     * Enters or leaves fullscreen mode
     */
    public void toggleFullscreen() {
        Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();

        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setWindowedMode(mode.width, mode.height);
        } else {
            Gdx.graphics.setFullscreenMode(mode);
        }
    }

    /**
     * Sets the camera for the game
     *
     * @param cam camera to use
     */
    public void setCamera(OrthographicCamera cam) {
        this.cam = cam;
    }

    /**
     * Sets the game's viewport
     *
     * @param viewport viewport to use
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    /**
     * Cleans up resources
     */
    public void unloadResources() {

    }
}
