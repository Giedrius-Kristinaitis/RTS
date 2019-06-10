package com.gasis.rts.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.map.MapRenderer;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.map.blockmap.BlockMapGenerator;
import com.gasis.rts.logic.map.blockmap.BlockMapRenderer;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.object.building.OffensiveBuilding;
import com.gasis.rts.logic.object.unit.RotatingGunUnit;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.UnitLoader;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

/**
 * Game instance. Holds game state, draws the game world and updates it
 */
public class GameInstance implements Updatable {

    // resources used by the game
    private Resources resources;

    // the game world's camera
    private OrthographicCamera cam;

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

    // the current zoom value
    private float zoom = 1;

    // zoom limitations
    private final float maxZoomIn = 0.25f;
    private final float maxZoomOut = 1.4f;

    // how much the zoom changes per second
    private float zoomSpeed;

    // the speeds at which the map is being scrolled
    private float xScrollSpeed;
    private float yScrollSpeed;

    // the maximum scroll speed per second(both positive and negative)
    private final float maxScrollSpeed = 20;

    // is one of the map scroll keys currently held down
    private boolean scrollingUp;
    private boolean scrollingDown;
    private boolean scrollingLeft;
    private boolean scrollingRight;

    // how close to the screen edge the mouse has to be in order to start
    // scrolling the map (expressed in screen width and height percentage)
    private final float mouseTriggeredScrollBounds = 0.025f;

    private RotatingGunUnit unit1;
    private RotatingGunUnit unit2;
    private RotatingGunUnit unit3;
    private RotatingGunUnit unit4;
    private RotatingGunUnit unit5;
    private RotatingGunUnit unit6;
    private RotatingGunUnit unit7;
    private RotatingGunUnit unit8;

    private Unit zeus1;
    private Unit zeus2;
    private Unit zeus3;
    private Unit zeus4;
    private Unit zeus5;
    private Unit zeus6;
    private Unit zeus7;
    private Unit zeus8;

    private Building plant;
    private OffensiveBuilding launcher;
    private OffensiveBuilding launcher2;
    private OffensiveBuilding launcher3;
    private OffensiveBuilding launcher4;
    private Building factory;
    private Building factory2;

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

        UnitLoader loader = new UnitLoader();
        loader.load(Gdx.files.internal(Constants.FOLDER_UNITS + "rhino"));

        unit1 = (RotatingGunUnit) loader.newInstance();
        unit2 = (RotatingGunUnit) loader.newInstance();
        unit3 = (RotatingGunUnit) loader.newInstance();
        unit4 = (RotatingGunUnit) loader.newInstance();
        unit5 = (RotatingGunUnit) loader.newInstance();
        unit6 = (RotatingGunUnit) loader.newInstance();
        unit7 = (RotatingGunUnit) loader.newInstance();
        unit8 = (RotatingGunUnit) loader.newInstance();

        unit1.setX(3.75f);
        unit1.setY(8.9f);
        unit1.rotateToDirection(Unit.EAST);
        unit1.aimAt(8, 8);

        unit2.setX(2.5f);
        unit2.setY(7.5f);
        unit2.rotateToDirection(Unit.SOUTH_EAST);
        unit2.aimAt(8, 8);

        unit3.setX(2);
        unit3.setY(5);
        unit3.rotateToDirection(Unit.SOUTH);
        unit3.aimAt(8, 8);

        unit4.setX(5);
        unit4.setY(3.5f);
        unit4.rotateToDirection(Unit.SOUTH_WEST);
        unit4.aimAt(8.5f, 8.5f);

        unit5.setX(13f);
        unit5.setY(6.5f);
        unit5.rotateToDirection(Unit.WEST);
        unit5.aimAt(8.5f, 8.5f);

        unit6.setX(5);
        unit6.setY(7.5f);
        unit6.rotateToDirection(Unit.NORTH_WEST);
        unit6.aimAt(8.5f, 8.5f);

        unit7.setX(5);
        unit7.setY(10);
        unit7.rotateToDirection(Unit.NORTH);
        unit7.aimAt(8.5f, 8.5f);

        unit8.setX(7.5f);
        unit8.setY(5f);
        unit8.rotateToDirection(Unit.NORTH_EAST);
        unit8.aimAt(8.5f, 8.5f);

        UnitLoader zeusLoader = new UnitLoader();
        zeusLoader.load(Gdx.files.internal(Constants.FOLDER_UNITS + "zeus"));

