package com.gasis.rts.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.map.Map;
import com.gasis.rts.logic.map.MapRenderer;
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
public class GameInstance {

    // resources used by the game
    private Resources resources;

    // renders the game map
    private MapRenderer mapRenderer;

    // game map
    private Map map;

    // the current zoom value
    private float zoom = 1;

    // zoom limitations
    private final float maxZoomIn = 0.5f;
    private final float maxZoomOut = 1.25f;

    // how much the zoom changes per second
    private float zoomSpeed;

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
        mapRenderer.setRenderX(0);
        mapRenderer.setRenderY(0);
        mapRenderer.setRenderWidth(Constants.WIDTH);
        mapRenderer.setRenderHeight(Constants.HEIGHT);

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
     * @param delta time elapsed since last render
     */
    public void draw(SpriteBatch batch, float delta) {
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
     * @param cam world's camera
     * @param delta time elapsed since last update
     */
    public void update(OrthographicCamera cam, float delta) {
        updateZoom(cam, delta);

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
     * Updates the camera's zoom
     */
    private void updateZoom(OrthographicCamera cam, float delta) {
        cam.zoom += zoomSpeed * delta;
        zoomSpeed *= Math.max(0, 1 - delta);

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
     * Called when the size of the viewport changes
     *
     * @param width new width
     * @param height new height
     */
    public void viewportDimensionsChanged(int width, int height) {

    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in Input.Keys
     * @return whether the input was processed
     */
    public void keyDown(int keycode) {

    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in Input.Keys
     * @return whether the input was processed
     */
    public void keyUp(int keycode) {

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
     * Cleans up resources
     */
    public void unloadResources() {

    }
}
