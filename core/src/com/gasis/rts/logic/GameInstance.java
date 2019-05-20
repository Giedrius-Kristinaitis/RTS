package com.gasis.rts.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.map.Map;
import com.gasis.rts.logic.map.MapRenderer;
import com.gasis.rts.logic.map.blockmap.BlockMapGenerator;
import com.gasis.rts.logic.map.blockmap.BlockMapRenderer;
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
    }

    /**
     * Called when the game should render itself
     *
     * @param batch sprite batch to draw sprites with
     * @param delta time elapsed since last render
     */
    public void draw(SpriteBatch batch, float delta) {
        mapRenderer.render(batch, resources, delta);
    }

    /**
     * Called when the game state should be updated
     *
     * @param cam world's camera
     * @param delta time elapsed since last update
     */
    public void update(OrthographicCamera cam, float delta) {

    }

    /**
     * Cleans up resources
     */
    public void unloadResources() {

    }
}
