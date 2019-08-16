package com.gasis.rts.logic.object.research;

import com.gasis.rts.logic.tech.Tech;

/**
 * Researches techs
 */
public interface TechReasearcher {

    /**
     * Queues up a tech to be researched
     *
     * @param tech tech to research
     */
    void queueUpTech(Tech tech);
}
