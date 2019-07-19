package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.combat.Aimable;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.movement.UnitMover;
import com.gasis.rts.logic.pathfinding.PathFinder;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.logic.tech.Tech;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles controlling of a player
 */
public class PlayerControls implements Updatable, Renderable, BuildingSelectionListener, UnitSelectionListener {

    // the player that is being controlled
    protected Player controlledPlayer;

    // the game's map
    protected BlockMap map;

    // building placement logic
    protected BuildingPlacer buildingPlacer;

    // all available control contexts
    protected Map<String, ControlContext> controlContexts = new HashMap<String, ControlContext>();

    // the currently active control context
    protected ControlContext currentContext;

    // the previous control context
    protected ControlContext previousControlContext;

    // unit selection logic
    protected UnitSelector unitSelector;

    // building selection logic
    protected BuildingSelector buildingSelector;

    // the code of the currently pressed key
    protected int pressedKey;

    // moves units
    protected UnitMover unitMover;

    /**
     * Default class constructor
     */
    public PlayerControls(BlockMap map, Player controlledPlayer) {
        this.map = map;
        this.controlledPlayer = controlledPlayer;

        buildingPlacer = new BuildingPlacer(map);
        unitSelector = new UnitSelector(map, controlledPlayer);
        buildingSelector = new BuildingSelector(map, controlledPlayer);

        unitSelector.addUnitSelectionListener(this);
        buildingSelector.addBuildingSelectionListener(this);

        unitMover = new UnitMover(new PathFinder(map));

        loadControlContexts();
    }

    /**
     * Called when a building gets selected
     *
     * @param building the selected building
     */
    @Override
    public void buildingSelected(Building building) {
        changeControlContext(building.getControlContextName());
        controlledPlayer.setSelectedBuilding(building);
    }

    /**
     * Called when one or more units get selected
     *
     * @param selectedUnits list with all selected units
     */
    @Override
    public void unitsSelected(List<Unit> selectedUnits) {
        ControlContextMultiplexer multiplexer = constructControlContextMultiplexer(selectedUnits);
        changeControlContext(multiplexer);
        controlledPlayer.setSelectedUnits(selectedUnits);
    }

    /**
     * Called when the selected building gets deselected
     */
    @Override
    public void buildingDeselected() {
        controlledPlayer.setSelectedBuilding(null);
    }

    /**
     * Called when selected unit(-s) get deselected
     */
    @Override
    public void unitsDeselected() {
        controlledPlayer.setSelectedUnits(null);
    }

    /**
     * Creates a control context multiplexer with the control contexts of the given units
     *
     * @param units units with different control contexts
     * @return
     */
    protected ControlContextMultiplexer constructControlContextMultiplexer(List<Unit> units) {
        ControlContextMultiplexer multiplexer = new ControlContextMultiplexer();

        for (Unit unit: units) {
            String controlContext = unit.getControlContextName();
            multiplexer.addControlContext(controlContexts.get(controlContext));
        }

        return multiplexer;
    }

    /**
     * Loads available control contexts
     */
    protected void loadControlContexts() {
        FileHandle dir = Gdx.files.internal(Constants.FOLDER_CONTROL_CONTEXTS);

        for (FileHandle file: dir.list()) {
            if (!file.isDirectory()) {
                loadControlContext(file);
            }
        }

        setDefaultControlContext();
    }

    /**
     * Changes the current control context
     *
     * @param name name of the new control context
     */
    protected void changeControlContext(String name) {
        previousControlContext = currentContext;
        currentContext = controlContexts.get(name);
    }

    /**
     * Changes the current control context
     *
     * @param context new control context
     */
    protected void changeControlContext(ControlContext context) {
        previousControlContext = currentContext;
        currentContext = context;
    }

    /**
     * Sets the default control context
     */
    protected void setDefaultControlContext() {
        currentContext = controlContexts.get(controlledPlayer.getFaction().getDefaultControlContextName());
        previousControlContext = currentContext;
    }

    /**
     * Loads a single control context
     *
     * @param file file to load the context from
     */
    protected void loadControlContext(FileHandle file) {
        ControlContext context = new ControlContext();
        context.load(file, buildingPlacer);
        controlContexts.put(file.name(), context);
    }

