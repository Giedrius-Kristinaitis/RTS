package com.gasis.rts.ui.behavior;

import com.gasis.rts.ui.Behavior;
import com.gasis.rts.ui.screen.component.ComponentBehavior;
import com.gasis.rts.ui.screen.component.minimap.behavior.MobileMinimapBehavior;

/**
 * Mobile UI behavior
 */
public class MobileBehavior implements Behavior {

    /**
     * Gets the current minimap behavior
     *
     * @return
     */
    @Override
    public ComponentBehavior getMinimapBehavior() {
        return new MobileMinimapBehavior();
    }
}
