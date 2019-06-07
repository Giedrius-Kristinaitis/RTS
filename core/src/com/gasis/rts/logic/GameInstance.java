package com.gasis.rts.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.map.Map;
import com.gasis.rts.logic.map.MapRenderer;
import com.gasis.rts.logic.map.blockmap.BlockMapGenerator;
import com.gasis.rts.logic.map.blockmap.BlockMapRenderer;
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
    }

    /**
     * Called when the game state should be updated
     *
     * @param cam world's camera
     * @param delta time elapsed since last update
     */
    public void update(OrthographicCamera cam, float delta) {
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
    }

    /**
     * Cleans up resources
     */
    public void unloadResources() {

    }
}
