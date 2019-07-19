package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Unit selecting logic
 */
public class UnitSelector extends Selector {

    // selection rectangle's position and dimensions
    protected float selectionStartX;
    protected float selectionStartY;
    protected float selectionEndX;
    protected float selectionEndY;

    // should the selection rectangle be rendered or not
    protected boolean renderSelectionRectangle = false;

    // selected units (if any)
    protected List<Unit> selectedUnits = new ArrayList<Unit>();

    // unit selection listeners
    protected Set<UnitSelectionListener> listeners = new HashSet<UnitSelectionListener>();

    /**
     * Default class constructor
     * @param player
     */
    public UnitSelector(BlockMap map, Player player) {
        super(map, player);
    }

    /**
     * Adds a unit selection listener
     *
     * @param listener listener to add
     */
    public void addUnitSelectionListener(UnitSelectionListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a unit selection listener
     *
     * @param listener listener to remove
     */
    public void removeUnitSelectionListener(UnitSelectionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Called when the screen was touched or a mouse button was pressed
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    @Override
    public void touchDown(float x, float y, int pointer, int button) {
        selectionStartX = x;
        selectionStartY = y;
        selectionEndX = x;
        selectionEndY = y;
        deselectUnits();
    }

    /**
     * Selects a single unit
     *
     * @param x mouse x
     * @param y mouse y
     */
    protected void selectSingleUnit(float x, float y) {
        GameObject occupyingObject = map.getOccupyingObject((short) (x / Block.BLOCK_WIDTH), (short) (y / Block.BLOCK_HEIGHT));

        if (occupyingObject instanceof Unit) {
            Unit unit = (Unit) occupyingObject;

            if (player.getUnits().contains(unit)) {
                unit.setRenderHp(true);
                unit.setRenderSelectionCircle(true);
                selectedUnits.add(unit);
                notifySelectionListeners();
            }
        }
    }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    @Override
    public void touchUp(float x, float y, int pointer, int button) {
        renderSelectionRectangle = false;
        selectUnitsInSelectionRectangle();

        if (selectedUnits.size() == 0) {
            selectSingleUnit(x, y);
        }
    }

    /**
     * Called when a touch drag event occurs
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event
     */
    @Override
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
                unit.setRenderSelectionCircle(true);
                unit.setRenderHp(true);
                selectedUnits.add(unit);
            }
        }

        if (selectedUnits.size() > 0) {
            notifySelectionListeners();
        }
    }

    /**
     * Notifies unit selection listeners that units were selected
     */
    protected void notifySelectionListeners() {
        for (UnitSelectionListener listener: listeners) {
            listener.unitsSelected(selectedUnits);
        }
    }

    /**
     * Notifies unit deselection listeners that units were deselected
     */
    protected void notifyDeselectionListeners() {
        for (UnitSelectionListener listener: listeners) {
            listener.unitsDeselected();
        }
    }

    /**
     * Deselects all currently selected units
     */
    public void deselectUnits() {
        for (Unit unit: selectedUnits) {
            unit.setRenderHp(false);
            unit.setRenderSelectionCircle(false);
        }

        selectedUnits.clear();
        notifyDeselectionListeners();
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
     * Renders texture-less shapes
     *
     * @param shapeRenderer renderer to draw shapes to
     */
    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (renderSelectionRectangle) {
            renderSelectionRectangle(shapeRenderer);
        }
    }

    /**
     * Renders the unit selection rectangle
     *
     * @param shapeRenderer renderer to draw shapes to
     */
    protected void renderSelectionRectangle(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.WHITE);

        if (selectionEndX < selectionStartX || selectionEndY < selectionStartY) {
            shapeRenderer.rect(selectionEndX, selectionEndY, selectionStartX - selectionEndX, selectionStartY - selectionEndY);
        } else {
            shapeRenderer.rect(selectionStartX, selectionStartY, selectionEndX - selectionStartX, selectionEndY - selectionStartY);
        }
    }
}
