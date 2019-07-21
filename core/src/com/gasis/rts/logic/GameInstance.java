package com.gasis.rts.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.map.MapRenderer;
import com.gasis.rts.logic.map.blockmap.*;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.logic.player.controls.PlayerControls;
import com.gasis.rts.resources.Resources;
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

    // size of the screen (expressed in screen coordinates)
    private int screenWidth;
    private int screenHeight;

    // half width and height of the screen (expressed in game coordinates)
    private float halfWidth;
    private float halfHeight;

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

    // current mouse coordinates (in screen units)
    private Vector3 screenCoords = new Vector3();

    private Test test;

    /**
     * Default class constructor
     *
     * @param resources resources used by the game
     */
    public GameInstance(Resources resources) {
        this.resources = resources;

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

        // create some test players
        Player one = new Player();
        Player two = new Player();

        one.initialize(Gdx.files.internal(Constants.FOLDER_FACTIONS + "rebels"), map);
        two.initialize(Gdx.files.internal(Constants.FOLDER_FACTIONS + "confederation"), map);

        players.add(two);
        players.add(one);

        // initialize player controls
        playerControls = new PlayerControls(map, two);
        playerControls2 = new PlayerControls(map, one);

        //test = new Test(map, two);
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

        //test.render(batch, resources);
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
        playerControls.update(delta);
        playerControls2.update(delta);

        mapZoomer.updateMapZoom(cam, delta);
        mapScroller.updateMapScroll(cam, delta);

        for (Player player: players) {
            for (Unit unit: player.getUnits()) {
                unit.update(delta);
            }

            for (Building building: player.getBuildings()) {
                building.update(delta);
            }
        }

        //test.update(delta);
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
        screenWidth = width;
        screenHeight = height;

        halfWidth = cam.viewportWidth / 2f;
        halfHeight = cam.viewportHeight / 2f;

        if (width > height) {
            halfHeight = halfWidth * ((float) height / (float) width);
        } else {
            halfWidth = halfHeight * ((float) width / (float) height);
        }

        mapScroller.setHalfScreenDimensions(halfWidth, halfHeight);
        mapScroller.setScreenDimensions(screenWidth, screenHeight);
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
