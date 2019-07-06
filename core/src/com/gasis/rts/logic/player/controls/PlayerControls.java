package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.resources.Resources;

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
     * Called when the screen was touched or a mouse button was pressed
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchDown(int screenX, int screenY, int pointer, int button) {
        handleBuildingPlacement(button);
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

    }

    /**
     * Called when a key on the keyboard is released
     *
     * @param keycode code of the released key
     */
    public void keyDown(int keycode) {

    }

    /**
     * Called when a key on the keyboard is pressed down
     *
     * @param keycode code of the pressed key
     */
    public void keyUp(int keycode) {

    }

    /**
     * Handles building placement logic
     *
     * @param mouseButton the mouse button that was just pressed
     */
    protected void handleBuildingPlacement(int mouseButton) {
        if (buildingPlacer.isPlacing()) {
            if (mouseButton == Input.Buttons.LEFT) {
                buildingPlacer.finishPlacement();
            } else if (mouseButton == Input.Buttons.RIGHT) {
                buildingPlacer.cancelPlacement();
            }
        }
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