        zeus1 = zeusLoader.newInstance();
        zeus2 = zeusLoader.newInstance();
        zeus3 = zeusLoader.newInstance();
        zeus4 = zeusLoader.newInstance();
        zeus5 = zeusLoader.newInstance();
        zeus6 = zeusLoader.newInstance();
        zeus7 = zeusLoader.newInstance();
        zeus8 = zeusLoader.newInstance();

        zeus1.setX(1.75f);
        zeus1.setY(8.9f);
        zeus1.rotateToDirection(Unit.EAST);
        zeus1.aimAt(8, 8);

        zeus2.setX(2.5f);
        zeus2.setY(6.25f);
        zeus2.rotateToDirection(Unit.SOUTH_EAST);
        zeus2.aimAt(8, 8);

        zeus3.setX(4);
        zeus3.setY(5);
        zeus3.rotateToDirection(Unit.SOUTH);
        zeus3.aimAt(8, 8);

        zeus4.setX(8.2f);
        zeus4.setY(3.5f);
        zeus4.rotateToDirection(Unit.SOUTH_WEST);
        zeus4.aimAt(8.5f, 8.5f);

        zeus5.setX(11f);
        zeus5.setY(9f);
        zeus5.rotateToDirection(Unit.WEST);
        zeus5.aimAt(8.5f, 8.5f);

        zeus6.setX(9.65f);
        zeus6.setY(10.45f);
        zeus6.rotateToDirection(Unit.NORTH_WEST);
        zeus6.aimAt(8.5f, 8.5f);

        zeus7.setX(6);
        zeus7.setY(11);
        zeus7.rotateToDirection(Unit.NORTH);
        zeus7.aimAt(8.5f, 8.5f);

        zeus8.setX(6f);
        zeus8.setY(5f);
        zeus8.rotateToDirection(Unit.NORTH_EAST);
        zeus8.aimAt(8.5f, 8.5f);

