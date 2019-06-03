package com.gasis.rts.logic.object.combat;

import com.gasis.rts.math.Point;

import java.util.List;

/**
 * Creates fire sources
 */
public interface FireSourceFactory {

    /**
     * Creates a fire source
     *
     * @param type type of the fire/projectile
     * @param scale scale of the fire/projectile
     * @param gunCount how many guns are firing
     * @param flightTime projectile's flight time
     * @param firePoints list of fire points
     *
     * @return newly created fire source
     */
    FireSource createFireSource(byte type, byte scale, byte gunCount, float flightTime, List<Point> firePoints);
}
