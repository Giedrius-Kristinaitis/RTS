package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.resources.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit selecting logic
 */
public class UnitSelector implements Renderable {

    // the game's map
    protected BlockMap map;

    // player whose units are being selected
    protected Player player;

    // used to render texture-less shapes
    protected ShapeRenderer shapeRenderer;

    // selection rectangle's position and dimensions
    protected float selectionStartX;
    protected float selectionStartY;
    protected float selectionEndX;
    protected float selectionEndY;

    // should the selection rectangle be rendered or not
    protected boolean renderSelectionRectangle = false;

    // selected units (if any)
    protected List<Unit> selectedUnits = new ArrayList<Unit>();

    /**
     * Default class constructor
     * @param player
     */
    public UnitSelector(BlockMap map, ShapeRenderer shapeRenderer, Player player) {
        this.map = map;
        this.shapeRenderer = shapeRenderer;
        this.player = player;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchDown(float x, float y, int pointer, int button) {
        selectionStartX = x;
        selectionStartY = y;
        selectionEndX = x;
        selectionEndY = y;
        deselectUnits();
    }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchUp(float x, float y, int pointer, int button) {
        renderSelectionRectangle = false;
        selectUnitsInSelectionRectangle();
    }

    /**
     * Called when a touch drag event occurs
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event
     */
    public void touchDragged(float x, float y, int pointer) {
        renderSelectionRectangle = true;

        selectionEndX = x;
        selectionEndY = y;
    }

    /**
     * Selects player's units that currently are in the selection rectangle
     */
    protected void selectUnitsInSelectionRectangle() {
        for (Unit unit: player.getUnits()) {
            if (isInSelectionRectangle(unit.getCenterX(), unit.getCenterY())) {
                selectedUnits.add(unit);
            }
        }
    }

    /**
     * Deselects all currently selected units
     */
    public void deselectUnits() {
        for (Unit unit: selectedUnits) {
            
        }

        selectedUnits.clear();
    }

    /**
     * Checks if the specified point is in the selection rectangle
     *
     * @param x x of the point
     * @param y y of the point
     * @return
     */
    protected boolean isInSelectionRectangle(float x, float y) {
        if (x >= Math.min(selectionStartX, selectionEndX) && x <= Math.min(selectionStartX, selectionEndX) + Math.abs(selectionStartX - selectionEndX)) {
            if (y >= Math.min(selectionStartY, selectionEndY) && y <= Math.min(selectionStartY, selectionEndY) + Math.abs(selectionStartY - selectionEndY)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets all currently selected units
     * @return
     */
    public List<Unit> getSelectedUnits() {
        return selectedUnits;
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        if (renderSelectionRectangle) {
            renderSelectionRectangle();
        }
    }

    /**
     * Renders the unit selection rectangle
     */
    protected void renderSelectionRectangle() {
        shapeRenderer.setColor(Color.WHITE);

        if (selectionEndX < selectionStartX || selectionEndY < selectionStartY) {
            shapeRenderer.rect(selectionEndX, selectionEndY, selectionStartX - selectionEndX, selectionStartY - selectionEndY);
        } else {
            shapeRenderer.rect(selectionStartX, selectionStartY, selectionEndX - selectionStartX, selectionEndY - selectionStartY);
        }
    }
}
