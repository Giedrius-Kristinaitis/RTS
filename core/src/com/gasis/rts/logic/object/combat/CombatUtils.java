package com.gasis.rts.logic.object.combat;

import com.gasis.rts.math.Point;

import java.util.List;
import java.util.Map;

/**
 * Utility functions for combat related stuff
 */
public class CombatUtils {

    /**
     * Creates firing logic for the given fire sources
     *
     * @param fireSources fire sources to create logic for
     * @param firingData firing data
     *
     * @return newly created firing logic
     */
    public static FiringLogic createFiringLogic(List<FireSource> fireSources, FiringData firingData) {
        FiringLogic logic = new FiringLogic();

        logic.setShotCount(firingData.getShotCount());
        logic.setSiegeModeShotCount(firingData.getSiegeModeShotCount());
        logic.setShotInterval(firingData.getShotInterval());
        logic.setSiegeModeShotInterval(firingData.getSiegeModeShotInterval());
        logic.setReloadSpeed(firingData.getReloadSpeed());
        logic.setSiegeModeReloadSpeed(firingData.getSiegeModeReloadSpeed());

        for (int i = 0; i < fireSources.size(); i++) {
            // clone the fire source because many objects can't share the same instance
            FireSource source = createFireSource(
                    fireSources.get(i).getFireType(),
                    fireSources.get(i).getProjectileScale(),
                    fireSources.get(i).getGunCount(),
                    fireSources.get(i).getFlightTime(),
                    fireSources.get(i).getFirePoints()
            );

            logic.addFireSource(String.valueOf(i + 1), source);
        }

        return logic;
    }

    /**
     * Creates a new rotating gun with the given data
     *
     * @param data data for the rotating gun
     * @param firingData firing data
     * @param specs offensive specs of the holder
     *
     * @return newly created rotating gun
     */
    public static RotatingGun createRotatingGun(Map.Entry<RotatingGun, List<FireSource>> data, FiringData firingData, OffensiveSpecs specs) {
        RotatingGun gun = new RotatingGun();

        gun.setAtlas(data.getKey().getAtlas());
        gun.setTextures(data.getKey().getTextures());
        gun.setWidth(data.getKey().getWidth());
        gun.setHeight(data.getKey().getHeight());
        gun.setRecoil(data.getKey().getRecoil());
        gun.setRecoilResistance(data.getKey().getRecoilResistance());
        gun.setRotationSpeed(data.getKey().getRotationSpeed());
        gun.setOffensiveSpecs(specs);
        gun.setRelativeX(data.getKey().getRelativeX());
        gun.setRelativeY(data.getKey().getRelativeY());
        gun.setFiringLogic(createFiringLogic(data.getValue(), firingData));

        return gun;
    }

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
    public static FireSource createFireSource(byte type, byte scale, byte gunCount, float flightTime, List<Point> firePoints) {
        FireSource source = new FireSource();

        source.setFirePoints(firePoints);
        source.setFireType(type);
        source.setProjectileScale(scale);
        source.setFlightTime(flightTime);
        source.setGunCount(gunCount);

        return source;
    }
}
