package com.gasis.rts.logic.player.controls;

import com.gasis.rts.logic.tech.Tech;

import java.util.Map;

/**
 * Control context that consists of one or more control contexts
 */
public class ControlContextMultiplexer extends ControlContext {

    /**
     * Adds a control context to the multiplexer
     *
     * @param context context to add
     */
    public void addControlContext(ControlContext context) {
        for (Map.Entry<String, Tech> tech : context.techs.entrySet()) {
            techs.put(tech.getKey(), tech.getValue());
        }
    }
}
