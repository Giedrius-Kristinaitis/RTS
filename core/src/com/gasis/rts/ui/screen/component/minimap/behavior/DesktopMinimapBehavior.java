package com.gasis.rts.ui.screen.component.minimap.behavior;

import com.gasis.rts.ui.screen.component.AbstractComponent;
import com.gasis.rts.ui.screen.component.ComponentBehaviorAdapter;
import com.gasis.rts.ui.screen.component.Minimap;

/**
 * Desktop minimap behavior
 */
public class DesktopMinimapBehavior extends ComponentBehaviorAdapter {

    /**
     * Performs touch down behavior
     *
     * @param component
     * @param x
     * @param y
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean behaveTouchDown(AbstractComponent component, float x, float y, int pointer, int button) {
        execute((Minimap) component, x, y);

        return true;
    }

    /**
     * Performs touch dragged behavior
     *
     * @param component
     * @param x
     * @param y
     * @param pointer
     * @return
     */
    @Override
    public boolean behaveTouchDragged(AbstractComponent component, float x, float y, int pointer) {
        execute((Minimap) component, x, y);

        return true;
    }

    /**
     * Executes the behavior
     *
     * @param minimap
     * @param x
     * @param y
     */
    protected void execute(Minimap minimap, float x, float y) {
        minimap.getNavigator().changePosition(minimap.getGameInstance().getCam(), x, y, minimap);
        minimap.getGameInstance().forceUpdateMapScroller();
    }
}
