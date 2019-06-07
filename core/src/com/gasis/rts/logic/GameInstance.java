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

        unit5.setX(5);
        unit5.setY(5);
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
    }

    /**
     * Called when the game state should be updated
     *
     * @param cam world's camera
     * @param delta time elapsed since last update
     */
    public void update(OrthographicCamera cam, float delta) {
        unit1.update(delta);
        unit2.update(delta);
        unit3.update(delta);
        unit4.update(delta);
        unit5.update(delta);
        unit6.update(delta);
        unit7.update(delta);
        unit8.update(delta);
    }

    /**
     * Cleans up resources
     */
    public void unloadResources() {

    }
}
