package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.math.MathUtils;
import com.gasis.rts.math.Point;

import java.util.List;
import java.util.Map;

/**
 * Utility functions for combat related stuff
 */
public class CombatUtils {

    /**
     * Gets the facing direction for the unit at (x, y) which is aiming at (x2, y2)
     *
     * @param x x of the unit
     * @param y y of the unit
     * @param x2 x of the target
     * @param y2 y of the target
     * @return facing direction
     */
    public static byte getFacingDirection(float x, float y, float x2, float y2) {
        float xDiff = x - x2;
        float yDiff = y - y2;
        float distance = MathUtils.distance(x, x2, y, y2);

        if (Math.abs(yDiff) < distance / 3f) {
            if (xDiff > 0) {
                return Unit.WEST;
            } else {
                return Unit.EAST;
            }
        } else if (Math.abs(xDiff) < distance / 3f) {
            if (yDiff > 0) {
                return Unit.SOUTH;
            } else {
                return Unit.NORTH;
            }
        }

        if (yDiff > 0) {
            if (xDiff < 0) {
                return Unit.SOUTH_EAST;
            } else {
                return Unit.SOUTH_WEST;
            }
        } else {
            if (xDiff < 0) {
                return Unit.NORTH_EAST;
            } else {
                return Unit.NORTH_WEST;
            }
        }
    }

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
                    fireSources.get(i).getProjectileSpeed(),
                    fireSources.get(i).getFirePoints(),
                    fireSources.get(i).isPresentInSiegeMode(),
                    fireSources.get(i).isPresentOutOfSiegeMode(),
                    fireSources.get(i).getProjectileDeviation()
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
        gun.setCurrentlyPresent(true);
        gun.setPresentInSiegeMode(data.getKey().isPresentInSiegeMode());
        gun.setPresentOutOfSiegeMode(data.getKey().isPresentOutOfSiegeMode());
        gun.setCurrentlyPresent(data.getKey().isPresentOutOfSiegeMode());

        return gun;
    }

    /**
     * Creates a fire source
     *
     * @param type                 type of the fire/projectile
     * @param scale                scale of the fire/projectile
     * @param gunCount             how many guns are firing
     * @param projectileSpeed      speed of the projectile
     * @param firePoints           list of fire points
     * @param presentInSiegeMode   is the source present in siege mode
     * @param presentOutOfSiegeMode is the source present when not in siege mode
     * @param projectileDeviation  how much can the projectile deviate from it's target
     *
     * @return newly created fire source
     */
    public static FireSource createFireSource(byte type, byte scale, byte gunCount, float projectileSpeed, List<Point> firePoints, boolean presentInSiegeMode, boolean presentOutOfSiegeMode, float projectileDeviation) {
        FireSource source = new FireSource();

        source.setFirePoints(firePoints);
        source.setFireType(type);
        source.setProjectileScale(scale);
        source.setGunCount(gunCount);
        source.setProjectileSpeed(projectileSpeed);
        source.setPresentInSiegeMode(presentInSiegeMode);
        source.setPresentOutOfSiegeMode(presentOutOfSiegeMode);
        source.setProjectileDeviation(projectileDeviation);

        return source;
    }
}
