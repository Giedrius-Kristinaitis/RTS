package com.gasis.rts.logic;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.resources.Resources;

/**
 * Game world. Holds game state, draws the game world and updates it
 */
public class GameWorld {

    // resources used by the game
    private Resources resources;

    // game map
    private BlockMap map;

    /**
     * Default class constructor
     * @param resources resources used by the game
     */
    public GameWorld(Resources resources) {
        this.resources = resources;
    }

    /**
     * Called when the game should render itself
     * @param batch sprite batch to draw sprites with
     * @param delta time elapsed since last render
     */
    public void draw(SpriteBatch batch, float delta) {
        map.render(batch, resources, delta);
    }

    /**
     * Called when the game state should be updated
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
