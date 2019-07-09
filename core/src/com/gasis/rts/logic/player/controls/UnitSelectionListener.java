package com.gasis.rts.logic.player.controls;

import com.gasis.rts.logic.object.unit.Unit;

import java.util.List;

/**
 * Listens for unit selection events
 */
public interface UnitSelectionListener {

    /**
     * Called when one or more units get selected
     *
     * @param selectedUnits list with all selected units
     */
    void unitsSelected(List<Unit> selectedUnits);

    /**
     * Called when units get deselected
     */
    void unitsDeselected();
}