    /**
     * Called when the mouse is moved
     *
     * @param x mouse x relative to map's bottom left
     * @param y mouse y relative to map's bottom left
     */
    public void mouseMoved(float x, float y) {
        buildingPlacer.mouseMoved(x, y);
        buildingSelector.mouseMoved(x, y);
        unitSelector.mouseMoved(x, y);
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
        if (button == Input.Buttons.RIGHT) {
            if (!handleUnitCombatControls(x, y)) {
                handleUnitMovementControls(x, y);
            }

            handleBuildingCombatControls(x, y);
        } else if (button == Input.Buttons.LEFT) {
            buildingSelector.touchDown(x, y, pointer, button);
            unitSelector.touchDown(x, y, pointer, button);
        }

        handleBuildingPlacement(button);
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
        if (button == Input.Buttons.LEFT) {
            buildingSelector.touchUp(x, y, pointer, button);
            unitSelector.touchUp(x, y, pointer, button);
        }

        if (buildingSelector.getSelectedBuilding() == null && unitSelector.getSelectedUnits().size() == 0 && currentContext != controlContexts.get(controlledPlayer.getFaction().getDefaultControlContextName())) {
            changeControlContext(controlledPlayer.getFaction().getDefaultControlContextName());
        }
    }

    /**
     * Called when a touch drag event occurs
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event
     */
    public void touchDragged(float x, float y, int pointer) {
        unitSelector.touchDragged(x, y, pointer);
    }

    /**
     * Called when a key on the keyboard is released
     *
     * @param keycode code of the released key
     */
    public void keyDown(int keycode) {
        handleControlContextHotkeys(keycode);

        pressedKey = keycode;
    }

    /**
     * Called when a key on the keyboard is pressed down
     *
     * @param keycode code of the pressed key
     */
    public void keyUp(int keycode) {
        pressedKey = -1;
    }

    /**
     * Handles unit combat controls that are not tactical techs
     *
     * @param x mouse x in world coordinates
     * @param y mouse y in world coordinates
     */
    protected boolean handleUnitCombatControls(float x, float y) {
        if (pressedKey == Input.Keys.CONTROL_LEFT || pressedKey == Input.Keys.CONTROL_RIGHT) {
            aimSelectedUnits(x, y);
            return true;
        }

        return false;
    }

    /**
     * Handles unit movement controls that are not tactical techs
     *
     * @param x mouse x in world coordinates
     * @param y mouse y in world coordinates
     */
    protected void handleUnitMovementControls(float x, float y) {
        orderUnitsToMove((short) (x / Block.BLOCK_WIDTH), (short) (y / Block.BLOCK_HEIGHT));
    }

    /**
     * Orders selected units to move to the specified block on the map
     *
     * @param x destination x in block map coordinates
     * @param y destination y in block map coordinates
     */
    protected void orderUnitsToMove(short x, short y) {
        if (controlledPlayer.getSelectedUnits() != null) {
            unitMover.moveUnits(controlledPlayer.getSelectedUnits(), x, y);
        }
    }

    /**
     * Handles building combat controls
     *
     * @param x mouse x in world coords
     * @param y mouse y in world coords
     */
    protected boolean handleBuildingCombatControls(float x, float y) {
        if (pressedKey == Input.Keys.CONTROL_LEFT || pressedKey == Input.Keys.CONTROL_RIGHT) {
            aimSelectedBuilding(x, y);
            return true;
        }

        return false;
    }

    /**
     * Aims all currently selected units at the given point
     *
     * @param x x of the target
     * @param y y of the target
     */
    protected void aimSelectedUnits(float x, float y) {
        for (Unit unit: unitSelector.getSelectedUnits()) {
            unit.aimAt(x, y);
        }
    }

    /**
     * Aims selected building at the given point
     *
     * @param x x of the target
     * @param y y of the target
     */
    protected void aimSelectedBuilding(float x, float y) {
        if (buildingSelector.getSelectedBuilding() != null && buildingSelector.getSelectedBuilding() instanceof Aimable) {
            ((Aimable) buildingSelector.getSelectedBuilding()).aimAt(x, y);
        }
    }

    /**
     * Handles current control context's hotkeys
     *
     * @param keycode code of the pressed key
     */
    protected void handleControlContextHotkeys(int keycode) {
        String pressedKey = Input.Keys.toString(keycode);
        Tech tech = currentContext.getTech(pressedKey);

        if (tech != null) {
            tech.apply(controlledPlayer);
        }
    }

    /**
     * Handles building placement logic
     *
     * @param mouseButton the mouse button that was just pressed
     */
    protected void handleBuildingPlacement(int mouseButton) {
        if (buildingPlacer.isPlacing()) {
            if (mouseButton == Input.Buttons.LEFT) {
                buildingPlacer.finishPlacement(controlledPlayer);
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
        unitMover.update(delta);
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
        unitSelector.render(batch, resources);
        buildingSelector.render(batch, resources);
    }

    /**
     * Renders texture-less shapes
     *
     * @param shapeRenderer renderer to draw to
     */
    public void render(ShapeRenderer shapeRenderer) {
        unitSelector.render(shapeRenderer);
        buildingSelector.render(shapeRenderer);
    }
}
