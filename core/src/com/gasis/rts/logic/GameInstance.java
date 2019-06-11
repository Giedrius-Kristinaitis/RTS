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

        test = new Test();
    }

    /**
     * Called when the game should render itself
     *
     * @param batch sprite batch to draw sprites with
     */
    public void draw(SpriteBatch batch) {
        mapRenderer.render(batch, resources);

        test.render(batch, resources);
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

        test.update(delta);
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
        } else if (cam.position.x > map.getWidth() * Block.BLOCK_WIDTH - halfWidth * cam.zoom) {
            cam.position.x = map.getWidth() * Block.BLOCK_WIDTH - halfWidth * cam.zoom;
        }

        if (cam.position.y < halfHeight * cam.zoom) {
            cam.position.y = halfHeight * cam.zoom;
        } else if (cam.position.y > map.getHeight() * Block.BLOCK_HEIGHT - halfHeight * cam.zoom) {
            cam.position.y = map.getHeight() * Block.BLOCK_HEIGHT - halfHeight * cam.zoom;
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
