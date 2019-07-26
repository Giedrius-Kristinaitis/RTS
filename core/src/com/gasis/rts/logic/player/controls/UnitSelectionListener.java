package com.gasis.rts.logic.player.controls;

import com.gasis.rts.logic.object.unit.Unit;

import java.util.Set;

/**
 * Listens for unit selection events
 */
public interface UnitSelectionListener {

    /**
     * Called when one or more units get selected
     *
     * @param selectedUnits set with all selected units
     */
    void unitsSelected(Set<Unit> selectedUnits);

    /**
     * Called when selected unit(-s) get deselected
     */
    void unitsDeselected();
}