        BuildingLoader buildingLoader = new BuildingLoader();
        buildingLoader.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + "power_plant_rebels"));

        plant = buildingLoader.newInstance();
        plant.setX(0);
        plant.setY(0);
        plant.initializeAnimations();

        BuildingLoader launcherLoader = new BuildingLoader();
        launcherLoader.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + "rocket_launcher_conf"));

        launcher = (OffensiveBuilding) launcherLoader.newInstance();
        launcher.setX(15.5f);
        launcher.setY(10);
        launcher.aimAt(8.5f, 8.5f);

        launcher2 = (OffensiveBuilding) launcherLoader.newInstance();
        launcher2.setX(15.5f);
        launcher2.setY(7);
        launcher2.aimAt(8.5f, 8.5f);

        launcher3 = (OffensiveBuilding) launcherLoader.newInstance();
        launcher3.setX(4);
        launcher3.setY(1.5f);
        launcher3.aimAt(8.5f, 8.5f);

        launcher4 = (OffensiveBuilding) launcherLoader.newInstance();
        launcher4.setX(0);
        launcher4.setY(5);
        launcher4.aimAt(8.5f, 8.5f);

        BuildingLoader factoryLoader = new BuildingLoader();
        factoryLoader.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + "machine_factory_conf"));

        factory = factoryLoader.newInstance();
        factory.setX(10);
        factory.setY(0);
        factory.initializeAnimations();

        BuildingLoader factoryLoader2 = new BuildingLoader();
        factoryLoader2.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + "machine_factory_rebels"));

        factory2 = factoryLoader2.newInstance();
        factory2.setX(16);
        factory2.setY(0);
        factory2.initializeAnimations();
    }

    /**
     * Called when the game should render itself
     *
     * @param batch sprite batch to draw sprites with
     */
    public void draw(SpriteBatch batch) {
        mapRenderer.render(batch, resources);

        unit1.render(batch, resources);
        unit2.render(batch, resources);
        unit3.render(batch, resources);
        unit4.render(batch, resources);
        unit5.render(batch, resources);
        unit6.render(batch, resources);
        unit7.render(batch, resources);
        unit8.render(batch, resources);

        zeus1.render(batch, resources);
        zeus2.render(batch, resources);
        zeus3.render(batch, resources);
        zeus4.render(batch, resources);
        zeus5.render(batch, resources);
        zeus6.render(batch, resources);
        zeus7.render(batch, resources);
        zeus8.render(batch, resources);

        plant.render(batch, resources);

        launcher.render(batch, resources);
        launcher2.render(batch, resources);
        launcher3.render(batch, resources);
        launcher4.render(batch, resources);

        factory.render(batch, resources);
        factory2.render(batch, resources);
    }

    /**
     * Called when the game state should be updated
     *
     * @param delta time elapsed since last update
     */
    @Override
    public void update(float delta) {
        updateZoom(cam, delta);
        updateScroll(delta);

        unit1.update(delta);
        unit2.update(delta);
        unit3.update(delta);
        unit4.update(delta);
        unit5.update(delta);
        unit6.update(delta);
        unit7.update(delta);
        unit8.update(delta);

        zeus1.update(delta);
        zeus2.update(delta);
        zeus3.update(delta);
        zeus4.update(delta);
        zeus5.update(delta);
        zeus6.update(delta);
        zeus7.update(delta);
        zeus8.update(delta);

        plant.update(delta);

        launcher.update(delta);
        launcher2.update(delta);
        launcher3.update(delta);
        launcher4.update(delta);

        factory.update(delta);
        factory2.update(delta);

        if (Gdx.input.isTouched()) {
            unit1.setInSiegeMode(!unit1.isInSiegeMode());
            unit2.setInSiegeMode(!unit2.isInSiegeMode());
            unit3.setInSiegeMode(!unit3.isInSiegeMode());
            unit4.setInSiegeMode(!unit4.isInSiegeMode());
            unit5.setInSiegeMode(!unit5.isInSiegeMode());
            unit6.setInSiegeMode(!unit6.isInSiegeMode());
            unit7.setInSiegeMode(!unit7.isInSiegeMode());
            unit8.setInSiegeMode(!unit8.isInSiegeMode());

            unit1.rotateToDirection(Unit.WEST);
            unit2.rotateToDirection(Unit.NORTH_WEST);
            unit3.rotateToDirection(Unit.NORTH);
            unit4.rotateToDirection(Unit.SOUTH_WEST);
            unit5.rotateToDirection(Unit.SOUTH);
            unit6.rotateToDirection(Unit.SOUTH);
            unit7.rotateToDirection(Unit.NORTH_EAST);
        }
    }

    /**
     * Updates the map's scrolling
     *
     * @param delta time elapsed since the last update
     */
    @SuppressWarnings("Duplicates") // ain't gonna write a separate method for this 'duplication'
    private void updateScroll(float delta) {
        cam.position.x += xScrollSpeed * delta;
        cam.position.y += yScrollSpeed * delta;

        // update the vertical scrolling
        if (scrollingUp) {
            yScrollSpeed += maxScrollSpeed * delta * 2;

            if (yScrollSpeed > maxScrollSpeed) {
                yScrollSpeed = maxScrollSpeed;
            }
        } else if (scrollingDown) {
            yScrollSpeed -= maxScrollSpeed * delta * 2;

            if (yScrollSpeed < -maxScrollSpeed) {
                yScrollSpeed = -maxScrollSpeed;
            }
        } else {
            yScrollSpeed *= Math.pow(Math.max(0, 1f - delta), 4);
        }

        // update the horizontal scrolling
        if (scrollingLeft) {
            xScrollSpeed -= maxScrollSpeed * delta * 2;

            if (xScrollSpeed < -maxScrollSpeed) {
                xScrollSpeed = -maxScrollSpeed;
            }
        } else if (scrollingRight) {
            xScrollSpeed += maxScrollSpeed * delta * 2;

            if (xScrollSpeed > maxScrollSpeed) {
                xScrollSpeed = maxScrollSpeed;
            }
        } else {
            xScrollSpeed *= Math.pow(Math.max(0, 1f - delta), 4);
        }

        // make sure the camera is in the bounds of the map
        if (cam.position.x < halfWidth * cam.zoom) {
            cam.position.x = halfWidth * cam.zoom;
            xScrollSpeed = 0;
        } else if (cam.position.x > map.getWidth() * Block.BLOCK_WIDTH - halfWidth * cam.zoom) {
            cam.position.x = map.getWidth() * Block.BLOCK_WIDTH - halfWidth * cam.zoom;
            xScrollSpeed = 0;
        }

        if (cam.position.y < halfHeight * cam.zoom) {
            cam.position.y = halfHeight * cam.zoom;
            yScrollSpeed = 0;
        } else if (cam.position.y > map.getHeight() * Block.BLOCK_HEIGHT - halfHeight * cam.zoom) {
            cam.position.y = map.getHeight() * Block.BLOCK_HEIGHT - halfHeight * cam.zoom;
            yScrollSpeed = 0;
        }

        // make sure the correct portion of the map is rendered
        mapRenderer.setRenderX(cam.position.x - cam.viewportWidth / 2f - 2);
        mapRenderer.setRenderY(cam.position.y - cam.viewportHeight / 2f - 2);

        // make sure the render width and height is always up to date
        mapRenderer.setRenderWidth((cam.viewportWidth / Block.BLOCK_WIDTH + 2) * Math.max(1, cam.zoom));
        mapRenderer.setRenderHeight((cam.viewportHeight / Block.BLOCK_HEIGHT + 2) * Math.max(1, cam.zoom));
    }

    /**
     * Starts scrolling the map
     *
     * @param keycode code of the pressed key
     */
    private void startScrolling(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                if (!scrollingUp) {
                    yScrollSpeed = 0;
                }

                scrollingUp = true;
                scrollingDown = false;
                break;
            case Input.Keys.DOWN:
                if (!scrollingDown) {
                    yScrollSpeed = 0;
                }

                scrollingDown = true;
                scrollingUp = false;
                break;
            case Input.Keys.LEFT:
                if (!scrollingLeft) {
                    xScrollSpeed = 0;
                }

                scrollingLeft = true;
                scrollingRight = false;
                break;
            case Input.Keys.RIGHT:
                if (!scrollingRight) {
                    xScrollSpeed = 0;
                }

                scrollingRight = true;
                scrollingLeft = false;
                break;
        }
    }

    /**
     * Stops scrolling the map
     *
     * @param keycode code of the released key
     */
    private void stopScrolling(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                scrollingUp = false;
                break;
            case Input.Keys.DOWN:
                scrollingDown = false;
                break;
            case Input.Keys.LEFT:
                scrollingLeft = false;
                break;
            case Input.Keys.RIGHT:
                scrollingRight = false;
                break;
        }
    }

    /**
     * Starts scrolling the map with the mouse
     *
     * @param screenX mouse pointer's x coordinate on the screen
     * @param screenY mouse pointer's y coordinate on the screen
     */
    private void initiateMouseScrolling(int screenX, int screenY) {
        if (screenX <= screenWidth * mouseTriggeredScrollBounds) {
            startScrolling(Input.Keys.LEFT);
        } else if (screenX >= screenWidth - screenWidth * mouseTriggeredScrollBounds) {
            startScrolling(Input.Keys.RIGHT);
        } else {
            stopScrolling(Input.Keys.LEFT);
            stopScrolling(Input.Keys.RIGHT);
        }

        if (screenY <= screenHeight * mouseTriggeredScrollBounds) {
            startScrolling(Input.Keys.UP);
        } else if (screenY >= screenHeight - screenHeight * mouseTriggeredScrollBounds) {
            startScrolling(Input.Keys.DOWN);
        } else {
            stopScrolling(Input.Keys.DOWN);
            stopScrolling(Input.Keys.UP);
        }
    }

    /**
     * Updates the camera's zoom
     *
     * @param cam game camera
     * @param delta time elapsed since the last update
     */
    private void updateZoom(OrthographicCamera cam, float delta) {
        cam.zoom += zoomSpeed * delta;

        zoomSpeed *= Math.max(0, 1f - delta);

        if (cam.zoom < maxZoomIn) {
            cam.zoom = maxZoomIn;
            zoomSpeed = 0;
        } else if (cam.zoom > maxZoomOut) {
            cam.zoom = maxZoomOut;
            zoomSpeed = 0;
        }
    }

    /**
     * Updates the speed of the camera's zoom
     *
     * @param scrollAmount how much was the mouse wheel scrolled
     */
    private void updateZoomSpeed(int scrollAmount) {
        zoomSpeed += scrollAmount / 10f;
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in Input.Keys
     *
     * @return whether the input was processed
     */
    public void keyDown(int keycode) {
        startScrolling(keycode);

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
     * @return whether the input was processed
     */
    public void keyUp(int keycode) {
        stopScrolling(keycode);
    }

    /**
     * Called when the screen was touched or a mouse button was pressed
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    public void touchDown(int screenX, int screenY, int pointer, int button) {

    }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    public void touchUp(int screenX, int screenY, int pointer, int button) {

    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    public void mouseMoved(int screenX, int screenY) {
        initiateMouseScrolling(screenX, screenY);
    }

    /**
     * Called when the mouse wheel is scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    public void scrolled(int amount) {
        updateZoomSpeed(amount);
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
     * Cleans up resources
     */
    public void unloadResources() {

    }
}
