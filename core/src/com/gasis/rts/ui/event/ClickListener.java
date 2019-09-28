package com.gasis.rts.ui.event;

import com.gasis.rts.ui.component.ClickableComponent;

/**
 * UI component's click callback interface
 */
public interface ClickListener {

    /**
     * Called when a ui component gets clicked
     *
     * @param component component that was just clicked
     */
    void clicked(ClickableComponent component);
}
