package com.gasis.rts.logic.object.combat;

import com.gasis.rts.math.Point;

import java.util.List;

/**
 * Creates fire sources
 */
public class FireSourceFactoryImplementation implements FireSourceFactory {

    /**
     * Creates a fire source
     *
     * @param type                 type of the fire/projectile
     * @param scale                scale of the fire/projectile
     * @param gunCount             how many guns are firing
     * @param flightTime           projectile's flight time
     * @param firePoints           list of fire points
     *
     * @return newly created fire source
     */
    @Override
    public FireSource createFireSource(byte type, byte scale, byte gunCount, float flightTime, List<Point> firePoints) {
        FireSource source = new FireSource();

        source.setFirePoints(firePoints);
        source.setFireType(type);
        source.setProjectileScale(scale);
        source.setFlightTime(flightTime);
        source.setGunCount(gunCount);

        return source;
    }
}
