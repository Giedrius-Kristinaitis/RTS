package com.gasis.rts.ui;

import com.gasis.rts.ui.screen.component.ComponentBehavior;

/**
 * Abstract UI behavior
 */
public interface Behavior {

    /**
     * Gets the current minimap behavior
     *
     * @return
     */
    ComponentBehavior getMinimapBehavior();
}
