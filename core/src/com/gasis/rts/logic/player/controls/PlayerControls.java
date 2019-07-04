package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles controlling of a player
 */
public class PlayerControls implements Updatable, Renderable {

    // all the players that are being controlled
    protected List<Player> controlledPlayers = new ArrayList<Player>();

    // the game's map
    protected BlockMap map;

    // building placement logic
    protected BuildingPlacer buildingPlacer;

    /**
     * Default class constructor
     */
    public PlayerControls(BlockMap map, Player... players) {
        this.map = map;

        controlledPlayers.addAll(Arrays.asList(players));

        buildingPlacer = new BuildingPlacer(map);

        BuildingLoader loader = new BuildingLoader();
        loader.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + "machine_factory_conf"));

        buildingPlacer.initiateBuildingPlacement(loader);
    }

    /**
     * Called when the mouse is moved
     *
     * @param x mouse x relative to map's bottom left
     * @param y mouse y relative to map's bottom left
     */
    public void mouseMoved(float x, float y) {
        buildingPlacer.mouseMoved(x, y);
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {

    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        buildingPlacer.render(batch, resources);
    }
}
